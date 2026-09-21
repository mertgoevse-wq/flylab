package com.flylab.data.genome

import com.flylab.domain.genome.*
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence

object MockGenomeDatabase {

    val dmelEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        citation = "Adams et al. 2000, Science; FlyBase",
        datasetSource = "FlyBase Release 6",
        confidence = 1.0f
    )

    val dmel = Species(
        id = "dmel",
        scientificName = "Drosophila melanogaster",
        commonName = "Fruit Fly",
        evidence = dmelEvidence
    )

    val chromosomes = listOf(
        Chromosome("2L", 23513712, dmel.id, dmelEvidence),
        Chromosome("2R", 25286936, dmel.id, dmelEvidence),
        Chromosome("3L", 28110227, dmel.id, dmelEvidence),
        Chromosome("3R", 32079331, dmel.id, dmelEvidence),
        Chromosome("X", 23542271, dmel.id, dmelEvidence),
        Chromosome("4", 1348131, dmel.id, dmelEvidence),
        Chromosome("Y", 3667352, dmel.id, dmelEvidence)
    )

    val genes = listOf(
        Gene(
            id = "FBgn0004187",
            symbol = "w",
            fullName = "white",
            coordinate = GenomicCoordinate("X", 2741913, 2747761),
            strand = Strand.MINUS,
            annotations = listOf(
                GenomicAnnotation("CDS", GenomicCoordinate("X", 2742000, 2742500), "Exon 1"),
                GenomicAnnotation("CDS", GenomicCoordinate("X", 2742600, 2743000), "Exon 2")
            ),
            description = "Transporter for eye pigment precursors. Mutations cause white eyes.",
            evidence = dmelEvidence
        ),
        Gene(
            id = "FBgn0000490",
            symbol = "dpp",
            fullName = "decapentaplegic",
            coordinate = GenomicCoordinate("2L", 2434567, 2465678),
            strand = Strand.PLUS,
            annotations = listOf(
                GenomicAnnotation("CDS", GenomicCoordinate("2L", 2435000, 2436000), "Coding")
            ),
            description = "Morphogen involved in dorsal-ventral patterning and wing development.",
            evidence = dmelEvidence
        ),
        Gene(
            id = "FBgn0003053",
            symbol = "rut",
            fullName = "rutabaga",
            coordinate = GenomicCoordinate("X", 1234567, 1269876),
            strand = Strand.PLUS,
            annotations = listOf(),
            description = "Adenylyl cyclase. Mutations affect olfactory learning and memory.",
            evidence = dmelEvidence
        )
    )

    fun getGenesForChromosome(chr: String): List<Gene> {
        return genes.filter { it.coordinate.chromosome == chr }
    }
}
