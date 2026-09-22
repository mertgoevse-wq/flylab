package com.flylab.sim.behavior

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.Fly
import com.flylab.domain.model.SensoryInput
import com.flylab.sim.ConnectomeReference
import com.flylab.sim.MotorMapping
import com.flylab.sim.NeuralDynamics
import com.flylab.sim.PlasticityRule
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.SynapseSimulationOverlay

/**
 * Deterministic, mechanistic behavior model capable of running entirely locally at simulation frequency.
 * Uses rate-based leaky integrate-and-fire population dynamics to map sensation to motor command.
 */
class MechanisticBehaviorRuntime : BehaviorRuntime {
    override val name: String = "Mechanistic Connectome (CPU)"
    override val isReady: Boolean = true

    // Internal state carried across evaluations
    var currentFiringRates: Map<String, Float> = emptyMap()
        private set
    
    var currentSynapticOverlays: Map<String, SynapseSimulationOverlay> = emptyMap()
        private set

    var currentRegionStates: Map<NeuropilId, com.flylab.domain.model.BrainRegionState> = emptyMap()
        private set
        
    var currentBehavior: BehaviorType = BehaviorType.EXPLORING
        private set

    // Experimental optogenetic/pharmacological perturbations per brain region (1.0 = normal, 0.0 = silenced)
    val regionPerturbations = mutableMapOf<NeuropilId, Float>()
    val neuronPerturbations = mutableMapOf<String, Float>()
    
    private var internalStep: Long = 0

    override fun initialize() {
        currentFiringRates = ConnectomeReference.NEURONS.associate { it.id to 0.05f }
        currentSynapticOverlays = emptyMap()
        currentRegionStates = emptyMap()
        regionPerturbations.clear()
        neuronPerturbations.clear()
        internalStep = 0
    }

    override fun evaluate(sensoryInput: SensoryInput, fly: Fly, dtSeconds: Float): BehaviorDecision {
        internalStep++
        val timestampMs = internalStep * (dtSeconds * 1000).toLong()

        // 1. Neural Dynamics: Rate-based integration across connectome subset
        val dynamicsResult = NeuralDynamics.step(
            previousRates = currentFiringRates,
            synapticOverlays = currentSynapticOverlays,
            sensoryInput = sensoryInput,
            regionPerturbations = regionPerturbations,
            neuronPerturbations = neuronPerturbations,
            dtSeconds = dtSeconds
        )
        currentFiringRates = dynamicsResult.firingRates
        currentRegionStates = dynamicsResult.regionStates

        // 2. Neuromodulation & Plasticity Update
        val danPam = currentFiringRates["DAN_PAM"] ?: 0.0f
        val danPpl1 = currentFiringRates["DAN_PPL1"] ?: 0.0f

        currentSynapticOverlays = PlasticityRule.updatePlasticSynapses(
            currentOverlays = currentSynapticOverlays,
            synapticReferences = ConnectomeReference.SYNAPSES,
            firingRates = currentFiringRates,
            danPamRate = danPam,
            danPpl1Rate = danPpl1,
            dtSeconds = dtSeconds,
            currentStep = internalStep,
            timestampMs = timestampMs
        )

        // 3. Update internal neuromodulator concentrations in fly model (handled mostly by caller or here)
        // Motor Mapping
        val noise = 0.0f // We remove random object, caller can supply if needed, or we keep it deterministic
        val motorDecision = MotorMapping.map(
            firingRates = currentFiringRates,
            sensoryInput = sensoryInput,
            behavioralState = fly.behavioralState,
            randomNoise = noise
        )
        currentBehavior = motorDecision.behaviorType

        return BehaviorDecision(
            command = motorDecision.command,
            internalStateUpdates = mapOf(
                "danPam" to danPam,
                "danPpl1" to danPpl1
            )
        )
    }
    
    fun overrideState(
        firingRates: Map<String, Float>, 
        synapses: Map<String, SynapseSimulationOverlay>,
        regionPerturbs: Map<NeuropilId, Float>,
        neuronPerturbs: Map<String, Float>,
        behavior: BehaviorType
    ) {
        currentFiringRates = firingRates
        currentSynapticOverlays = synapses
        regionPerturbations.clear()
        regionPerturbations.putAll(regionPerturbs)
        neuronPerturbations.clear()
        neuronPerturbations.putAll(neuronPerturbs)
        currentBehavior = behavior
    }
}
