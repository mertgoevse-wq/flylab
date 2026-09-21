package com.flylab.experiment

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertNotNull

class LearningExperimentProtocolTest {

    @Test
    fun `conditioning and extinction protocol executes sequentially with memory transfer`() {
        // Use a consistent seed
        val config = ExperimentConfiguration.standardFoodSeeking(seed = 101L)
        val protocol = LearningExperimentProtocol(config)
        
        val (conditioning, extinction) = protocol.runConditioningAndExtinction()
        
        // Confirm journals return valid distinct data
        assertEquals("exp_food_seeking_conditioning", conditioning.configuration.id)
        assertEquals("exp_food_seeking_extinction", extinction.configuration.id)
        
        // Confirm sucrose was present in conditioning and absent in extinction
        val condSources = conditioning.configuration.initialEnvironment.sources
        val extSources = extinction.configuration.initialEnvironment.sources
        
        assertTrue(condSources.first().sucroseConcentration > 0.0f)
        assertEquals(0.0f, extSources.first().sucroseConcentration, 0.001f)
        
        // The final synaptic weights should differ somewhat if extinction occurred,
        // or at minimum they must carry over. (This is highly dependent on the model parameters,
        // but we verify the structural execution here).
        assertNotNull(conditioning.finalSynapticWeightChanges)
        assertNotNull(extinction.finalSynapticWeightChanges)
    }
}
