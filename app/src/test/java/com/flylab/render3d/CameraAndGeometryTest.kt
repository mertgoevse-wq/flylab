package com.flylab.render3d

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CameraAndGeometryTest {

    @Test
    fun `vector3D operations conform to euclidean geometry`() {
        val v1 = Vector3D(1f, 0f, 0f)
        val v2 = Vector3D(0f, 1f, 0f)

        val v3 = v1.cross(v2)
        assertEquals(0f, v3.x, 0.0001f)
        assertEquals(0f, v3.y, 0.0001f)
        assertEquals(1f, v3.z, 0.0001f)

        assertEquals(0f, v1.dot(v2), 0.0001f)
        assertEquals(1f, v1.length(), 0.0001f)

        val rotated = v1.rotateY((Math.PI * 0.5).toFloat())
        assertEquals(0f, rotated.x, 0.0001f)
        assertEquals(-1f, rotated.z, 0.0001f)
    }

    @Test
    fun `camera projects 3d points onto 2d viewport with depth culling`() {
        val camera = Camera3D(target = Vector3D(0f, 0f, 0f), distance = 5.0f, azimuthDegrees = 0f, elevationDegrees = 0f)

        // Point at origin (target) should project near center of screen
        val projectedCenter = camera.project(Vector3D(0f, 0f, 0f), 800f, 600f)
        assertTrue(projectedCenter.isVisible)
        assertEquals(400f, projectedCenter.screenX, 5f)
        assertEquals(300f, projectedCenter.screenY, 5f)

        // Point behind camera should not be visible
        val pointBehind = camera.project(Vector3D(0f, 0f, 10f), 800f, 600f)
        assertFalse(pointBehind.isVisible)
    }

    @Test
    fun `camera orbit and zoom respect bounds`() {
        var camera = Camera3D()
        camera = camera.orbit(360f, 0f)
        assertEquals(45f, camera.azimuthDegrees, 0.01f)

        // Test zoom clamping
        camera = camera.zoom(0.01f)
        assertTrue("Zoom distance should be clamped to minimum 1.5", camera.distance >= 1.5f)

        camera = camera.zoom(100f)
        assertTrue("Zoom distance should be clamped to maximum 25.0", camera.distance <= 25.0f)
    }

    @Test
    fun `anatomy geometry generates complete morphological model`() {
        val (polygons, lines) = FlyMeshGeometry.buildAnatomyGeometry()
        assertTrue("Polygons for body, wings, and eyes must be generated", polygons.isNotEmpty())
        assertTrue("Lines for leg joints, veins, and bristles must be generated", lines.isNotEmpty())

        val neuropils = FlyMeshGeometry.buildBrainNeuropils()
        assertEquals(11, neuropils.size) // AL (2), MB (2), CX (1), LH (2), OL (2), LAL (2)
    }
}
