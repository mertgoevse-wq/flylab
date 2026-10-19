package com.flylab.sim.diagnostics

import com.flylab.sim.backend.AcceleratorBridge

/**
 * Publishes the externally initialized accelerator state (LiteRT NNAPI/GPU
 * delegate self-test result) into the engine diagnostics without touching
 * the deterministic simulation path. See docs/NPU_GPU_RESEARCH.md.
 */
object PerformanceDiagnosticsBridge {

    @Volatile
    var publishedBackendName: String = "CPU Ref"
        private set

    @Volatile
    var publishedStatus: String = AcceleratorBridge.STATUS_NOT_INITIALIZED
        private set

    fun publish() {
        publishedBackendName = when (AcceleratorBridge.strategy) {
            AcceleratorBridge.Strategy.NPU -> "NPU (ENNSIFIED NNAPI)"
            AcceleratorBridge.Strategy.GPU -> "GPU (LiteRT delegate)"
            AcceleratorBridge.Strategy.CPU -> "CPU Ref"
        }
        publishedStatus = AcceleratorBridge.status
    }
}
