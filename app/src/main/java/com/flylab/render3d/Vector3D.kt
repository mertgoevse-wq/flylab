package com.flylab.render3d

/**
 * High-performance 3D vector for camera transformations and biological mesh projections.
 */
data class Vector3D(
    val x: Float = 0.0f,
    val y: Float = 0.0f,
    val z: Float = 0.0f
) {
    operator fun plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3D) = Vector3D(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Float) = Vector3D(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Float) = Vector3D(x / scalar, y / scalar, z / scalar)

    fun dot(other: Vector3D): Float = x * other.x + y * other.y + z * other.z

    fun cross(other: Vector3D): Vector3D = Vector3D(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    fun length(): Float = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

    fun normalized(): Vector3D {
        val len = length()
        return if (len > 0.00001f) this / len else Vector3D(0f, 0f, 0f)
    }

    fun rotateX(radians: Float): Vector3D {
        val cos = Math.cos(radians.toDouble()).toFloat()
        val sin = Math.sin(radians.toDouble()).toFloat()
        return Vector3D(x, y * cos - z * sin, y * sin + z * cos)
    }

    fun rotateY(radians: Float): Vector3D {
        val cos = Math.cos(radians.toDouble()).toFloat()
        val sin = Math.sin(radians.toDouble()).toFloat()
        return Vector3D(x * cos + z * sin, y, -x * sin + z * cos)
    }

    fun rotateZ(radians: Float): Vector3D {
        val cos = Math.cos(radians.toDouble()).toFloat()
        val sin = Math.sin(radians.toDouble()).toFloat()
        return Vector3D(x * cos - y * sin, x * sin + y * cos, z)
    }

    companion object {
        val ZERO = Vector3D(0f, 0f, 0f)
        val UP = Vector3D(0f, 1f, 0f)
        val FORWARD = Vector3D(0f, 0f, 1f)
        val RIGHT = Vector3D(1f, 0f, 0f)
    }
}
