package com.flylab.sim

import com.flylab.domain.model.SynapseReference
import com.flylab.domain.model.SynapseSimulationOverlay

/**
 * Mushroom body dopamine-dependent associative synaptic plasticity rule.
 *
 * SCIENTIFIC FOUNDATION:
 * Aso et al. (eLife 2014) & Hige et al. (Nature 2015):
 * Kenyon Cell -> MBON synapses undergo dopamine-dependent long-term depression (LTD).
 * - PAM cluster dopamine (appetitive / sucrose) depresses KC -> MBON_AVERSIVE connections.
 *   This net-disinhibits approach behavior.
 * - PPL1 cluster dopamine (aversive / shock / heat) depresses KC -> MBON_APPETITIVE connections.
 *   This net-disinhibits avoidance behavior.
 *
 * Reference baseline weights are NEVER modified.
 */
object PlasticityRule {

    const val LEARNING_RATE = 0.45f
    const val SPONTANEOUS_RECOVERY_RATE_PER_SEC = 0.005f // Slow return toward baseline

    /**
     * Updates plastic synaptic weights based on pre/post activations and dopamine signals.
     */
    fun updatePlasticSynapses(
        currentOverlays: Map<String, SynapseSimulationOverlay>,
        synapticReferences: List<SynapseReference>,
        firingRates: Map<String, Float>,
        danPamRate: Float,
        danPpl1Rate: Float,
        dtSeconds: Float,
        currentStep: Long,
        timestampMs: Long
    ): Map<String, SynapseSimulationOverlay> {
        val updated = currentOverlays.toMutableMap()

        for (syn in synapticReferences) {
            if (!syn.isPlastic) continue

            val overlay = updated[syn.synapseId] ?: SynapseSimulationOverlay(
                synapseId = syn.synapseId,
                baselineWeight = syn.baselineWeight,
                currentWeight = syn.baselineWeight
            )

            val preRate = firingRates[syn.preNeuronId] ?: 0.0f
            val postRate = firingRates[syn.postNeuronId] ?: 0.0f

            var deltaW = 0.0f
            var cause = "SPONTANEOUS_DECAY"

            // Dopamine-gated LTD
            if (syn.postNeuronId == "MBON_AVERSIVE" && danPamRate > 0.05f) {
                // Reward depresses aversive MBON synapse
                val depression = LEARNING_RATE * preRate * postRate * danPamRate * dtSeconds
                deltaW -= depression
                cause = "REWARD_PAM_DEPRESSION"
            } else if (syn.postNeuronId == "MBON_APPETITIVE" && danPpl1Rate > 0.05f) {
                // Punishment depresses appetitive MBON synapse
                val depression = LEARNING_RATE * preRate * postRate * danPpl1Rate * dtSeconds
                deltaW -= depression
                cause = "AVERSION_PPL1_DEPRESSION"
            } else {
                // Slow decay / recovery toward baseline
                val diffFromBaseline = overlay.baselineWeight - overlay.currentWeight
                deltaW += diffFromBaseline * SPONTANEOUS_RECOVERY_RATE_PER_SEC * dtSeconds
            }

            if (Math.abs(deltaW) > 0.00001f) {
                val newWeight = (overlay.currentWeight + deltaW).coerceIn(0.0f, syn.baselineWeight * 2.5f)
                updated[syn.synapseId] = overlay.updateWeight(
                    newWeight = newWeight,
                    step = currentStep,
                    timestampMs = timestampMs,
                    cause = cause
                )
            }
        }

        return updated
    }
}
