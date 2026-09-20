package com.flylab.domain.brain

import com.flylab.domain.EvidenceLevel

/**
 * Top-level brain model representing a complete Drosophila melanogaster brain.
 *
 * Reference: FlyWire whole-brain connectome (adult female)
 * Source: https://flywire.ai/
 */
data class Brain(
    val id: String,
    val name: String,
    val species: String = "Drosophila melanogaster",
    val sex: Sex,
    val developmentalStage: String = "adult",
    val regions: List<BrainRegion>,
    val metadata: BrainMetadata
)

enum class Sex {
    FEMALE,
    MALE,
    UNKNOWN
}

data class BrainMetadata(
    val version: String,
    val source: String,
    val evidenceLevel: EvidenceLevel,
    val description: String,
    val license: String? = null,
    val referenceUrl: String? = null,
    val notes: String? = null
)
