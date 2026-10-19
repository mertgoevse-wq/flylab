package com.flylab.sim.backend

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File

/**
 * Runtime probe for hardware accelerators on the target device.
 *
 * Evidence gathered on the Samsung Galaxy A56 (Exynos 1580) from the vendor
 * filesystem (see docs/NPU_GPU_RESEARCH.md):
 *  - NPU:   Samsung SLSI ENN runtime 2.3.28 in /vendor/lib64
 *           (libenn_public_api_cpp.so + libenn_user_driver_{cpu,gpu,unified}.so),
 *           a vendor AIDL HAL service (vendor.samsung_slsi.hardware.enn_aidl)
 *           and /dev/{npu0,npu1,npu,unpu}_throughput device nodes (2 NPU cores).
 *           Accessible from an app only through framework dispatch
 *           (LiteRT NNAPI delegate), never by loading vendor .so directly.
 *  - GPU:   Xclipse 540 (RDNA2-based), Vulkan >= 1.1 via PackageManager features.
 *
 * All values are *availability guesses + evidence* — the authoritative verdict
 * comes from the first successful LiteRT self-test in AcceleratorBridge.
 * Everything here is engineering evidence (MODELED), no behaviour claims.
 */
object AcceleratorProbe {

    data class Report(
        val vulkanAvailable: Boolean,
        val npuLikely: Boolean,
        val nnapiLikely: Boolean,
        val evidence: List<String>
    ) {
        fun summary(): String =
            "vulkan=$vulkanAvailable npu=$npuLikely nnapi=$nnapiLikely"
    }

    fun probe(context: Context?): Report {
        val evidence = mutableListOf<String>()

        // ---- GPU: Vulkan feature declaration ----------------------------
        var vulkan = false
        if (context != null) {
            try {
                val pm: PackageManager = context.packageManager
                vulkan = pm.hasSystemFeature("android.hardware.vulkan.version")
                if (vulkan) evidence.add("packageManager: android.hardware.vulkan.version declared")
            } catch (t: Throwable) {
                evidence.add("packageManager probe failed: ${t.javaClass.simpleName}")
            }
        } else {
            evidence.add("no context: GPU feature probe skipped")
        }

        // ---- NPU: vendor ENN runtime + platform heuristics ---------------
        var npu = false
        var nnapi = false

        // (a) vendor library evidence — may be denied by SELinux for apps;
        //     a "false" here is *not* proof of absence.
        try {
            val lib = File("/vendor/lib64/libenn_public_api_cpp.so")
            if (lib.exists()) {
                npu = true
                nnapi = true
                evidence.add("vendor: ${lib.path} exists (Samsung SLSI ENN runtime)")
            }
        } catch (t: Throwable) {
            evidence.add("vendor ENN check denied: ${t.javaClass.simpleName}")
        }

        // (b) platform heuristic: Exynos 1580 ships the ENN NPU (MODELED)
        val hw = (Build.HARDWARE + " " + Build.FINGERPRINT).lowercase()
        if (hw.contains("exynos1580") || hw.contains("s5e8835")) {
            npu = true
            evidence.add("platform: Exynos 1580 / s5e8835 with ENN NPU (MODELED)")
        }

        // (c) NNAPI runtime heuristic: available only below/around the
        //     documented deprecation cutoff and plausible drivers (Android 15
        //     keeps drivers functional on Exynos mid-range; MODELED).
        if (Build.VERSION.SDK_INT < 36 && npu) nnapi = true

        if (npu && !File("/dev/npu0_throughput").exists()) {
            // device nodes usually not visible from app context — informational only
            evidence.add("note: /dev/npu*_throughput not visible from app context (expected)")
        }

        return Report(vulkan, npu, nnapi, evidence)
    }
}
