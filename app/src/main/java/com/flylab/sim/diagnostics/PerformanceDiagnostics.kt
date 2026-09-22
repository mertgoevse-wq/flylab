package com.flylab.sim.diagnostics

/**
 * Performance tracking layer.
 */
data class DiagnosticsSnapshot(
    val tickDurationMs: Float,
    val renderTimeMs: Float,
    val inferenceLatencyMs: Float,
    val activeOrganisms: Int,
    val computeBackend: String,
    val droppedFrames: Int
)

class PerformanceDiagnostics {
    private var lastTickStart: Long = 0
    private var lastTickDuration: Float = 0f
    
    private var lastInferenceStart: Long = 0
    private var lastInferenceLatency: Float = 0f

    private var renderTime: Float = 16.6f
    private var dropped: Int = 0
    
    var currentBackendName: String = "CPU Ref"

    fun startTick() {
        lastTickStart = System.nanoTime()
    }

    fun endTick() {
        lastTickDuration = (System.nanoTime() - lastTickStart) / 1_000_000f
    }

    fun startInference() {
        lastInferenceStart = System.nanoTime()
    }

    fun endInference() {
        lastInferenceLatency = (System.nanoTime() - lastInferenceStart) / 1_000_000f
    }
    
    fun setRenderTime(ms: Float) {
        renderTime = ms
    }
    
    fun recordDroppedFrame() {
        dropped++
    }

    fun getSnapshot(activeOrganisms: Int): DiagnosticsSnapshot {
        return DiagnosticsSnapshot(
            tickDurationMs = lastTickDuration,
            renderTimeMs = renderTime,
            inferenceLatencyMs = lastInferenceLatency,
            activeOrganisms = activeOrganisms,
            computeBackend = currentBackendName,
            droppedFrames = dropped
        )
    }
}
