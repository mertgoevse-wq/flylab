package com.flylab.sim.backend

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pure JVM tests for the accelerator strategy selection.
 * Hardware dispatch itself is validated at runtime on-device
 * (numeric self-test in AcceleratorBridge).
 */
class AcceleratorBridgeStrategyTest {

    private fun report(
        vulkan: Boolean = false,
        npu: Boolean = false,
        nnapi: Boolean = false
    ) = AcceleratorProbe.Report(
        vulkanAvailable = vulkan,
        npuLikely = npu,
        nnapiLikely = nnapi,
        evidence = emptyList()
    )

    @Test
    fun `exynos-class device tries NPU first then GPU then CPU`() {
        val order = AcceleratorBridge.selectStrategy(report(vulkan = true, npu = true, nnapi = true))
        assertEquals(
            listOf(AcceleratorBridge.Strategy.NPU, AcceleratorBridge.Strategy.GPU, AcceleratorBridge.Strategy.CPU),
            order
        )
    }

    @Test
    fun `vulkan-only device skips NPU`() {
        val order = AcceleratorBridge.selectStrategy(report(vulkan = true))
        assertEquals(
            listOf(AcceleratorBridge.Strategy.GPU, AcceleratorBridge.Strategy.CPU),
            order
        )
    }

    @Test
    fun `bare fallback is always CPU`() {
        val order = AcceleratorBridge.selectStrategy(report())
        assertEquals(listOf(AcceleratorBridge.Strategy.CPU), order)
    }

    @Test
    fun `offload is opt-in and off by default`() {
        System.clearProperty(AcceleratorBridge.OFFLOAD_PROPERTY)
        assertEquals(false, AcceleratorBridge.offloadEnabled())
        System.setProperty(AcceleratorBridge.OFFLOAD_PROPERTY, "true")
        assertEquals(true, AcceleratorBridge.offloadEnabled())
        System.clearProperty(AcceleratorBridge.OFFLOAD_PROPERTY)
    }
}
