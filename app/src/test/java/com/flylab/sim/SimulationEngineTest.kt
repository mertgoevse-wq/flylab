package com.flylab.sim

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.OdorSource
import com.flylab.domain.model.OdorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulationEngineTest {

    @Test
    fun `simulation execution is strictly deterministic given identical seed`() {
        val engine1 = SimulationEngine(seed = 12345L)
        val engine2 = SimulationEngine(seed = 12345L)

        for (i in 1..40) {
            engine1.step(0.05f)
            engine2.step(0.05f)
        }

        assertEquals(engine1.currentStep, engine2.currentStep)
        assertEquals(engine1.currentTimeSeconds, engine2.currentTimeSeconds, 0.0001f)
        assertEquals(engine1.currentFly.behavioralState.posXmm, engine2.currentFly.behavioralState.posXmm, 0.0001f)
        assertEquals(engine1.currentFly.behavioralState.posYmm, engine2.currentFly.behavioralState.posYmm, 0.0001f)
        assertEquals(engine1.currentFly.behavioralState.headingRadians, engine2.currentFly.behavioralState.headingRadians, 0.0001f)

        for ((neuronId, rate1) in engine1.currentFiringRates) {
            val rate2 = engine2.currentFiringRates[neuronId]
            assertNotNull(rate2)
            assertEquals("Rate mismatch for $neuronId", rate1, rate2!!, 0.0001f)
        }
    }

    @Test
    fun `appetitive odor stimulates projection neurons and driving behavior`() {
        val envWithFood = Environment(
            sources = listOf(
                OdorSource(
                    id = "food",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 5.0f,
                    posYmm = 0.0f,
                    emissionRate = 1.0f,
                    plumeSigmaMm = 25.0f
                )
            )
        )

        val engine = SimulationEngine(initialEnvironment = envWithFood, seed = 42L)

        for (i in 1..25) {
            engine.step(0.05f)
        }

        val pnRate = engine.currentFiringRates["PN_DM1_R"] ?: 0.0f
        assertTrue("PN_DM1 should be activated by vinegar odor, was $pnRate", pnRate > 0.2f)
    }

    @Test
    fun `associative learning depresses aversive MBON synapse upon sugar reward`() {
        // Place fly directly on sucrose reward source
        val env = Environment(
            sources = listOf(
                OdorSource(
                    id = "sugar_patch",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 0.0f,
                    posYmm = 0.0f,
                    emissionRate = 0.8f,
                    sucroseConcentration = 1.0f
                )
            )
        )

        val engine = SimulationEngine(initialEnvironment = env, seed = 99L)

        // Step through simulation to allow learning
        for (i in 1..30) {
            engine.step(0.05f)
        }

        // Verify PAM dopaminergic cluster fired
        val danPam = engine.currentFiringRates["DAN_PAM"] ?: 0.0f
        assertTrue("DAN_PAM reward neuron should activate on sucrose, was $danPam", danPam > 0.3f)

        // Verify that plastic KC -> MBON_AVERSIVE synapses underwent depression (deltaWeight < 0)
        val overlays = engine.currentSynapticOverlays
        val aversiveOverlays = overlays.values.filter {
            val ref = ConnectomeReference.SYNAPSES.find { s -> s.synapseId == it.synapseId }
            ref?.postNeuronId == "MBON_AVERSIVE"
        }

        assertTrue("Overlays for plastic synapses should be recorded", aversiveOverlays.isNotEmpty())
        val depressedCount = aversiveOverlays.count { it.deltaWeight < -0.001f }
        assertTrue("Associative learning must depress aversive synapses, found $depressedCount depressed", depressedCount > 0)
    }

    @Test
    fun `time travel rewind accurately restores previous state`() {
        val engine = SimulationEngine(seed = 777L)

        for (i in 1..30) {
            engine.step(0.05f)
        }

        val step20Snapshot = engine.history.find { it.step == 20L }
        assertNotNull(step20Snapshot)

        val success = engine.seekToStep(20L)
        assertTrue(success)
        assertEquals(20L, engine.currentStep)
        assertEquals(step20Snapshot?.timeSeconds ?: 0f, engine.currentTimeSeconds, 0.0001f)
        assertEquals(step20Snapshot?.fly?.behavioralState?.posXmm ?: 0f, engine.currentFly.behavioralState.posXmm, 0.0001f)
    }

    @Test
    fun `brain region perturbation suppresses neural activity`() {
        val engine = SimulationEngine(seed = 55L)

        // Silence antennal lobe
        engine.setRegionPerturbation(NeuropilId.ANTENNAL_LOBE, 0.0f)

        for (i in 1..15) {
            engine.step(0.05f)
        }

        val alState = engine.currentRegionStates[NeuropilId.ANTENNAL_LOBE]
        assertNotNull(alState)
        assertTrue("Antennal lobe must be flagged as perturbed", alState?.isPerturbed == true)
        assertEquals(0.0f, alState?.perturbationFactor ?: 1f, 0.001f)
    }
}
