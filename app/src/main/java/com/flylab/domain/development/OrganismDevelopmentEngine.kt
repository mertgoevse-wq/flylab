package com.flylab.domain.development

import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.model.Organ
import com.flylab.domain.model.OrganSystemType
import com.flylab.domain.model.AnatomicalRegion
import com.flylab.domain.model.PhysiologicalState
import java.util.Random

interface OrganismDevelopmentEngine {
    fun develop(context: DevelopmentalContext, seed: Long, parentId: String? = null): Organism
}

class DefaultDevelopmentEngine(
    private val modelVersion: String = "M9-v1.0"
) : OrganismDevelopmentEngine {

    override fun develop(context: DevelopmentalContext, seed: Long, parentId: String?): Organism {
        val random = Random(seed)

        val trajectory = generateTrajectory(context, random)
        val anatomy = generateAnatomy(context, random)
        val neuralStructure = generateNeuralStructure(context, random)

        val provenance = Provenance(
            modelVersion = modelVersion,
            dataSources = listOf("FlyLab Core Priors", "Synthetic Genomes v1"),
            assumptions = listOf("Deterministic growth given seed", "Linear trait scaling"),
            parentId = parentId,
            randomSeed = seed,
            genomeSnapshot = context.genome.sequenceSnapshot.take(50), // Snapshot
            evidence = ScientificEvidence(
                level = ProvenanceLevel.MODELLED,
                notes = "Synthetic embryonic development driven by $modelVersion via random seed $seed.",
                confidence = 0.8f
            )
        )

        return Organism(
            trajectory = trajectory,
            anatomy = anatomy,
            neuralStructure = neuralStructure,
            provenance = provenance
        )
    }

    private fun generateTrajectory(context: DevelopmentalContext, random: Random): DevelopmentalTrajectory {
        val baseTime = context.priors.baseDevelopmentTime * context.rules.growthRateFactor
        val variation = 1.0f + (random.nextFloat() * 0.2f - 0.1f) // +/- 10% deviation
        var finalTime = baseTime * variation
        if (finalTime < context.constraints.minDuration) finalTime = context.constraints.minDuration

        return DevelopmentalTrajectory(
            stages = listOf(
                DevelopmentalStage("Embryo", finalTime * 0.1f, listOf("Gastrulation", "Segmentation")),
                DevelopmentalStage("Larva", finalTime * 0.4f, listOf("Instar 1", "Instar 2", "Instar 3")),
                DevelopmentalStage("Pupa", finalTime * 0.4f, listOf("Metamorphosis")),
                DevelopmentalStage("Adult", finalTime * 0.1f, listOf("Eclosion", "Maturation"))
            ),
            totalDurationHours = finalTime
        )
    }

    private fun generateAnatomy(context: DevelopmentalContext, random: Random): DevelopmentalAnatomy {
        val sizeVar = 1.0f + (random.nextFloat() * 0.1f - 0.05f)
        var finalSize = context.priors.baseSize * sizeVar
        if (finalSize > context.constraints.maxSize) finalSize = context.constraints.maxSize

        val wingShape = if (context.genome.dominantAlleles.contains("dpp-")) "vestigial" else "wild-type"
        val eyeColor = if (context.genome.dominantAlleles.contains("w-")) "white" else "red"

        val ev = ScientificEvidence(level = ProvenanceLevel.MODELLED, notes = "Synthetic organ generated during development.", confidence = 0.8f)
        val organs = listOf(
            Organ("org_digestive_1", "Midgut", OrganSystemType.DIGESTIVE, AnatomicalRegion.ABDOMEN, PhysiologicalState(), ev),
            Organ("org_resp_1", "Tracheal Tubes", OrganSystemType.RESPIRATORY, AnatomicalRegion.THORAX, PhysiologicalState(), ev),
            Organ("org_circ_1", "Dorsal Vessel (Heart)", OrganSystemType.CIRCULATORY, AnatomicalRegion.ABDOMEN, PhysiologicalState(), ev),
            Organ("org_repro_1", "Ovaries", OrganSystemType.REPRODUCTIVE, AnatomicalRegion.ABDOMEN, PhysiologicalState(), ev),
            Organ("org_nervous_1", "Ventral Nerve Cord", OrganSystemType.NERVOUS, AnatomicalRegion.THORAX, PhysiologicalState(), ev)
        )

        return DevelopmentalAnatomy(
            sizeMillimeters = finalSize,
            weightMilligrams = finalSize * 1.5f,
            traits = mapOf("wingShape" to wingShape, "eyeColor" to eyeColor),
            organs = organs
        )
    }

    private fun generateNeuralStructure(context: DevelopmentalContext, random: Random): NeuralStructure {
        val baseNeurons = context.priors.baseNeuronCount
        var neurons = baseNeurons + random.nextInt(1000)
        if (neurons > context.constraints.maxNeurons) neurons = context.constraints.maxNeurons

        return NeuralStructure(
            neuronCount = neurons,
            synapseCount = neurons * (10 + random.nextInt(20)),
            connectomeHash = "conn_${random.nextInt(10000)}"
        )
    }
}
