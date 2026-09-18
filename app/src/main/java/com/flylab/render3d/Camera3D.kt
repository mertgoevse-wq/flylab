package com.flylab.render3d

/**
 * 2D point projected on screen with associated depth value for depth sorting.
 */
data class ProjectedPoint(
    val screenX: Float,
    val screenY: Float,
    val depthZ: Float,
    val isVisible: Boolean
)

/**
 * Orbit camera providing smooth 3D rotation, pan, and zoom for inspecting the Drosophila model.
 */
data class Camera3D(
    val target: Vector3D = Vector3D(0f, 0f, 0f),
    val distance: Float = 6.0f,
    val azimuthDegrees: Float = 45.0f,
    val elevationDegrees: Float = 25.0f,
    val fovDegrees: Float = 45.0f
) {
    fun orbit(deltaAzimuthDeg: Float, deltaElevationDeg: Float): Camera3D {
        val newAzimuth = (azimuthDegrees + deltaAzimuthDeg) % 360.0f
        val newElevation = (elevationDegrees + deltaElevationDeg).coerceIn(-85.0f, 85.0f)
        return copy(
            azimuthDegrees = if (newAzimuth < 0f) newAzimuth + 360f else newAzimuth,
            elevationDegrees = newElevation
        )
    }

    fun zoom(zoomFactor: Float): Camera3D {
        val newDist = (distance * zoomFactor).coerceIn(1.5f, 25.0f)
        return copy(distance = newDist)
    }

    fun pan(deltaX: Float, deltaY: Float): Camera3D {
        val azRad = Math.toRadians(azimuthDegrees.toDouble()).toFloat()
        val forwardX = -Math.sin(azRad.toDouble()).toFloat()
        val forwardZ = Math.cos(azRad.toDouble()).toFloat()
        val rightX = forwardZ
        val rightZ = -forwardX

        val panSensitivity = distance * 0.002f
        val newTarget = target + Vector3D(
            x = rightX * deltaX * panSensitivity,
            y = deltaY * panSensitivity,
            z = rightZ * deltaX * panSensitivity
        )
        return copy(target = newTarget)
    }

    /**
     * Calculates the 3D camera eye position in world space.
     */
    fun eyePosition(): Vector3D {
        val azRad = Math.toRadians(azimuthDegrees.toDouble()).toFloat()
        val elRad = Math.toRadians(elevationDegrees.toDouble()).toFloat()

        val x = target.x + distance * Math.cos(elRad.toDouble()).toFloat() * Math.sin(azRad.toDouble()).toFloat()
        val y = target.y + distance * Math.sin(elRad.toDouble()).toFloat()
        val z = target.z + distance * Math.cos(elRad.toDouble()).toFloat() * Math.cos(azRad.toDouble()).toFloat()

        return Vector3D(x, y, z)
    }

    /**
     * Projects a 3D world coordinate onto the 2D screen viewport.
     */
    fun project(point: Vector3D, viewportWidth: Float, viewportHeight: Float): ProjectedPoint {
        // Shift point relative to target
        val shifted = point - target

        // Rotate by -azimuth around Y
        val azRad = -Math.toRadians(azimuthDegrees.toDouble()).toFloat()
        val rotY = shifted.rotateY(azRad)

        // Rotate by -elevation around X
        val elRad = -Math.toRadians(elevationDegrees.toDouble()).toFloat()
        val camSpace = rotY.rotateX(elRad)

        // Camera is looking toward target at distance along -Z
        val zCam = distance - camSpace.z

        if (zCam <= 0.1f) {
            return ProjectedPoint(0f, 0f, zCam, isVisible = false)
        }

        val aspect = viewportWidth / viewportHeight.coerceAtLeast(1f)
        val fovRad = Math.toRadians(fovDegrees.toDouble()).toFloat()
        val f = (1.0f / Math.tan(fovRad.toDouble() * 0.5)).toFloat()

        // Perspective division
        val projX = (camSpace.x * f / aspect) / zCam
        val projY = (camSpace.y * f) / zCam

        // Map normalized device coordinates [-1, 1] to screen pixels
        val screenX = (projX * 0.5f + 0.5f) * viewportWidth
        val screenY = (-projY * 0.5f + 0.5f) * viewportHeight

        return ProjectedPoint(
            screenX = screenX,
            screenY = screenY,
            depthZ = zCam,
            isVisible = screenX >= -200f && screenX <= viewportWidth + 200f &&
                        screenY >= -200f && screenY <= viewportHeight + 200f
        )
    }
}
