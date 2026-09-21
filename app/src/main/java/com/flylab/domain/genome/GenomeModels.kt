package com.flylab.domain.genome

import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence

/**
 * Represents a species in the FlyLab simulation.
 */
data class Species(
    val id: String,
    val scientificName: String,
    val commonName: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

/**
 * A chromosome belonging to a species.
 */
data class Chromosome(
    val name: String, // e.g., 2L, 2R, 3L, 3R, X, 4, Y
    val lengthBp: Long,
    val speciesId: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

/**
 * A coordinate range on a specific chromosome.
 */
data class GenomicCoordinate(
    val chromosome: String,
    val startBp: Long,
    val endBp: Long
) {
    val length: Long get() = endBp - startBp + 1
}

enum class Strand { PLUS, MINUS }

/**
 * Annotation types like CDS, exon, enhancer, etc.
 */
data class GenomicAnnotation(
    val type: String,
    val coordinate: GenomicCoordinate,
    val label: String
)

/**
 * Represents a specific gene definition.
 */
data class Gene(
    val id: String,
    val symbol: String,
    val fullName: String,
    val coordinate: GenomicCoordinate,
    val strand: Strand,
    val annotations: List<GenomicAnnotation>,
    val description: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

enum class VariantType { SNP, INSERTION, DELETION, STRUCTURAL }

/**
 * A known or applied genetic variant.
 */
data class Variant(
    val id: String,
    val coordinate: GenomicCoordinate,
    val referenceAllele: String,
    val alternateAllele: String,
    val type: VariantType,
    val functionalImpact: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

/**
 * Pathway models mapping the genomic change to functional cell biology.
 */
data class PathwayEffect(
    val pathwayName: String,
    val functionalImpact: String,
    val severity: Float, // 0.0 to 1.0
    override val evidence: ScientificEvidence
) : ProvenanceTagged

/**
 * Phenotype mapped from a genetic state/variant.
 */
data class PhenotypePrediction(
    val trait: String,
    val effectDescription: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged

/**
 * A mutation experiment manipulating a specific variant.
 */
data class MutationExperiment(
    val targetGene: Gene,
    val appliedVariant: Variant,
    val pathwayEffects: List<PathwayEffect>,
    val predictedPhenotypes: List<PhenotypePrediction>,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.SIMULATED,
        notes = "Live simulated mutation experiment mapping variant to phenotype.",
        confidence = 1.0f
    )
) : ProvenanceTagged

/**
 * Provides access to simplified sequence data strings.
 * For a real app, this would stream from a local DB/API. For the simulation slice,
 * it returns small substrings.
 */
data class SequenceChunk(
    val coordinate: GenomicCoordinate,
    val sequenceData: String
)
