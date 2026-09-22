package com.flylab.sim.backend

/**
 * Accelerator-neutral compute backend interface.
 * Decouples the simulation execution strategy from the biological logic.
 */
interface SimulationComputeBackend {
    val name: String
    val type: BackendType

    fun initialize()
    fun shutdown()
    
    // In actual implementation, this could return a Future or coroutine, but for this abstraction
    // we keep it simple. It allows capability discovery at runtime.
    fun isAvailable(): Boolean
}

enum class BackendType {
    CPU,
    GPU,
    NPU
}
