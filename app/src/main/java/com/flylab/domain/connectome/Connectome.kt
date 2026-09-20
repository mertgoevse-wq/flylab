package com.flylab.domain.connectome

import com.flylab.domain.EvidenceLevel

/**
 * Complete connectome dataset.
 *
 * Note: The FlyWire connectome contains ~139,000 neurons.
 * This model supports progressive loading and level-of-detail rendering.
 */
data class Connectome(
    val id: String,
    val version: String,
    val brainId: String,
    val metadata: ConnectomeMetadata,

    // Neurons and connections (loaded progressively)
    val neurons: Map<String, Neuron> = emptyMap(),
    val synapses: Map<String, Synapse> = emptyMap(),
    val connections: Map<Pair<String, String>, Connection> = emptyMap(),

    // Loading state
    val isFullyLoaded: Boolean = false,
    val loadedRegionIds: Set<String> = emptySet()
)

data class ConnectomeMetadata(
    val source: String,
    val evidenceLevel: EvidenceLevel,
    val totalNeuronCount: Int,
    val totalSynapseCount: Long,
    val description: String,
    val referenceUrl: String? = null,
    val license: String? = null
)
