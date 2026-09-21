package com.flylab.domain.development

import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence

// Represents the Genome provided from M8
data class DevelopmentGenome(
    val sequenceSnapshot: String,
    val dominantAlleles: List<String>
)

data class DevelopmentalRules(
    val growthRateFactor: Float,
    val mutationImpact: Float
)

data class SpeciesPriors(
    val baseDevelopmentTime: Float,
    val baseSize: Float,
    val baseNeuronCount: Int
)

data class Constraints(
    val maxSize: Float,
    val minDuration: Float,
    val maxNeurons: Int
)

data class DevelopmentalContext(
    val genome: DevelopmentGenome,
    val rules: DevelopmentalRules,
    val priors: SpeciesPriors,
    val constraints: Constraints
)

data class Provenance(
    val modelVersion: String,
    val dataSources: List<String>,
    val assumptions: List<String>,
    val parentId: String?, // ID of the parent organism, if any
    val randomSeed: Long,
    val genomeSnapshot: String, // Simplified subset or hash of the initiating genome
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MODELED,
        notes = "Rule-based simulated development from synthetic developmental framework.",
        confidence = 0.8f
    )
) : ProvenanceTagged
