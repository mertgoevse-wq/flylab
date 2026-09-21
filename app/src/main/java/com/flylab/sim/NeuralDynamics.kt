package com.flylab.sim

import com.flylab.domain.model.BrainRegionState
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.SensoryInput
import com.flylab.domain.model.SynapseSimulationOverlay

/**
 * Leaky integrate-and-fire rate-based population dynamics.
 */
object NeuralDynamics {

    const val TAU_MEMBRANE_MS = 25.0f // Membrane time constant
    const val ACTIVATION_GAIN = 4.0f
    const val FIRING_THRESHOLD = 0.5f

    data class DynamicsStepResult(
        val firingRates: Map<String, Float>,
        val regionStates: Map<NeuropilId, BrainRegionState>
    )

    /**
     * Executes one time-step of the rate-based neural network.
     */
    fun step(
        previousRates: Map<String, Float>,
        synapticOverlays: Map<String, SynapseSimulationOverlay>,
        sensoryInput: SensoryInput,
        regionPerturbations: Map<NeuropilId, Float> = emptyMap(),
        neuronPerturbations: Map<String, Float> = emptyMap(),
        dtSeconds: Float
    ): DynamicsStepResult {
        val dtMs = dtSeconds * 1000.0f
        val currentRates = previousRates.toMutableMap()
        val totalCurrents = mutableMapOf<String, Float>()

        // 1. External sensory current injection
        // Olfactory receptor neurons driven by odor
        val vinegarFactor = if (sensoryInput.activeOdor == com.flylab.domain.model.OdorType.APPLE_CIDER_VINEGAR) 1.0f else 0.2f
        val moldFactor = if (sensoryInput.activeOdor == com.flylab.domain.model.OdorType.GEOSMIN) 1.0f else 0.1f

        totalCurrents["ORN_DM1_L"] = sensoryInput.odorLeftAntenna * 2.5f * vinegarFactor
        totalCurrents["ORN_DM1_R"] = sensoryInput.odorRightAntenna * 2.5f * vinegarFactor
        totalCurrents["ORN_DA2_L"] = sensoryInput.odorLeftAntenna * 3.0f * moldFactor
        totalCurrents["ORN_DA2_R"] = sensoryInput.odorRightAntenna * 3.0f * moldFactor

        // Sucrose drives PAM reward dopaminergic cluster
        totalCurrents["DAN_PAM"] = sensoryInput.sucroseContact * 3.5f

        // Heat stress or noxious odor drives PPL1 aversive dopaminergic cluster
        totalCurrents["DAN_PPL1"] = (sensoryInput.thermalStress * 2.0f) + (sensoryInput.meanOdorConcentration * moldFactor * 1.5f)

        // 2. Synaptic transmission currents
        for (syn in ConnectomeReference.SYNAPSES) {
            val preRate = currentRates[syn.preNeuronId] ?: 0.0f
            if (preRate <= 0.001f) continue

            val effWeight = synapticOverlays[syn.synapseId]?.currentWeight ?: syn.baselineWeight
            val sign = if (syn.neurotransmitter == NeuromodulatorType.GABA) -1.0f else 1.0f

            val synCurrent = sign * effWeight * preRate
            totalCurrents[syn.postNeuronId] = (totalCurrents[syn.postNeuronId] ?: 0.0f) + synCurrent
        }

        // 3. Integration & Sigmoid rate update: dr/dt = (-r + f(I)) / tau
        val updatedRates = mutableMapOf<String, Float>()
        for (neuron in ConnectomeReference.NEURONS) {
            val id = neuron.id
            val oldRate = currentRates[id] ?: 0.0f
            val current = totalCurrents[id] ?: 0.0f

            // Sigmoid activation: f(I) = 1 / (1 + exp(-gain * (I - threshold)))
            val targetRate = (1.0f / (1.0f + Math.exp((-ACTIVATION_GAIN * (current - FIRING_THRESHOLD)).toDouble()))).toFloat()

            // Apply experimental optogenetic / pharmacological perturbation factor if present
            val regionPert = regionPerturbations[neuron.neuropil] ?: 1.0f
            val neuronPert = neuronPerturbations[neuron.id] ?: 1.0f
            val perturbedTarget = (targetRate * regionPert * neuronPert).coerceIn(0.0f, 1.0f)

            val dRate = ((perturbedTarget - oldRate) / TAU_MEMBRANE_MS) * dtMs
            updatedRates[id] = (oldRate + dRate).coerceIn(0.0f, 1.0f)
        }

        // 4. Compute mean brain region states
        val regionStates = NeuropilId.entries.associateWith { neuropil ->
            val neuronsInRegion = ConnectomeReference.NEURONS.filter { it.neuropil == neuropil }
            val rates = neuronsInRegion.map { updatedRates[it.id] ?: 0.0f }
            val meanAct = if (rates.isNotEmpty()) rates.average().toFloat() else 0.0f
            val activeFraction = if (rates.isNotEmpty()) rates.count { it > 0.15f }.toFloat() / rates.size else 0.0f

            BrainRegionState(
                neuropil = neuropil,
                meanActivation = meanAct.coerceIn(0.0f, 1.0f),
                activeNeuronFraction = activeFraction.coerceIn(0.0f, 1.0f),
                isPerturbed = (regionPerturbations[neuropil] != null && regionPerturbations[neuropil] != 1.0f),
                perturbationFactor = regionPerturbations[neuropil] ?: 1.0f
            )
        }

        return DynamicsStepResult(
            firingRates = updatedRates,
            regionStates = regionStates
        )
    }
}
