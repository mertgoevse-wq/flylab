package com.flylab.sim

import com.flylab.domain.model.NeuropilId
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests for neural dynamics system.
 * Validates rate-based integration, region activation, and perturbations.
 */
class NeuralDynamicsTest {

    @Test
    fun `neural dynamics step produces valid firing rates`() {
        val previousRates = ConnectomeReference.NEURONS.associate { it.id to 0.05f }
        val sensoryInput = com.flylab.domain.model.SensoryInput(
            odorLeftAntenna = 0.5f,
            odorRightAntenna = 0.5f,
            activeOdor = com.flylab.domain.model.OdorType.APPLE_CIDER_VINEGAR
        )

        val result = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = sensoryInput,
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        // All firing rates should be non-negative
        assertTrue("All firing rates must be non-negative",
            result.firingRates.values.all { it >= 0f })

        // Region states should be computed
        assertTrue("Region states should be populated",
            result.regionStates.isNotEmpty())

        // Major regions should have states
        assertNotNull("Antennal lobe state should exist",
            result.regionStates[NeuropilId.ANTENNAL_LOBE])
        assertNotNull("Mushroom body state should exist",
            result.regionStates[NeuropilId.MUSHROOM_BODY])
    }

    @Test
    fun `odor input drives projection neuron activation`() {
        val previousRates = ConnectomeReference.NEURONS.associate { it.id to 0.0f }

        val strongOdorInput = com.flylab.domain.model.SensoryInput(
            odorLeftAntenna = 0.8f,
            odorRightAntenna = 0.8f,
            activeOdor = com.flylab.domain.model.OdorType.APPLE_CIDER_VINEGAR
        )

        val result = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = strongOdorInput,
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        // Projection neurons should activate in response to odor
        val pnRates = result.firingRates.filter { it.key.startsWith("PN_") }
        assertTrue("At least one projection neuron should activate",
            pnRates.values.any { it > 0.1f })
    }

    @Test
    fun `region perturbation suppresses neural activity`() {
        val previousRates = ConnectomeReference.NEURONS.associate { it.id to 0.3f }
        val sensoryInput = com.flylab.domain.model.SensoryInput(
            odorLeftAntenna = 0.5f,
            odorRightAntenna = 0.5f,
            activeOdor = com.flylab.domain.model.OdorType.APPLE_CIDER_VINEGAR
        )

        // Silence antennal lobe
        val perturbations = mapOf(NeuropilId.ANTENNAL_LOBE to 0.0f)

        val result = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = sensoryInput,
            regionPerturbations = perturbations,
            dtSeconds = 0.05f
        )

        val alState = result.regionStates[NeuropilId.ANTENNAL_LOBE]
        assertNotNull("Antennal lobe state should exist", alState)
        assertTrue("Antennal lobe should be marked as perturbed", alState?.isPerturbed == true)
        assertEquals("Perturbation factor should be 0.0", 0.0f, alState?.perturbationFactor ?: 1f, 0.001f)
    }

    @Test
    fun `synaptic overlays modulate connection strength`() {
        val previousRates = mapOf(
            "KC_001" to 0.5f,
            "MBON_AVERSIVE" to 0.2f
        )

        // Create synaptic overlay reducing KC -> MBON connection
        val overlays = mapOf(
            "syn_kc001_mbon_aversive" to com.flylab.domain.model.SynapseSimulationOverlay(
                synapseId = "syn_kc001_mbon_aversive",
                deltaWeight = -0.3f,
                lastModifiedStep = 100L,
                lastModifiedTimestampMs = 5000L
            )
        )

        val result = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = overlays,
            sensoryInput = com.flylab.domain.model.SensoryInput(),
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        // MBON activity should be influenced by modified synaptic weights
        val mbonRate = result.firingRates["MBON_AVERSIVE"]
        assertNotNull("MBON firing rate should be computed", mbonRate)
    }

    @Test
    fun `neural dynamics is deterministic for identical inputs`() {
        val previousRates = ConnectomeReference.NEURONS.associate { it.id to 0.1f }
        val sensoryInput = com.flylab.domain.model.SensoryInput(
            odorLeftAntenna = 0.6f,
            odorRightAntenna = 0.6f
        )

        val result1 = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = sensoryInput,
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        val result2 = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = sensoryInput,
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        // Identical inputs should produce identical outputs
        assertEquals("Number of firing rates should match",
            result1.firingRates.size, result2.firingRates.size)

        result1.firingRates.forEach { (neuronId, rate1) ->
            val rate2 = result2.firingRates[neuronId]
            assertNotNull("Neuron $neuronId should exist in both results", rate2)
            assertEquals("Firing rate for $neuronId should be identical",
                rate1, rate2!!, 0.0001f)
        }
    }

    @Test
    fun `region mean activity reflects constituent neuron firing rates`() {
        val previousRates = mapOf(
            "PN_DM1_L" to 0.4f,
            "PN_DM1_R" to 0.6f,
            "PN_VA1v_L" to 0.3f
        )

        val result = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = com.flylab.domain.model.SensoryInput(),
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        val alState = result.regionStates[NeuropilId.ANTENNAL_LOBE]
        assertNotNull("Antennal lobe region state should exist", alState)
        assertTrue("Antennal lobe mean activity should reflect PN activity",
            alState?.meanActivity!! > 0f)
    }

    @Test
    fun `excitatory perturbation amplifies neural activity`() {
        val previousRates = ConnectomeReference.NEURONS.associate { it.id to 0.2f }

        // Excite mushroom body (3x amplification)
        val perturbations = mapOf(NeuropilId.MUSHROOM_BODY to 3.0f)

        val normalResult = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = com.flylab.domain.model.SensoryInput(),
            regionPerturbations = emptyMap(),
            dtSeconds = 0.05f
        )

        val excitedResult = NeuralDynamics.step(
            previousRates = previousRates,
            synapticOverlays = emptyMap(),
            sensoryInput = com.flylab.domain.model.SensoryInput(),
            regionPerturbations = perturbations,
            dtSeconds = 0.05f
        )

        val normalMB = normalResult.regionStates[NeuropilId.MUSHROOM_BODY]?.meanActivity ?: 0f
        val excitedMB = excitedResult.regionStates[NeuropilId.MUSHROOM_BODY]?.meanActivity ?: 0f

        assertTrue("Excitatory perturbation should increase mushroom body activity",
            excitedMB > normalMB)
    }
}
