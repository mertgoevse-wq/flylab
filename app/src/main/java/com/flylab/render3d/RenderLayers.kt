package com.flylab.render3d

/**
 * Visual layers that can be toggled on/off in the 3D scientific viewport.
 */
data class RenderLayers(
    val showExoskeleton: Boolean = true,
    val showInternalOrgans: Boolean = false,
    val showBrainRegions: Boolean = true,
    val showConnectomeGraph: Boolean = true,
    val showNeuralActivityPulse: Boolean = true,
    val showSensoryPlume: Boolean = true,
    val levelOfDetail: LevelOfDetail = LevelOfDetail.LOD1
)

/**
 * Performance Level of Detail (LOD) for responsive rendering on mid-range hardware.
 * Meets the Samsung Galaxy A56 and OnePlus 6T performance mandate (CLAUDE.md).
 */
enum class LevelOfDetail(
    val description: String,
    val maxNeuronsToRender: Int,
    val segmentSubdivisions: Int
) {
    LOD0("Hohe Detailstufe (Fasern & Einzelneurone)", maxNeuronsToRender = 150, segmentSubdivisions = 16),
    LOD1("Standard (Gehirnbereiche & Leitbahnen)", maxNeuronsToRender = 50, segmentSubdivisions = 10),
    LOD2("Sparmodus (Nur Hauptregionen & Umrisse)", maxNeuronsToRender = 20, segmentSubdivisions = 6)
}
