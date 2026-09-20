package com.flylab.domain.connectome

import com.flylab.domain.EvidenceLevel
import com.flylab.domain.brain.Coordinates3D

/**
 * Synaptic connection between neurons.
 */
data class Synapse(
    val id: String,
    val presynapticNeuronId: String,
    val postsynapticNeuronId: String,
    val coordinates: Coordinates3D,
    val evidenceLevel: EvidenceLevel,
    val source: String,

    // Baseline reference data (immutable)
    val baselineWeight: Float,

    // Simulation state (mutable via plasticity)
    val currentWeight: Float = baselineWeight,

    // Synaptic properties
    val type: SynapseType = SynapseType.CHEMICAL,
    val delay: Float = 1f // milliseconds
)

enum class SynapseType {
    CHEMICAL,
    ELECTRICAL,
    UNKNOWN
}

/**
 * Connection represents the complete connectivity information between two neurons,
 * potentially comprising multiple physical synapses.
 */
data class Connection(
    val sourceNeuronId: String,
    val targetNeuronId: String,
    val synapses: List<Synapse>,
    val totalWeight: Float,
    val evidenceLevel: EvidenceLevel
) {
    val synapseCount: Int get() = synapses.size
}
