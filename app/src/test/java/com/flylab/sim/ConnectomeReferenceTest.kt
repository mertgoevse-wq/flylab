package com.flylab.sim

import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.ProvenanceLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConnectomeReferenceTest {

    @Test
    fun `connectome subset contains required circuits for milestone 1`() {
        val neurons = ConnectomeReference.NEURONS
        assertTrue(neurons.isNotEmpty())

        val neuropils = neurons.map { it.neuropil }.toSet()
        assertTrue(neuropils.contains(NeuropilId.ANTENNAL_LOBE))
        assertTrue(neuropils.contains(NeuropilId.MUSHROOM_BODY))
        assertTrue(neuropils.contains(NeuropilId.LATERAL_HORN))
        assertTrue(neuropils.contains(NeuropilId.CENTRAL_COMPLEX))
        assertTrue(neuropils.contains(NeuropilId.LATERAL_ACCESSORY_LOBE))
    }

    @Test
    fun `connectome includes key identified reinforcement neurons`() {
        val pam = ConnectomeReference.getNeuron("DAN_PAM")
        assertNotNull(pam)
        assertEquals(NeuropilId.MUSHROOM_BODY, pam?.neuropil)

        val ppl1 = ConnectomeReference.getNeuron("DAN_PPL1")
        assertNotNull(ppl1)
        assertEquals(NeuropilId.MUSHROOM_BODY, ppl1?.neuropil)
    }

    @Test
    fun `synapses contain both immutable structural and plastic learning connections`() {
        val synapses = ConnectomeReference.SYNAPSES
        assertTrue(synapses.isNotEmpty())

        val plasticSynapses = synapses.filter { it.isPlastic }
        assertTrue("Mushroom body must contain plastic KC->MBON synapses", plasticSynapses.isNotEmpty())

        // All plastic synapses must target MBONs
        for (syn in plasticSynapses) {
            assertTrue(syn.postNeuronId.startsWith("MBON_"))
            assertEquals(NeuromodulatorType.ACETYLCHOLINE, syn.neurotransmitter)
        }

        // Structural synapses like ORN->PN must NOT be plastic
        val ornPnSyns = synapses.filter { it.preNeuronId.startsWith("ORN_") && it.postNeuronId.startsWith("PN_") }
        assertTrue(ornPnSyns.isNotEmpty())
        for (syn in ornPnSyns) {
            assertFalse(syn.isPlastic)
        }
    }

    @Test
    fun `connectome subset evidence carries valid DERIVED provenance`() {
        assertEquals(ProvenanceLevel.DERIVED, ConnectomeReference.EVIDENCE.level)
        assertNotNull(ConnectomeReference.EVIDENCE.citation)
        assertNotNull(ConnectomeReference.EVIDENCE.doi)
    }

    @Test
    fun `kenyon cell projections and neurons are explicitly classified as MODELED`() {
        val kcNeurons = ConnectomeReference.NEURONS.filter { it.id.startsWith("KC_") }
        assertEquals(16, kcNeurons.size)

        for (kc in kcNeurons) {
            assertTrue("Kenyon cell #$kc must be marked as representative model", kc.isRepresentativeModel)
            assertEquals("Kenyon cell #$kc evidence must be MODELED", ProvenanceLevel.MODELLED, kc.provenanceLevel)
            assertFalse("Modeled KCs must not claim empirical verification", kc.evidence.level.isEmpiricallyVerified)
        }

        // Check PN -> KC synapses
        val pnKcSynapses = ConnectomeReference.SYNAPSES.filter { it.preNeuronId.startsWith("PN_") && it.postNeuronId.startsWith("KC_") }
        assertTrue(pnKcSynapses.isNotEmpty())
        for (syn in pnKcSynapses) {
            assertEquals("PN->KC claw synapse must be classified as MODELED", ProvenanceLevel.MODELLED, syn.provenanceLevel)
            assertFalse(syn.evidence.level.isEmpiricallyVerified)
        }
    }

    @Test
    fun `no modeled synapse or circuit is presented as MEASURED FlyWire anatomy`() {
        for (syn in ConnectomeReference.SYNAPSES) {
            // A synapse can only be DERIVED, PUBLISHED or MODELED in this vertical slice model
            assertFalse(
                "Synapse ${syn.synapseId} (${syn.preNeuronId} -> ${syn.postNeuronId}) is simplified/modeled and cannot be marked MEASURED",
                syn.provenanceLevel == ProvenanceLevel.MEASURED
            )
        }
    }

    @Test
    fun `plastic KC to MBON synapses are explicitly MODELED and immutable in reference data`() {
        val plasticSynapses = ConnectomeReference.SYNAPSES.filter { it.isPlastic }
        assertEquals(32, plasticSynapses.size) // 16 KCs * 2 MBONs

        for (syn in plasticSynapses) {
            assertEquals(ProvenanceLevel.MODELLED, syn.provenanceLevel)
            assertEquals(1.0f, syn.baselineWeight, 0.001f)
        }
    }
}
