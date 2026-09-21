package com.flylab.domain.model

enum class OrganSystemType(val displayName: String) {
    NERVOUS("Nervous System"),
    DIGESTIVE("Digestive System"),
    RESPIRATORY("Tracheal System"),
    CIRCULATORY("Circulatory System"),
    REPRODUCTIVE("Reproductive System"),
    MUSCULAR("Muscular System")
}

data class Organ(
    val id: String,
    val name: String,
    val systemType: OrganSystemType,
    val anatomicalRegion: AnatomicalRegion,
    val physiologicalState: PhysiologicalState,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

data class PhysiologicalState(
    val metabolicLoad: Float = 0.0f,
    val health: Float = 1.0f,
    val capacity: Float = 1.0f
)
