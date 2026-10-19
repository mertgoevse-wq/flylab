package com.flylab.sim

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Locks the Phase-B guarantee: the brain view is a pure projection of the
 * canonical engine snapshot — world and brain can never diverge.
 */
class BrainProjectionTest {

    @Test
    fun `identical seeds project to identical regional activity`() {
        val engine1 = SimulationEngine(seed = 42L)
        val engine2 = SimulationEngine(seed = 42L)
        repeat(20) {
            engine1.step(0.05f)
            engine2.step(0.05f)
        }

        val activity1 = engine1.history.last().toRegionalActivity()
        val activity2 = engine2.history.last().toRegionalActivity()

        assertEquals(activity1.regionActivities.keys, activity2.regionActivities.keys)
        for ((region, value1) in activity1.regionActivities) {
            assertEquals(
                "Activity mismatch for $region",
                value1, activity2.regionActivities.getValue(region), 0.0001f
            )
        }
    }

    @Test
    fun `projection keys match neuropil names and values are normalized`() {
        val engine = SimulationEngine(seed = 7L)
        repeat(5) { engine.step(0.05f) }
        val snapshot = engine.history.last()
        val activity = snapshot.toRegionalActivity()

        assertEquals(
            snapshot.regionStates.keys.map { it.name }.toSet(),
            activity.regionActivities.keys
        )
        for ((region, value) in activity.regionActivities) {
            assertTrue("Out of range for $region: $value", value in 0f..1f)
        }
        assertEquals((snapshot.timeSeconds * 1000).toLong(), activity.timestamp)
    }

    @Test
    fun `population signal is normalized deterministic and history-synchronous`() {
        val engine1 = SimulationEngine(seed = 42L)
        val engine2 = SimulationEngine(seed = 42L)
        repeat(20) {
            engine1.step(0.05f)
            engine2.step(0.05f)
        }

        val signal1 = engine1.history.populationSignal()
        val signal2 = engine2.history.populationSignal()

        assertEquals(engine1.history.size, signal1.size)
        assertEquals(signal1.size, signal2.size)
        for (i in signal1.indices) {
            assertTrue("Out of range at $i: ${signal1[i]}", signal1[i] in 0f..1f)
            assertEquals("Signal mismatch at $i", signal1[i], signal2[i], 0.0001f)
        }
    }
}
