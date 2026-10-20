package com.flylab.render3d

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Phase-C guard: halteres exist bilaterally mirrored, and the whole
 * anatomy mesh stays finite and non-degenerate.
 */
class HaltereGeometryTest {

    @Test
    fun `halteres exist on both sides mirrored in x`() {
        val (_, lines) = FlyMeshGeometry.buildAnatomyGeometry()
        val left = lines.filter { it.tag == "haltere" && it.start.x < 0f }
        val right = lines.filter { it.tag == "haltere" && it.start.x > 0f }

        assertTrue("Left haltere lines missing", left.isNotEmpty())
        assertEquals("Haltere line count must mirror", left.size, right.size)

        // Mirror left x values first, then sort: (-left).sorted() must equal right.sorted().
        val leftMirrored = left.flatMap { listOf(-it.start.x, -it.end.x) }.sorted()
        val rightXs = right.flatMap { listOf(it.start.x, it.end.x) }.sorted()
        assertEquals(leftMirrored.size, rightXs.size)
        for (i in leftMirrored.indices) {
            assertEquals("Haltere x not mirrored at $i", leftMirrored[i], rightXs[i], 0.0001f)
        }
    }

    @Test
    fun `anatomy mesh is finite and non-degenerate`() {
        val (polygons, lines) = FlyMeshGeometry.buildAnatomyGeometry()
        assertTrue(polygons.isNotEmpty())
        assertTrue(lines.isNotEmpty())

        for (line in lines) {
            for (p in listOf(line.start, line.end)) {
                assertTrue("Non-finite vertex in $line", p.x.isFinite() && p.y.isFinite() && p.z.isFinite())
            }
        }
        for (poly in polygons) {
            assertTrue("Degenerate polygon: $poly", poly.vertices.distinct().size >= 3)
        }
    }
}
