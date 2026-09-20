package com.flylab.domain.brain

import com.flylab.domain.EvidenceLevel

/**
 * A major anatomical brain region (e.g., mushroom body, antennal lobe, central complex).
 */
data class BrainRegion(
    val id: String,
    val name: String,
    val abbreviation: String,
    val description: String,
    val function: String,
    val evidenceLevel: EvidenceLevel,
    val source: String,
    val coordinates: Coordinates3D,
    val boundingBox: BoundingBox,
    val subregions: List<Subregion> = emptyList(),
    val knownInputs: List<String> = emptyList(),
    val knownOutputs: List<String> = emptyList(),
    val isVisible: Boolean = true,
    val isHighlighted: Boolean = false
)

data class Subregion(
    val id: String,
    val name: String,
    val description: String,
    val cellTypes: List<CellType> = emptyList(),
    val coordinates: Coordinates3D,
    val evidenceLevel: EvidenceLevel
)

data class CellType(
    val id: String,
    val name: String,
    val description: String,
    val count: Int? = null,
    val evidenceLevel: EvidenceLevel
)

data class BoundingBox(
    val min: Coordinates3D,
    val max: Coordinates3D
) {
    fun center(): Coordinates3D = Coordinates3D(
        (min.x + max.x) / 2f,
        (min.y + max.y) / 2f,
        (min.z + max.z) / 2f
    )

    fun size(): Coordinates3D = Coordinates3D(
        max.x - min.x,
        max.y - min.y,
        max.z - min.z
    )
}
