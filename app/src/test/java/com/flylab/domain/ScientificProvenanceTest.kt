package com.flylab.domain

import com.flylab.domain.model.BiologicalSex
import com.flylab.domain.model.Fly
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * Validates strict adherence to FlyLab scientific integrity rules (CLAUDE.md).
 */
class ScientificProvenanceTest {

    @Test
    fun `provenance levels cover all mandated categories`() {
        val canonicalNames = ProvenanceLevel.entries.map { it.canonicalName }
        assertTrue(canonicalNames.contains("MEASURED"))
        assertTrue(canonicalNames.contains("PUBLISHED"))
        assertTrue(canonicalNames.contains("DERIVED"))
        assertTrue(canonicalNames.contains("MODELED"))
        assertTrue(canonicalNames.contains("SIMULATED"))
        assertTrue(canonicalNames.contains("HYPOTHESIS"))
    }

    @Test
    fun `flywire female connectome is classified as MEASURED`() {
        val evidence = ScientificEvidence.FLYWIRE_FEMALE_CONNECTOME_V783
        assertEquals(ProvenanceLevel.MEASURED, evidence.level)
        assertTrue(evidence.isEmpiricallyVerified())
        assertNotNull(evidence.doi)
        assertEquals("v783", evidence.datasetVersion)
    }

    @Test
    fun `no simplified or modeled connection is classified as MEASURED`() {
        val kcClawEvidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL
        assertEquals(ProvenanceLevel.MODELED, kcClawEvidence.level)
        assertFalse("Modeled KC claw connections cannot claim to be empirically measured", kcClawEvidence.isEmpiricallyVerified())

        val premotorEvidence = ScientificEvidence.PREMOTOR_STEERING_MODEL
        assertEquals(ProvenanceLevel.MODELED, premotorEvidence.level)
        assertFalse(premotorEvidence.isEmpiricallyVerified())
    }

    @Test
    fun `simulation dynamic overlays carry SIMULATED provenance`() {
        val overlayEvidence = ScientificEvidence.SIMULATED_RUNTIME_OVERLAY
        assertEquals(ProvenanceLevel.SIMULATED, overlayEvidence.level)
        assertFalse(overlayEvidence.isEmpiricallyVerified())
    }

    @Test
    fun `male fly connectome cannot be marked as MEASURED without empirical data`() {
        try {
            // Attempting to claim male whole-brain connectome is measured must fail
            Fly(
                sex = BiologicalSex.MALE,
                evidence = ScientificEvidence(
                    level = ProvenanceLevel.MEASURED,
                    notes = "Fabricated male connectome"
                )
            )
            fail("Should throw IllegalArgumentException: fabricating measured male connectome is forbidden by CLAUDE.md")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Male whole-brain connectome cannot be marked MEASURED") == true)
        }
    }

    @Test
    fun `forbidden mammalian endorphin claims are strictly rejected`() {
        try {
            NeuromodulatorType.assertValidDrosophilaModulator("beta-endorphin")
            fail("Should throw IllegalArgumentException: Drosophila does not have endorphin reward biology")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("endorphin") == true)
        }
    }

    @Test
    fun `drosophila neuromodulators include octopamine and dopamine`() {
        val types = NeuromodulatorType.entries.map { it.name }
        assertTrue(types.contains("OCTOPAMINE"))
        assertTrue(types.contains("DOPAMINE"))
        assertTrue(types.contains("SEROTONIN"))
        assertTrue(types.contains("ACETYLCHOLINE"))
        assertTrue(types.contains("GABA"))
    }

    private fun ScientificEvidence.isEmpiricallyVerified() = level.isEmpiricallyVerified
}
