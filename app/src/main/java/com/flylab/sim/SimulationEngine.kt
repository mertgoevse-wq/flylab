package com.flylab.sim

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.BrainRegionState
import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.SynapseSimulationOverlay
import com.flylab.sim.backend.SimulationComputeBackend
import com.flylab.sim.backend.BackendType
import com.flylab.sim.behavior.BehaviorRuntime
import com.flylab.sim.behavior.MechanisticBehaviorRuntime
import com.flylab.sim.diagnostics.PerformanceDiagnostics
import java.util.Random

/**
 * Deterministic, frame-rate independent simulation engine for FlyLab.
 *
 * ARCHITECTURAL INTEGRITY (CLAUDE.md & SIMULATION_MODEL.md):
 * - Decoupled completely from rendering and Android UI contexts.
 * - Uses BehaviorRuntime abstraction.
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
        
    var currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)
        private set

    var currentMotorCommand = MotorCommand.IDLE
        private set
        
    val diagnostics = PerformanceDiagnostics()

    // Default to the mechanistic behavior runtime
    private val behaviorRuntime = MechanisticBehaviorRuntime()

    // For backwards compatibility and UI bindings
    val currentFiringRates: Map<String, Float> get() = behaviorRuntime.currentFiringRates
    val currentRegionStates: Map<NeuropilId, BrainRegionState> get() = behaviorRuntime.currentRegionStates
    val currentSynapticOverlays: Map<String, SynapseSimulationOverlay> get() = behaviorRuntime.currentSynapticOverlays
    val currentBehavior: BehaviorType get() = behaviorRuntime.currentBehavior
    val regionPerturbations get() = behaviorRuntime.regionPerturbations
    val neuronPerturbations get() = behaviorRuntime.neuronPerturbations

    private val _history = mutableListOf<SimulationSnapshot>()
    val history: List<SimulationSnapshot> get() = _history

    init {
        behaviorRuntime.initialize()
        recordSnapshot()
    }

    fun step(dtSeconds: Float = 0.05f): SimulationSnapshot {
        diagnostics.startTick()
        currentStep++
        currentTimeSeconds += dtSeconds

        // 1. Sensory
        currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)

        // 2. Behavior Runtime Inference
        diagnostics.startInference()
        val decision = behaviorRuntime.evaluate(currentSensoryInput, currentFly, dtSeconds)
        diagnostics.endInference()
        
        currentMotorCommand = decision.command

        // 3. Update fly states (Neuromodulators, Kinematics)
        val danPam = decision.internalStateUpdates["danPam"] as? Float ?: 0f
        val danPpl1 = decision.internalStateUpdates["danPpl1"] as? Float ?: 0f
        
        val updatedNeuromodulators = currentFly.neuromodulators.mapValues { (type, state) ->
            val stepState = state.stepDecay(dtSeconds)
            when (type) {
                NeuromodulatorType.OCTOPAMINE -> if (currentSensoryInput.meanOdorConcentration > 0.1f) stepState.inject(0.1f) else stepState
                NeuromodulatorType.DOPAMINE -> if (danPam > 0.1f || danPpl1 > 0.1f) stepState.inject(0.15f) else stepState
                else -> stepState
            }
        }

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
        diagnostics.endTick()
        return snapshot
    }

    fun seekToStep(targetStep: Long): Boolean {
        val snapshot = _history.find { it.step == targetStep } ?: return false
        restoreSnapshot(snapshot)
        return true
    }

    fun restoreSnapshot(snapshot: SimulationSnapshot) {
        currentStep = snapshot.step
        currentTimeSeconds = snapshot.timeSeconds
        currentFly = snapshot.fly
        currentEnvironment = snapshot.environment
        currentSensoryInput = snapshot.sensoryInput
        currentMotorCommand = snapshot.motorCommand
        
        behaviorRuntime.overrideState(
            firingRates = snapshot.firingRates,
            synapses = snapshot.synapticOverlays,
            regionPerturbs = snapshot.regionPerturbations,
            neuronPerturbs = snapshot.neuronPerturbations,
            behavior = snapshot.activeBehavior
        )

        random = Random(snapshot.seed + snapshot.step * 31L)
    }

    fun reset() {
        random = Random(seed)
        currentStep = 0L
        currentTimeSeconds = 0.0f
        currentFly = initialFly
        currentEnvironment = initialEnvironment
        currentSensoryInput = SensoryTransduction.transduce(currentFly, currentEnvironment)
        currentMotorCommand = MotorCommand.IDLE
        behaviorRuntime.initialize()
        _history.clear()
        recordSnapshot()
    }

    fun setNeuronPerturbation(neuronId: String, factor: Float) {
        if (factor == 1.0f) {
            behaviorRuntime.neuronPerturbations.remove(neuronId)
        } else {
            behaviorRuntime.neuronPerturbations[neuronId] = factor.coerceIn(0.0f, 3.0f)
        }
    }

    fun setRegionPerturbation(region: NeuropilId, factor: Float) {
        if (factor == 1.0f) {
            behaviorRuntime.regionPerturbations.remove(region)
        } else {
            behaviorRuntime.regionPerturbations[region] = factor.coerceIn(0.0f, 3.0f)
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
            firingRates = behaviorRuntime.currentFiringRates,
            regionStates = behaviorRuntime.currentRegionStates,
            synapticOverlays = behaviorRuntime.currentSynapticOverlays,
            neuronPerturbations = behaviorRuntime.neuronPerturbations.toMap(),
            activeBehavior = behaviorRuntime.currentBehavior,
            seed = seed,
            regionPerturbations = behaviorRuntime.regionPerturbations.toMap()
        )

        if (_history.size >= maxHistoryCapacity) {
            _history.removeAt(0)
        }
        _history.add(snapshot)
        return snapshot
    }
}
