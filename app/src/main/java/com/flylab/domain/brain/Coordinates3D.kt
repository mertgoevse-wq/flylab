package com.flylab.domain.brain

/**
 * 3D spatial coordinates in brain space.
 * Units: micrometers (μm) from brain coordinate system origin
 */
data class Coordinates3D(
    val x: Float,
    val y: Float,
    val z: Float
) {
    fun distanceTo(other: Coordinates3D): Float {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    companion object {
        val ORIGIN = Coordinates3D(0f, 0f, 0f)
    }
}
