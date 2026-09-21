package com.flylab.render3d

import com.flylab.domain.model.NeuropilId

/**
 * A 3D line segment with color/semantic tag.
 */
data class Line3D(
    val start: Vector3D,
    val end: Vector3D,
    val colorHex: Long,
    val strokeWidthDp: Float = 1.5f,
    val tag: String = ""
)

/**
 * A 3D polygonal quad/triangle face for anatomical surface rendering.
 */
data class Polygon3D(
    val vertices: List<Vector3D>,
    val baseColorHex: Long,
    val alpha: Float = 1.0f,
    val normal: Vector3D = Vector3D.UP
)

/**
 * 3D visual representation of a brain neuropil.
 */
data class NeuropilMarker3D(
    val neuropil: NeuropilId,
    val center: Vector3D,
    val radius: Float,
    val baseColorHex: Long
)

/**
 * 3D anatomical mesh definitions for Drosophila melanogaster.
 * Scale: 1 unit ~ 0.5 mm in Drosophila coordinates.
 */
object FlyMeshGeometry {

    // Scientific coloration: Ochre/Amber body, Brick-red eyes, Translucent wings, Emerald/Mint brain
    const val COLOR_BODY_AMBER = 0xFFD97706L
    const val COLOR_BODY_DARK = 0xFF78350FL
    const val COLOR_EYE_RED = 0xFFDC2626L
    const val COLOR_WING_TRANSLUCENT = 0x44E2E8F0L
    const val COLOR_WING_VEIN = 0xFF94A3B8L
    const val COLOR_LEG = 0xFFB45309L
    const val COLOR_ANTENNA = 0xFF92400EL

    // Brain Neuropil Colors (scientific visualization palette)
    const val COLOR_AL = 0xFF10B981L   // Emerald
    const val COLOR_MB = 0xFF3B82F6L   // Sapphire Blue
    const val COLOR_CX = 0xFFF59E0BL   // Amber
    const val COLOR_LH = 0xFF8B5CF6L   // Purple
    const val COLOR_OL = 0xFF06B6D4L   // Cyan
    const val COLOR_LAL = 0xFFEC4899L  // Rose

    /**
     * Builds the complete set of anatomical line segments and surface polygons.
     */
    fun buildAnatomyGeometry(): Pair<List<Polygon3D>, List<Line3D>> {
        val polygons = mutableListOf<Polygon3D>()
        val lines = mutableListOf<Line3D>()

        // --- 1. HEAD (Centroid at z = 1.6, y = 0.1) ---
        buildEllipsoid(
            center = Vector3D(0f, 0.1f, 1.6f),
            radiusX = 0.55f, radiusY = 0.45f, radiusZ = 0.45f,
            subdivisions = 8,
            colorHex = COLOR_BODY_AMBER,
            polygons = polygons,
            lines = lines
        )

        // Compound Eyes (Left and Right)
        buildEllipsoid(
            center = Vector3D(-0.48f, 0.12f, 1.62f),
            radiusX = 0.28f, radiusY = 0.38f, radiusZ = 0.35f,
            subdivisions = 6,
            colorHex = COLOR_EYE_RED,
            polygons = polygons,
            lines = lines
        )
        buildEllipsoid(
            center = Vector3D(0.48f, 0.12f, 1.62f),
            radiusX = 0.28f, radiusY = 0.38f, radiusZ = 0.35f,
            subdivisions = 6,
            colorHex = COLOR_EYE_RED,
            polygons = polygons,
            lines = lines
        )

        // Antennae and Arista bristles
        lines.add(Line3D(Vector3D(-0.15f, 0.2f, 2.0f), Vector3D(-0.25f, 0.45f, 2.3f), COLOR_ANTENNA, strokeWidthDp = 2.0f))
        lines.add(Line3D(Vector3D(-0.25f, 0.45f, 2.3f), Vector3D(-0.35f, 0.65f, 2.45f), COLOR_ANTENNA, strokeWidthDp = 1.0f))
        lines.add(Line3D(Vector3D(0.15f, 0.2f, 2.0f), Vector3D(0.25f, 0.45f, 2.3f), COLOR_ANTENNA, strokeWidthDp = 2.0f))
        lines.add(Line3D(Vector3D(0.25f, 0.45f, 2.3f), Vector3D(0.35f, 0.65f, 2.45f), COLOR_ANTENNA, strokeWidthDp = 1.0f))

        // Proboscis
        lines.add(Line3D(Vector3D(0f, -0.25f, 1.6f), Vector3D(0f, -0.65f, 1.75f), COLOR_BODY_DARK, strokeWidthDp = 2.5f))

        // --- 2. THORAX (Centroid at z = 0.5, y = 0.2) ---
        buildEllipsoid(
            center = Vector3D(0f, 0.2f, 0.5f),
            radiusX = 0.70f, radiusY = 0.65f, radiusZ = 0.80f,
            subdivisions = 8,
            colorHex = COLOR_BODY_AMBER,
            polygons = polygons,
            lines = lines
        )

        // Scutellum triangle on posterior thorax
        polygons.add(
            Polygon3D(
                vertices = listOf(
                    Vector3D(-0.25f, 0.48f, 0.05f),
                    Vector3D(0.25f, 0.48f, 0.05f),
                    Vector3D(0f, 0.40f, -0.25f)
                ),
                baseColorHex = COLOR_BODY_DARK,
                alpha = 0.9f
            )
        )

        // --- 3. WINGS (Left and Right membranous plates with veins) ---
        buildWing(isRight = false, polygons, lines)
        buildWing(isRight = true, polygons, lines)

        // --- 4. SIX ARTICULATED LEGS ---
        // Prothoracic (Front T1)
        buildLeg(startX = -0.4f, startZ = 1.0f, dirX = -1f, dirZ = 0.6f, lines)
        buildLeg(startX = 0.4f, startZ = 1.0f, dirX = 1f, dirZ = 0.6f, lines)

        // Mesothoracic (Middle T2)
        buildLeg(startX = -0.5f, startZ = 0.5f, dirX = -1.2f, dirZ = -0.1f, lines)
        buildLeg(startX = 0.5f, startZ = 0.5f, dirX = 1.2f, dirZ = -0.1f, lines)

        // Metathoracic (Rear T3)
        buildLeg(startX = -0.4f, startZ = 0.0f, dirX = -1.0f, dirZ = -0.8f, lines)
        buildLeg(startX = 0.4f, startZ = 0.0f, dirX = 1.0f, dirZ = -0.8f, lines)

        // --- 5. ABDOMEN (Segmented rings A1-A6 with dark stripes) ---
        for (i in 0..5) {
            val zPos = -0.45f - i * 0.35f
            val radScale = 1.0f - (i * 0.12f)
            val isDarkTergite = (i >= 3)
            val segmentColor = if (isDarkTergite) COLOR_BODY_DARK else COLOR_BODY_AMBER

            buildEllipsoid(
                center = Vector3D(0f, 0.05f - i * 0.02f, zPos),
                radiusX = 0.62f * radScale,
                radiusY = 0.52f * radScale,
                radiusZ = 0.22f,
                subdivisions = 7,
                colorHex = segmentColor,
                polygons = polygons,
                lines = lines
            )
        }

        return Pair(polygons, lines)
    }

