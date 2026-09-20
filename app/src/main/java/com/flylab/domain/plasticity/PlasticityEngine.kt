package com.flylab.domain.plasticity

import com.flylab.domain.EvidenceLevel

/**
 * Plasticity engine for updating synaptic weights based on learning rules.
 *
 * SCIENTIFIC STATUS: MODELED
 *
 * This implements computational learning rules, not directly measured
 * biological plasticity mechanisms.
 */
class PlasticityEngine(
    val rule: PlasticityRule
) {
    fun applyPlasticity(
        event: LearningEvent,
        currentWeight: Float
    ): PlasticityUpdate {
        val deltaWeight = rule.computeWeightChange(
            presynapticActivity = event.presynapticActivity,
            postsynapticActivity = event.postsynapticActivity,
            rewardSignal = event.rewardSignal,
            currentWeight = currentWeight
        )

        val newWeight = (currentWeight + deltaWeight).coerceIn(0f, 10f)

        return PlasticityUpdate(
            timestamp = event.timestamp,
            synapseId = event.synapseId,
            oldWeight = currentWeight,
            newWeight = newWeight,
            deltaWeight = deltaWeight,
            cause = event.cause,
            evidenceLevel = EvidenceLevel.MODELED
        )
    }
}

/**
 * Abstract learning rule interface.
 */
interface PlasticityRule {
    val name: String
    val description: String
    val evidenceLevel: EvidenceLevel

    fun computeWeightChange(
        presynapticActivity: Float,
        postsynapticActivity: Float,
        rewardSignal: Float,
        currentWeight: Float
    ): Float
}

/**
 * Simple reward-modulated Hebbian plasticity.
 *
 * Δw = learningRate × preActivity × postActivity × rewardSignal
 *
 * SCIENTIFIC STATUS: MODELED
 */
class RewardModulatedHebbian(
    val learningRate: Float = 0.01f
) : PlasticityRule {
    override val name = "Reward-Modulated Hebbian"
    override val description = "Synaptic strengthening proportional to pre/post activity and reward"
    override val evidenceLevel = EvidenceLevel.MODELED

    override fun computeWeightChange(
        presynapticActivity: Float,
        postsynapticActivity: Float,
        rewardSignal: Float,
        currentWeight: Float
    ): Float {
        return learningRate * presynapticActivity * postsynapticActivity * rewardSignal
    }
}

data class LearningEvent(
    val timestamp: Long,
    val synapseId: String,
    val presynapticActivity: Float,
    val postsynapticActivity: Float,
    val rewardSignal: Float,
    val cause: String
)

data class PlasticityUpdate(
    val timestamp: Long,
    val synapseId: String,
    val oldWeight: Float,
    val newWeight: Float,
    val deltaWeight: Float,
    val cause: String,
    val evidenceLevel: EvidenceLevel
) {
    val percentChange: Float get() = if (oldWeight > 0f) {
        (deltaWeight / oldWeight) * 100f
    } else 0f
}
