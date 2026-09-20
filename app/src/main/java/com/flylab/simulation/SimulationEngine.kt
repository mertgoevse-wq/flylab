package com.flylab.simulation

import com.flylab.domain.brain.Brain
import com.flylab.domain.connectome.Connectome
import com.flylab.domain.memory.MemorySystem
import com.flylab.domain.neural.NeuralActivity
import com.flylab.domain.neural.RegionalActivity
import com.flylab.domain.plasticity.PlasticityEngine
import com.flylab.domain.plasticity.RewardModulatedHebbian
import com.flylab.domain.simulation.SimulationConfig
import com.flylab.domain.simulation.SimulationState
import com.flylab.domain.timeline.Timeline
import kotlin.random.Random

/**
 * Core simulation engine.
 * Runs neural activity simulation at configurable levels of detail.
 */
class SimulationEngine(
    private val config: SimulationConfig
) {
    private val random = Random(config.seed)
    private val plasticityEngine = PlasticityEngine(
        rule = RewardModulatedHebbian(config.learningRate)
    )

    fun createInitialState(
        brain: Brain,
        connectome: Connectome
    ): SimulationState {
        return SimulationState(
            simulationId = generateId(),
            timestamp = System.currentTimeMillis(),
            brain = brain,
            connectome = connectome,
            neuralActivity = null,
            memorySystem = MemorySystem(),
            timeline = Timeline(
                experimentId = generateId(),
                startTime = System.currentTimeMillis(),
                seed = config.seed
            ),
            levelOfDetail = config.levelOfDetail
        )
    }

    fun step(state: SimulationState): SimulationState {
        // Compute regional activity (LOD 1)
        val regionActivities = computeRegionalActivity(state)

        val activity = RegionalActivity(
            timestamp = state.timestamp + config.timeStep.toLong(),
            regionActivities = regionActivities
        )

        return state.copy(
            timestamp = state.timestamp + config.timeStep.toLong(),
            neuralActivity = activity
        )
    }

    private fun computeRegionalActivity(state: SimulationState): Map<String, Float> {
        // Simple baseline activity with small random fluctuations
        return state.brain.regions.associate { region ->
            region.id to (0.1f + random.nextFloat() * 0.05f)
        }
    }

    private fun generateId(): String {
        return "sim_${System.currentTimeMillis()}_${random.nextInt(10000)}"
    }
}
