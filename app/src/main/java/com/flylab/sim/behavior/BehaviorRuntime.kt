package com.flylab.sim.behavior

import com.flylab.domain.model.Fly
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.SensoryInput

/**
 * Abstract controller capable of running at simulation frequency.
 * Conceptual mapping: sensors + biological state + internal state + memory -> action -> motor
 */
interface BehaviorRuntime {
    val name: String
    val isReady: Boolean

    fun initialize()

    /**
     * Evaluates the current state and returns an action decision.
     */
    fun evaluate(
        sensoryInput: SensoryInput,
        fly: Fly,
        dtSeconds: Float
    ): BehaviorDecision
}

data class BehaviorDecision(
    val command: MotorCommand,
    val internalStateUpdates: Map<String, Any> = emptyMap()
)