    /**
     * Builds the 3D neuropil markers located within the head.
     */
    fun buildBrainNeuropils(): List<NeuropilMarker3D> {
        val headCenter = Vector3D(0f, 0.1f, 1.6f)

        return listOf(
            NeuropilMarker3D(NeuropilId.ANTENNAL_LOBE, headCenter + Vector3D(-0.18f, -0.15f, 0.28f), 0.16f, COLOR_AL),
            NeuropilMarker3D(NeuropilId.ANTENNAL_LOBE, headCenter + Vector3D(0.18f, -0.15f, 0.28f), 0.16f, COLOR_AL),
            NeuropilMarker3D(NeuropilId.MUSHROOM_BODY, headCenter + Vector3D(-0.20f, 0.12f, 0.12f), 0.22f, COLOR_MB),
            NeuropilMarker3D(NeuropilId.MUSHROOM_BODY, headCenter + Vector3D(0.20f, 0.12f, 0.12f), 0.22f, COLOR_MB),
            NeuropilMarker3D(NeuropilId.CENTRAL_COMPLEX, headCenter + Vector3D(0f, 0.05f, 0.08f), 0.18f, COLOR_CX),
            NeuropilMarker3D(NeuropilId.LATERAL_HORN, headCenter + Vector3D(-0.32f, 0.08f, 0.05f), 0.18f, COLOR_LH),
            NeuropilMarker3D(NeuropilId.LATERAL_HORN, headCenter + Vector3D(0.32f, 0.08f, 0.05f), 0.18f, COLOR_LH),
            NeuropilMarker3D(NeuropilId.OPTIC_LOBE, headCenter + Vector3D(-0.42f, -0.02f, 0.15f), 0.26f, COLOR_OL),
            NeuropilMarker3D(NeuropilId.OPTIC_LOBE, headCenter + Vector3D(0.42f, -0.02f, 0.15f), 0.26f, COLOR_OL),
            NeuropilMarker3D(NeuropilId.LATERAL_ACCESSORY_LOBE, headCenter + Vector3D(-0.14f, -0.06f, 0.02f), 0.12f, COLOR_LAL),
            NeuropilMarker3D(NeuropilId.LATERAL_ACCESSORY_LOBE, headCenter + Vector3D(0.14f, -0.06f, 0.02f), 0.12f, COLOR_LAL)
        )
    }

