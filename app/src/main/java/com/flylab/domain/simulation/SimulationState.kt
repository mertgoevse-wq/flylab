package com.flylab.domain.simulation

import com.flylab.domain.brain.Brain
import com.flylab.domain.connectome.Connectome
import com.flylab.domain.memory.MemorySystem
import com.flylab.domain.neural.NeuralActivity
import com.flylab.domain.timeline.Timeline

/**
 * Complete simulation state at a given time point.
 * Supports save/restore for replay.
 */
data class SimulationState(
    val simulationId: String,
    val timestamp: Long,
    val brain: Brain,
    val connectome: Connectome,
    val neuralActivity: NeuralActivity?,
    val memorySystem: MemorySystem,
    val timeline: Timeline,
    val levelOfDetail: Int = 1,
    val isPaused: Boolean = false
)

/**
 * Simulation configuration.
 */
data class SimulationConfig(
    val seed: Long = System.currentTimeMillis(),
    val timeStep: Float = 1f, // milliseconds per simulation step
    val levelOfDetail: Int = 1,
    val enablePlasticity: Boolean = true,
    val enableMemory: Boolean = true,
    val learningRate: Float = 0.01f
)
