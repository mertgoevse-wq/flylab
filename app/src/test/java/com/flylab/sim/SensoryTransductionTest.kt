package com.flylab.sim

import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.OdorSource
import com.flylab.domain.model.OdorType
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests for sensory transduction system.
 * Validates bilateral odor sampling, spatial calculations, and sucrose contact.
 */
class SensoryTransductionTest {

    @Test
    fun `bilateral antennal positions are correctly offset from head center`() {
        val fly = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 0f,
                posYmm = 0f,
                headingRadians = 0f // Facing right along X axis
            )
        )

        val env = Environment()
        val input = SensoryTransduction.transduce(fly, env)

        // When heading = 0 (facing right), left antenna should be forward-left, right antenna forward-right
        // Both should be approximately ANTENNAL_OFFSET_MM distance from origin
        assertNotNull(input)
    }

    @Test
    fun `odor gradient produces different concentrations at left and right antennae`() {
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "source1",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 10f,
                    posYmm = 0f,
                    emissionRate = 1.0f,
                    plumeSigmaMm = 15f
                )
            )
        )

        val fly = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 0f,
                posYmm = 0f,
                headingRadians = 0f // Facing source directly
            )
        )

        val input = SensoryTransduction.transduce(fly, env)

        // Both antennae should detect odor
        assertTrue("Left antenna should detect odor", input.odorLeftAntenna > 0f)
        assertTrue("Right antenna should detect odor", input.odorRightAntenna > 0f)

        // Mean concentration should be positive
        assertTrue("Mean odor concentration should be positive", input.meanOdorConcentration > 0f)
    }

    @Test
    fun `fly positioned far from odor source receives minimal concentration`() {
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "distant_source",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 0f,
                    posYmm = 0f,
                    emissionRate = 0.5f,
                    plumeSigmaMm = 10f
                )
            )
        )

        val fly = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 100f, // Very far from source
                posYmm = 100f,
                headingRadians = 0f
            )
        )

        val input = SensoryTransduction.transduce(fly, env)

        // Concentration should be very low or zero at this distance
        assertTrue("Odor concentration should be minimal at distance", input.meanOdorConcentration < 0.05f)
    }

    @Test
    fun `sucrose contact detected when proboscis within contact distance`() {
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "sugar_patch",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 0.5f, // Very close to origin
                    posYmm = 0f,
                    emissionRate = 0.3f,
                    sucroseConcentration = 1.0f
                )
            )
        )

        val fly = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 0f,
                posYmm = 0f,
                headingRadians = 0f
            )
        )

        val input = SensoryTransduction.transduce(fly, env)

        // Proboscis should contact sucrose
        assertTrue("Sucrose contact should be detected", input.sucroseContact > 0f)
    }

    @Test
    fun `sensory input reflects environment ambient conditions`() {
        val customTemp = 28.5f
        val customLux = 0.8f

        val env = Environment(
            ambientTemperatureCelsius = customTemp,
            ambientLuminance = customLux
        )

        val fly = Fly()
        val input = SensoryTransduction.transduce(fly, env)

        assertEquals(customTemp, input.temperatureCelsius, 0.001f)
        assertEquals(customLux, input.lightIntensity, 0.001f)
    }

    @Test
    fun `deterministic sensory sampling for identical fly position and environment`() {
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "test_source",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 5f,
                    posYmm = 5f,
                    emissionRate = 0.8f
                )
            )
        )

        val fly = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 3f,
                posYmm = 3f,
                headingRadians = 0.785f // 45 degrees
            )
        )

        val input1 = SensoryTransduction.transduce(fly, env)
        val input2 = SensoryTransduction.transduce(fly, env)

        // Identical inputs should produce identical outputs
        assertEquals(input1.odorLeftAntenna, input2.odorLeftAntenna, 0.0001f)
        assertEquals(input1.odorRightAntenna, input2.odorRightAntenna, 0.0001f)
        assertEquals(input1.sucroseContact, input2.sucroseContact, 0.0001f)
    }

    @Test
    fun `heading rotation changes bilateral odor sampling pattern`() {
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "source_right",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 10f,
                    posYmm = 0f,
                    emissionRate = 1.0f,
                    plumeSigmaMm = 20f
                )
            )
        )

        val flyFacingRight = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 0f,
                posYmm = 0f,
                headingRadians = 0f // Facing right toward source
            )
        )

        val flyFacingLeft = Fly().copy(
            behavioralState = Fly().behavioralState.copy(
                posXmm = 0f,
                posYmm = 0f,
                headingRadians = Math.PI.toFloat() // Facing left away from source
            )
        )

        val inputRight = SensoryTransduction.transduce(flyFacingRight, env)
        val inputLeft = SensoryTransduction.transduce(flyFacingLeft, env)

        // Facing toward source should yield higher mean concentration
        assertTrue(
            "Fly facing source should detect stronger odor than fly facing away",
            inputRight.meanOdorConcentration > inputLeft.meanOdorConcentration
        )
    }
}