    private fun buildWing(isRight: Boolean, polygons: MutableList<Polygon3D>, lines: MutableList<Line3D>) {
        val sign = if (isRight) 1.0f else -1.0f
        val wingRoot = Vector3D(sign * 0.45f, 0.45f, 0.4f)
        val wingTip = Vector3D(sign * 1.8f, 0.52f, -1.8f)
        val wingLeadingMid = Vector3D(sign * 1.5f, 0.50f, -0.4f)
        val wingTrailingMid = Vector3D(sign * 0.9f, 0.48f, -1.4f)

        // Wing membrane polygons
        polygons.add(
            Polygon3D(
                vertices = listOf(wingRoot, wingLeadingMid, wingTip, wingTrailingMid),
                baseColorHex = COLOR_WING_TRANSLUCENT,
                alpha = 0.45f
            )
        )

        // Wing veins (Costa, Radial L1-L5)
        lines.add(Line3D(wingRoot, wingLeadingMid, COLOR_WING_VEIN, strokeWidthDp = 1.8f))
        lines.add(Line3D(wingLeadingMid, wingTip, COLOR_WING_VEIN, strokeWidthDp = 1.4f))
        lines.add(Line3D(wingRoot, wingTrailingMid, COLOR_WING_VEIN, strokeWidthDp = 1.2f))
        lines.add(Line3D(wingTrailingMid, wingTip, COLOR_WING_VEIN, strokeWidthDp = 1.0f))
        lines.add(Line3D(wingRoot, Vector3D(sign * 1.3f, 0.50f, -1.1f), COLOR_WING_VEIN, strokeWidthDp = 1.0f))
    }

    private fun buildLeg(startX: Float, startZ: Float, dirX: Float, dirZ: Float, lines: MutableList<Line3D>) {
        val coxa = Vector3D(startX, 0.0f, startZ)
        val femur = coxa + Vector3D(dirX * 0.4f, -0.4f, dirZ * 0.3f)
        val tibia = femur + Vector3D(dirX * 0.6f, -0.6f, dirZ * 0.5f)
        val tarsus = tibia + Vector3D(dirX * 0.4f, -0.2f, dirZ * 0.4f)

        lines.add(Line3D(coxa, femur, COLOR_LEG, strokeWidthDp = 2.5f))
        lines.add(Line3D(femur, tibia, COLOR_LEG, strokeWidthDp = 2.0f))
        lines.add(Line3D(tibia, tarsus, COLOR_LEG, strokeWidthDp = 1.5f))
    }

    private fun buildEllipsoid(
        center: Vector3D,
        radiusX: Float,
        radiusY: Float,
        radiusZ: Float,
        subdivisions: Int,
        colorHex: Long,
        polygons: MutableList<Polygon3D>,
        lines: MutableList<Line3D>
    ) {
        val stacks = subdivisions
        val slices = subdivisions

        for (i in 0 until stacks) {
            val phi0 = Math.PI * (-0.5 + i.toDouble() / stacks)
            val phi1 = Math.PI * (-0.5 + (i + 1).toDouble() / stacks)

            val y0 = (radiusY * Math.sin(phi0)).toFloat()
            val r0 = (Math.cos(phi0)).toFloat()

            val y1 = (radiusY * Math.sin(phi1)).toFloat()
            val r1 = (Math.cos(phi1)).toFloat()

            for (j in 0 until slices) {
                val theta0 = 2.0 * Math.PI * j.toDouble() / slices
                val theta1 = 2.0 * Math.PI * (j + 1).toDouble() / slices

                val p00 = center + Vector3D(
                    radiusX * r0 * Math.cos(theta0).toFloat(),
                    y0,
                    radiusZ * r0 * Math.sin(theta0).toFloat()
                )
                val p10 = center + Vector3D(
                    radiusX * r0 * Math.cos(theta1).toFloat(),
                    y0,
                    radiusZ * r0 * Math.sin(theta1).toFloat()
                )
                val p11 = center + Vector3D(
                    radiusX * r1 * Math.cos(theta1).toFloat(),
                    y1,
                    radiusZ * r1 * Math.sin(theta1).toFloat()
                )
                val p01 = center + Vector3D(
                    radiusX * r1 * Math.cos(theta0).toFloat(),
                    y1,
                    radiusZ * r1 * Math.sin(theta0).toFloat()
                )

                polygons.add(
                    Polygon3D(
                        vertices = listOf(p00, p10, p11, p01),
                        baseColorHex = colorHex,
                        alpha = 0.95f
                    )
                )

                // Wireframe silhouette contour
                if (j % 2 == 0) {
                    lines.add(Line3D(p00, p10, COLOR_BODY_DARK, strokeWidthDp = 1.0f))
                }
            }
        }
    }
}
