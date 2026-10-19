package com.flylab.sim

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.BehavioralState
import com.flylab.domain.model.BrainRegionState
import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.model.SensoryInput
import com.flylab.domain.model.SynapseSimulationOverlay
import com.flylab.domain.neural.RegionalActivity

/**
 * Immutable snapshot of the complete simulation state at a specific discrete step.
 */
data class SimulationSnapshot(
    val step: Long,
    val timeSeconds: Float,
    val fly: Fly,
    val environment: Environment,
    val sensoryInput: SensoryInput,
    val motorCommand: MotorCommand,
    val firingRates: Map<String, Float>,
    val regionStates: Map<NeuropilId, BrainRegionState>,
    val synapticOverlays: Map<String, SynapseSimulationOverlay>,
    val neuronPerturbations: Map<String, Float> = emptyMap(),
    val regionPerturbations: Map<NeuropilId, Float> = emptyMap(),
    val activeBehavior: BehaviorType,
    val    seed: Long,
    override val evidence: ScientificEvidence = ScientificEvidence.SIMULATED_RUNTIME_OVERLAY
) : ProvenanceTagged

/**
 * Projects the canonical snapshot's per-neuropil states onto the brain viewer's
 * regional activity map. Keys are [NeuropilId] names.
 *
 * Honesty note: the reference brain's region ids do not all match these names 1:1,
 * so overlay coverage is partial until the id mapping table lands (Phase D).
 * Values are honest model outputs (meanActivation), not measured spikes.
 */
fun SimulationSnapshot.toRegionalActivity(): RegionalActivity =
    RegionalActivity(
        timestamp = (timeSeconds * 1000).toLong(),
        regionActivities = regionStates.mapKeys { (neuropil, _) -> neuropil.name }
            .mapValues { (_, state) -> state.meanActivation.coerceIn(0f, 1f) }
    )

/**
 * EEG-ähnliches Populationssignal: Mittelwert der regionalen meanActivation pro Snapshot.
 *
 * Ausdrücklich KEIN echtes EEG (keine Elektroden, kein Volumenleiter-Modell) — eine
 * visualisierte Aggregationskurve der modellierten Feuerraten, synchron zum selben
 * Snapshot-Verlauf wie Fliege und Gehirn. Evidenz: SIMULATED_RUNTIME_OVERLAY.
 */
fun List<SimulationSnapshot>.populationSignal(): List<Float> =
    map { snapshot ->
        val states = snapshot.regionStates.values
        if (states.isEmpty()) 0f
        else states.sumOf { it.meanActivation.coerceIn(0f, 1f).toDouble() }.toFloat() / states.size
    }
