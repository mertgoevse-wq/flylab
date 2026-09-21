package com.flylab.experiment

import com.flylab.domain.model.OdorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EnvironmentFactoryTest {

    @Test
    fun `open field navigation creates centered rewarding source`() {
        val env = EnvironmentFactory.createOpenFieldNavigation()
        assertEquals(50.0f, env.arenaRadiusMm, 0.01f)
        assertEquals(1, env.sources.size)
        
        val source = env.sources.first()
        assertEquals(OdorType.APPLE_CIDER_VINEGAR, source.odorType)
        assertTrue(source.sucroseConcentration > 0f)
    }

    @Test
    fun `two-choice discrimination places appetitive and aversive sources`() {
        val env = EnvironmentFactory.createTwoChoiceDiscrimination()
        assertEquals(2, env.sources.size)
        
        val vinegar = env.sources.find { it.odorType == OdorType.APPLE_CIDER_VINEGAR }
        val geosmin = env.sources.find { it.odorType == OdorType.GEOSMIN }
        
        assertTrue("Must contain engaging odor", vinegar != null)
        assertTrue("Must contain aversive odor", geosmin != null)
        assertTrue("Vinegar must be rewarding", vinegar!!.sucroseConcentration > 0f)
        assertTrue("Geosmin must NOT be rewarding", geosmin!!.sucroseConcentration == 0f)
    }
}
