package com.flylab.sim

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.BrainRegionState
import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.SynapseSimulationOverlay
import java.util.Random

/**
 * Deterministic, frame-rate independent simulation engine for FlyLab.
 *
 * ARCHITECTURAL INTEGRITY (CLAUDE.md & SIMULATION_MODEL.md):
 * - Decoupled completely from rendering and Android UI contexts.
 * - Deterministic: Same initial state + same seed = identical trajectory.
 * - Full time-travel capability (snapshots recorded for replay, scrub, branch).
 */
class SimulationEngine(
    val initialFly: Fly = Fly(),
    val initialEnvironment: Environment = Environment(),
    val seed: Long = 42L,
    val maxHistoryCapacity: Int = 2000
) {
    private var random = Random(seed)

    var currentStep: Long = 0L
        private set

    var currentTimeSeconds: Float = 0.0f
        private set

    var currentFly: Fly = initialFly
        private set

    var currentEnvironment: Environment = initialEnvironment
        private set

    var currentFiringRates: Map<String, Float> = ConnectomeReference.NEURONS.associate { it.id to 0.05f }
        private set

    var currentRegionStates: Map<NeuropilId, BrainRegionState> = emptyMap()
        private set

    var currentSynapticOverlays: Map<String, SynapseSimulationOverlay> = emptyMap()
        private set

    var currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)
        private set

    var currentMotorCommand = MotorCommand.IDLE
        private set

    var currentBehavior: BehaviorType = BehaviorType.EXPLORING
        private set

    // Experimental optogenetic/pharmacological perturbations per brain region (1.0 = normal, 0.0 = silenced)
    val regionPerturbations = mutableMapOf<NeuropilId, Float>()
    val neuronPerturbations = mutableMapOf<String, Float>()

    // Ring-buffer/List of historical snapshots for deterministic time-travel
    private val _history = mutableListOf<SimulationSnapshot>()
    val history: List<SimulationSnapshot> get() = _history

    init {
        recordSnapshot()
    }

    /**
     * Advances the simulation by dtSeconds.
     */
    fun step(dtSeconds: Float = 0.05f): SimulationSnapshot {
        currentStep++
        currentTimeSeconds += dtSeconds

        // 1. Sensory Transduction: Sample environment at antennae and proboscis
        currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)

        // 2. Neural Dynamics: Rate-based integration across connectome subset
        val dynamicsResult = NeuralDynamics.step(
            previousRates = currentFiringRates,
            synapticOverlays = currentSynapticOverlays,
            sensoryInput = currentSensoryInput,
            regionPerturbations = regionPerturbations,
            neuronPerturbations = neuronPerturbations,
            dtSeconds = dtSeconds
        )
        currentFiringRates = dynamicsResult.firingRates
        currentRegionStates = dynamicsResult.regionStates

        // 3. Neuromodulation & Plasticity Update
        val danPam = currentFiringRates["DAN_PAM"] ?: 0.0f
        val danPpl1 = currentFiringRates["DAN_PPL1"] ?: 0.0f

        currentSynapticOverlays = PlasticityRule.updatePlasticSynapses(
            currentOverlays = currentSynapticOverlays,
            synapticReferences = ConnectomeReference.SYNAPSES,
            firingRates = currentFiringRates,
            danPamRate = danPam,
            danPpl1Rate = danPpl1,
            dtSeconds = dtSeconds,
            currentStep = currentStep,
            timestampMs = (currentTimeSeconds * 1000).toLong()
        )

        // Update fly neuromodulator concentrations
        val updatedNeuromodulators = currentFly.neuromodulators.mapValues { (type, state) ->
            val stepState = state.stepDecay(dtSeconds)
            when (type) {
                NeuromodulatorType.OCTOPAMINE -> if (currentSensoryInput.meanOdorConcentration > 0.1f) stepState.inject(0.1f) else stepState
                NeuromodulatorType.DOPAMINE -> if (danPam > 0.1f || danPpl1 > 0.1f) stepState.inject(0.15f) else stepState
                else -> stepState
            }
        }

        // 4. Motor Mapping
        val noise = (random.nextFloat() - 0.5f) * 0.4f
        val motorDecision = MotorMapping.map(
            firingRates = currentFiringRates,
            sensoryInput = currentSensoryInput,
            behavioralState = currentFly.behavioralState,
            randomNoise = noise
        )
        currentMotorCommand = motorDecision.command
        currentBehavior = motorDecision.behaviorType

        // 5. Kinematics & Spatial Position Update
        val updatedBehavioralState = currentFly.behavioralState
            .copy(activeBehavior = currentBehavior)
            .updateSpatial(
                command = currentMotorCommand,
                dtSeconds = dtSeconds,
                arenaRadiusMm = currentEnvironment.arenaRadiusMm
            )

        currentFly = currentFly.copy(
            behavioralState = updatedBehavioralState,
            neuromodulators = updatedNeuromodulators
        )

        val snapshot = recordSnapshot()
        return snapshot
    }

    /**
     * Rewinds or seeks to an exact previous step in history.
     */
    fun seekToStep(targetStep: Long): Boolean {
        val snapshot = _history.find { it.step == targetStep } ?: return false
        restoreSnapshot(snapshot)
        return true
    }

    /**
     * Restores complete internal state from a snapshot.
     */
    fun restoreSnapshot(snapshot: SimulationSnapshot) {
        currentStep = snapshot.step
        currentTimeSeconds = snapshot.timeSeconds
        currentFly = snapshot.fly
        currentEnvironment = snapshot.environment
        currentSensoryInput = snapshot.sensoryInput
        currentMotorCommand = snapshot.motorCommand
        currentFiringRates = snapshot.firingRates
        currentRegionStates = snapshot.regionStates
        currentSynapticOverlays = snapshot.synapticOverlays
        neuronPerturbations.clear()
        neuronPerturbations.putAll(snapshot.neuronPerturbations)
        currentBehavior = snapshot.activeBehavior

        // Re-align PRNG seed deterministically
        random = Random(snapshot.seed + snapshot.step * 31L)
    }

    /**
     * Resets the simulation to the initial starting condition.
     */
    fun reset() {
        random = Random(seed)
        currentStep = 0L
        currentTimeSeconds = 0.0f
        currentFly = initialFly
        currentEnvironment = initialEnvironment
        currentFiringRates = ConnectomeReference.NEURONS.associate { it.id to 0.05f }
        currentRegionStates = emptyMap()
        currentSynapticOverlays = emptyMap()
        currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)
        currentMotorCommand = MotorCommand.IDLE
        currentBehavior = BehaviorType.EXPLORING
        regionPerturbations.clear()
        neuronPerturbations.clear()
        _history.clear()
        recordSnapshot()
    }

    /**
     * Perturbs (silences or excites) a specific brain region.
     */
    
    fun setNeuronPerturbation(neuronId: String, factor: Float) {
        if (factor == 1.0f) {
            neuronPerturbations.remove(neuronId)
        } else {
            neuronPerturbations[neuronId] = factor.coerceIn(0.0f, 3.0f)
        }
    }

    fun setRegionPerturbation(region: NeuropilId, factor: Float) {
        if (factor == 1.0f) {
            regionPerturbations.remove(region)
        } else {
            regionPerturbations[region] = factor.coerceIn(0.0f, 3.0f)
        }
    }

    private fun recordSnapshot(): SimulationSnapshot {
        val snapshot = SimulationSnapshot(
            step = currentStep,
            timeSeconds = currentTimeSeconds,
            fly = currentFly,
            environment = currentEnvironment,
            sensoryInput = currentSensoryInput,
            motorCommand = currentMotorCommand,
            firingRates = currentFiringRates,
            regionStates = currentRegionStates,
            synapticOverlays = currentSynapticOverlays,
            neuronPerturbations = neuronPerturbations.toMap(),
            activeBehavior = currentBehavior,
            seed = seed
        )

        if (_history.size >= maxHistoryCapacity) {
            _history.removeAt(0)
        }
        _history.add(snapshot)
        return snapshot
    }
}
