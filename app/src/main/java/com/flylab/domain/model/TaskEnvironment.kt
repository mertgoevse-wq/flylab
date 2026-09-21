package com.flylab.domain.model

import com.flylab.sim.SimulationSnapshot

/**
 * Task environment protocol architecture.
 * Determines trial state, constraints, logging, and evaluation metrics for behavioral experiments.
 */
interface TaskEnvironment {
    val id: String
    val name: String
    val baseEnvironment: Environment

    /**
     * Resets the boundary parameters, sensory states, and fly coordinates for a new trial.
     */
    fun setupTrial(fly: Fly): Fly

    /**
     * Updates and logs environmental state given the latest fly state.
     * Evaluates termination criteria (e.g. food reached, or time expired).
     */
    fun evaluate(snapshot: SimulationSnapshot): TaskEvaluationResult
}

data class TaskEvaluationResult(
    val criteriaMet: Boolean,
    val terminationReason: String?,
    val rewardDelivered: Boolean,
    val metrics: Map<String, Float>
)
