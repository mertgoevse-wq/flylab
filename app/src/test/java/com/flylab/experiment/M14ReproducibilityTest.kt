package com.flylab.experiment

import com.flylab.domain.model.Fly
import com.flylab.domain.model.ProvenanceLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class M14ReproducibilityTest {

    @Test
    fun `experiment model tracks multi-organism foundation properties`() {
        val scenario = ExperimentConfiguration.standardFoodSeeking(seed = 999L)
        
        // Assert Subject abstraction is accessible
        assertEquals(1, scenario.subjects.size)
        assertEquals("Drosophila melanogaster", scenario.subjects.first().species)
        
        // Assert canonical experiment data
        assertEquals(999L, scenario.seed)
        assertNotNull(scenario.datasetVersion)
        assertNotNull(scenario.modelVersion)
    }

    @Test
    fun `time series logs strictly require evidence provenance`() {
        val eventStore = EventStore()
        eventStore.logMetric("fly_1", "speed", 0.1f, 1.2f)
        eventStore.logMetric("fly_1", "speed", 0.2f, 1.4f)
        
        val series = eventStore.getSeries("fly_1", "speed", "mm/s")
        assertEquals(2, series.timestamps.size)
        // Ensure TS log carries Simulated or Modelled level
        assertEquals(ProvenanceLevel.SIMULATED, series.provenanceLevel)
    }

    @Test
    fun `graph hierarchy respects node provenance`() {
        val neuronNode = com.flylab.domain.graph.NeuronNode(
            id = "n_123",
            label = "DA1 PN",
            typeName = "ProjectionNeuron",
            transmitter = "ACH"
        )
        // Defaults to measured for neuron nodes from connectome
        assertEquals(ProvenanceLevel.MEASURED, neuronNode.provenanceLevel)
        
        val hypothesisNode = com.flylab.domain.graph.InterventionNode(
            id = "int_01",
            label = "Unknown Drug Application",
            interventionType = "Chemical"
        )
        assertEquals(ProvenanceLevel.EXPERIMENTAL, hypothesisNode.provenanceLevel)
    }
}
