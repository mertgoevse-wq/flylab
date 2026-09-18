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
}
