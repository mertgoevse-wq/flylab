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
    val activeBehavior: BehaviorType,
    val seed: Long,
    override val evidence: ScientificEvidence = ScientificEvidence.SIMULATED_RUNTIME_OVERLAY
) : ProvenanceTagged
