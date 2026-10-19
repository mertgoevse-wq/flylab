package com.flylab.sim.backend

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * Accelerator dispatcher for the KC->MBON neural kernel.
 *
 * Loads `assets/neural_kernel.tflite` (deterministic hand-encoded kernel,
 * 32 KC -> 8 MBON, FULLY_CONNECTED + TANH, generator: tools/generate_npu_kernel.py)
 * and tries inference backends in order:
 *   1. NNAPI delegate  -> vendor NPU dispatch (Samsung Exynos ENN on the A56)
 *   2. GPU delegate    -> Xclipse 540 (LiteRT GPU delegate: OpenCL/Vulkan)
 *   3. CPU fallback    -> always available
 *
 * Every delegate must pass a numeric self-test against the CPU reference
 * stored in `assets/neural_kernel_manifest.json` (tolerance 2e-2 for NPU
 * fp16 rounding). A failed hardware path is rejected, never silently
 * tolerated — scientific integrity (see docs/NPU_GPU_RESEARCH.md).
 *
 * Evidence level: MODELED. Numerical neural-dynamics offload stays behind
 * an explicit opt-in flag (see OFFLOAD_PROPERTY below) and is NOT enabled
 * by default.
 */
object AcceleratorBridge {

    const val OFFLOAD_PROPERTY = "flylab.npu.offload"
    const val STATUS_NOT_INITIALIZED = "not initialized"

    enum class Strategy { NPU, GPU, CPU }

    private const val KC = 32
    private const val MBON = 8
    private const val TOLERANCE = 2e-2f

    @Volatile private var interpreter: Interpreter? = null
    @Volatile private var gpuDelegate: GpuDelegate? = null
    // NnapiDelegate is resolved reflectively: some LiteRT AAR distributions
    // do not ship the nnapi wrapper; a missing class then cleanly rules out
    // the NPU path at runtime instead of breaking the build.
    @Volatile private var nnapiHandle: Any? = null

    var status = "not initialized"
        private set
    var strategy: Strategy = Strategy.CPU
        private set

    val isReady: Boolean get() = interpreter != null

    // ---------------- strategy selection (pure, unit-testable) ----------------

    fun selectStrategy(report: AcceleratorProbe.Report): List<Strategy> {
        val order = mutableListOf<Strategy>()
        if (report.nnapiLikely) order.add(Strategy.NPU)
        if (report.vulkanAvailable || report.npuLikely) order.add(Strategy.GPU)
        order.add(Strategy.CPU)
        return order
    }

    // ---------------- lifecycle ----------------

    fun initialize(context: Context) {
        shutdown()
        try {
            val report = AcceleratorProbe.probe(context)
            val model = loadModel(context)
            val reference = loadReference(context)

            for (candidate in selectStrategy(report)) {
                val result = tryStrategy(model, reference, candidate)
                if (result == null) {
                    status = "strategy $candidate failed validation"
                    continue
                }
                interpreter = result
                strategy = candidate
                status = "active backend=$candidate (self-test OK; ${CandidateStatus.current})"
                return
            }
            strategy = Strategy.CPU
            status = "all hardware paths rejected; plain CPU interpreter active"
        } catch (t: Throwable) {
            strategy = Strategy.CPU
            status = "initialization failed, CPU dynamics only: ${t.javaClass.simpleName}"
        }
    }

    fun shutdown() {
        try {
            interpreter?.close()
            interpreter = null
            gpuDelegate?.close()
            gpuDelegate = null
            try { nnapiHandle?.javaClass?.getMethod("close")?.invoke(nnapiHandle) } catch (_: Throwable) { }
            nnapiHandle = null
        } catch (_: Throwable) {
        }
    }

    // ---------------- inference ----------------

    /**
     * Runs the KC->MBON kernel on the active accelerator.
     * Returns null if the bridge cannot produce a trusted result —
     * the caller MUST then fall back to the CPU dynamics path.
     */
    fun inferMBON(kcActivations: FloatArray): FloatArray? {
        val inter = interpreter ?: return null
        if (kcActivations.size != KC) return null
        return try {
            val input = Array(1) { kcActivations.copyOf() }
            val output = Array(1) { FloatArray(MBON) }
            inter.run(input, output)
            output[0]
        } catch (t: Throwable) {
            null
        }
    }

    /**
     * Optional opt-in numerical offload for the sim loop
     * (see NeuralDynamics KC->MBON path). Defaults to OFF because the
     * hardware kernel (tanh) is a proxy, not bit-identical to the
     * sigmoid dynamics of the biological model.
     */
    fun offloadEnabled(): Boolean =
        System.getProperty(OFFLOAD_PROPERTY) == "true"

    // ---------------- internals ----------------

    private object CandidateStatus {
        var current: String = ""
    }

    private fun tryStrategy(
        model: MappedByteBuffer,
        reference: Reference,
        candidate: Strategy
    ): Interpreter? {
        val options = Interpreter.Options()
        try {
            when (candidate) {
                Strategy.NPU -> {
                    val delegate = tryDelegate("org.tensorflow.lite.nnapi.NnapiDelegate")
                        ?: return null
                    options.addDelegate(delegate)
                    nnapiHandle = delegate
                }
                Strategy.GPU -> {
                    val delegate = GpuDelegate()
                    options.addDelegate(delegate)
                    gpuDelegate = delegate
                }
                Strategy.CPU -> { /* no delegate */ }
            }
            val inter = Interpreter(model, options)

            // Numeric self-test: run reference input, compare against manifest
            val probeY = Array(1) { reference.x0.copyOf() }
            val out = Array(1) { FloatArray(MBON) }
            inter.run(probeY, out)
            val maxErr = out[0].zip(reference.y0).maxOf { (a, b) -> kotlin.math.abs(a - b) }
            CandidateStatus.current = "maxErr=%.4f".format(maxErr)
            if (maxErr > TOLERANCE) {
                inter.close()
                disposeDelegates(candidate)
                return null
            }
            return inter
        } catch (t: Throwable) {
            disposeDelegates(candidate)
            return null
        }
    }

    private fun disposeDelegates(candidate: Strategy) {
        try {
            when (candidate) {
                Strategy.NPU -> {
                    try { nnapiHandle?.javaClass?.getMethod("close")?.invoke(nnapiHandle) } catch (_: Throwable) { }
                    nnapiHandle = null
                }
                Strategy.GPU -> { gpuDelegate?.close(); gpuDelegate = null }
                Strategy.CPU -> { }
            }
        } catch (_: Throwable) { }
    }

    /** Reflection-instantiates a delegate class, null if class/library absent. */
    private fun tryDelegate(className: String): org.tensorflow.lite.Delegate? =
        try {
            Class.forName(className)
                .getDeclaredConstructor()
                .newInstance() as org.tensorflow.lite.Delegate
        } catch (t: Throwable) {
            null
        }

    private fun loadModel(context: Context): MappedByteBuffer =
        context.assets.openFd("neural_kernel.tflite").use { fd ->
            java.io.FileInputStream(fd.fileDescriptor).channel.map(
                FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength
            )
        }

    data class Reference(val x0: FloatArray, val y0: FloatArray)

    private fun loadReference(context: Context): Reference {
        val json = context.assets.open("neural_kernel_manifest.json")
            .bufferedReader().use { it.readText() }
        val map: Map<String, Any> = Gson().fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
        val x0 = (map["probe_input_x0"] as List<*>).map { (it as Double).toFloat() }.toFloatArray()
        val y0 = (map["expected_output_y0"] as List<*>).map { (it as Double).toFloat() }.toFloatArray()
        return Reference(x0, y0)
    }
}
