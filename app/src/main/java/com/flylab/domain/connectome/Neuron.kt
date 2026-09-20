package com.flylab.domain.connectome

import com.flylab.domain.EvidenceLevel
import com.flylab.domain.brain.Coordinates3D

/**
 * Individual neuron in the connectome.
 */
data class Neuron(
    val id: String,
    val name: String? = null,
    val type: String? = null,
    val regionId: String,
    val coordinates: Coordinates3D,
    val evidenceLevel: EvidenceLevel,
    val source: String,

    // Morphology (optional)
    val somaRadius: Float? = null,
    val arborizationVolume: Float? = null,

    // Classification
    val neurotransmitter: String? = null,
    val isInterneuron: Boolean = false,
    val isProjectionNeuron: Boolean = false
)
