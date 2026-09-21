package com.flylab.domain.genome

import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GenomeModelsTest {

    private val testEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        citation = "Test Citation",
        notes = "Test notes",
        confidence = 0.9f
    )

    @Test
    fun genomicCoordinateLengthCalculation() {
        val coord = GenomicCoordinate(chromosome = "2L", startBp = 1000, endBp = 2000)
        assertEquals(1001, coord.length)
    }

    @Test
    fun geneCanHoldAnnotations() {
        val coord = GenomicCoordinate("3R", 1000, 1500)
        val annotation = GenomicAnnotation("CDS", coord, "Coding Sequence")
        val gene = Gene(
            id = "FBgn123",
            symbol = "test",
            fullName = "test gene",
            coordinate = coord,
            strand = Strand.PLUS,
            annotations = listOf(annotation),
            description = "Test gene",
            evidence = testEvidence
        )

        assertEquals(1, gene.annotations.size)
        assertEquals("CDS", gene.annotations[0].type)
    }

    @Test
    fun mutationExperimentHasSimulatedProvenanceByDefault() {
        val coord = GenomicCoordinate("X", 500, 500)
        val variant = Variant(
            id = "V1",
            coordinate = coord,
            referenceAllele = "A",
            alternateAllele = "T",
            type = VariantType.SNP,
            functionalImpact = "Missense",
            evidence = testEvidence
        )

        val gene = Gene("G1", "sym", "Full", coord, Strand.PLUS, emptyList(), "Desc", testEvidence)

        val mutationExperiment = MutationExperiment(
            targetGene = gene,
            appliedVariant = variant,
            pathwayEffects = emptyList(),
            predictedPhenotypes = listOf(
                PhenotypePrediction("Eye color", "White eyes", ScientificEvidence(ProvenanceLevel.MODELLED, notes="", confidence=0.8f))
            )
        )

        assertEquals(ProvenanceLevel.SIMULATED, mutationExperiment.provenanceLevel)
        assertEquals(1, mutationExperiment.predictedPhenotypes.size)
        assertEquals("Eye color", mutationExperiment.predictedPhenotypes[0].trait)
    }
}
