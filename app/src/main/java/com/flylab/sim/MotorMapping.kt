package com.flylab.sim

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.BehavioralState
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.SensoryInput

/**
 * Translates neural network activations and physiological needs into physical motor commands.
 */
object MotorMapping {

    const val MAX_WALK_SPEED_MM_S = 22.0f
    const val MAX_TURN_RATE_RAD_S = 4.0f

    data class MotorDecision(
        val command: MotorCommand,
        val behaviorType: BehaviorType
    )

    fun map(
        firingRates: Map<String, Float>,
        sensoryInput: SensoryInput,
        behavioralState: BehavioralState,
        randomNoise: Float = 0.0f
    ): MotorDecision {
        val mbonApp = firingRates["MBON_APPETITIVE"] ?: 0.1f
        val mbonAvo = firingRates["MBON_AVERSIVE"] ?: 0.1f
        val lhonAtt = firingRates["LHON_ATTRACT"] ?: 0.1f
        val lhonAvo = firingRates["LHON_AVOID"] ?: 0.1f

        val motorForward = firingRates["CX_MOTOR_FORWARD"] ?: 0.2f
        val motorL = firingRates["CX_MOTOR_L"] ?: 0.0f
        val motorR = firingRates["CX_MOTOR_R"] ?: 0.0f

        val needs = behavioralState.needs

        // If sucrose is touching the proboscis/tarsi and fly is hungry -> FEEDING
        if (sensoryInput.sucroseContact > 0.1f && needs.hunger > 0.1f) {
            return MotorDecision(
                command = MotorCommand(
                    forwardVelocityMmS = 0.0f,
                    angularVelocityRadS = 0.0f,
                    proboscisExtension = (sensoryInput.sucroseContact * (0.5f + needs.hunger * 0.5f)).coerceIn(0.0f, 1.0f)
                ),
                behaviorType = BehaviorType.FEEDING
            )
        }

        // If highly fatigued and no strong stimuli -> RESTING
        if (needs.fatigue > 0.85f && sensoryInput.meanOdorConcentration < 0.1f) {
            return MotorDecision(
                command = MotorCommand(forwardVelocityMmS = 0.0f, angularVelocityRadS = 0.0f),
                behaviorType = BehaviorType.RESTING
            )
        }

        // Net valence drive
        val netAttraction = (mbonApp - mbonAvo) + (lhonAtt - lhonAvo)

        // Bilateral steering: turn towards the side with higher sensory/motor drive
        val steerBalance = (motorR - motorL) + (sensoryInput.odorGradientBilateral * 1.5f) + randomNoise
        val angularVel = (steerBalance * MAX_TURN_RATE_RAD_S).coerceIn(-MAX_TURN_RATE_RAD_S, MAX_TURN_RATE_RAD_S)

        val speedFactor = if (netAttraction > 0.1f) {
            1.2f // Accelerate towards appetitive odor
        } else if (netAttraction < -0.1f) {
            1.5f // Escape / fast sprint away from toxic odor
        } else {
            0.8f // Spontaneous exploration pace
        }

        val forwardVel = ((motorForward * MAX_WALK_SPEED_MM_S * speedFactor) * (1.0f - needs.fatigue * 0.4f)).coerceIn(0.0f, MAX_WALK_SPEED_MM_S * 1.5f)

        val behavior = when {
            netAttraction < -0.2f -> BehaviorType.AVOIDING
            netAttraction > 0.2f && sensoryInput.meanOdorConcentration > 0.05f -> BehaviorType.APPROACHING
            Math.abs(angularVel) > 2.0f -> BehaviorType.ORIENTING
            forwardVel > 2.0f -> BehaviorType.EXPLORING
            else -> BehaviorType.RESTING
        }

        return MotorDecision(
            command = MotorCommand(
                forwardVelocityMmS = forwardVel,
                angularVelocityRadS = angularVel,
                proboscisExtension = 0.0f
            ),
            behaviorType = behavior
        )
    }
}
