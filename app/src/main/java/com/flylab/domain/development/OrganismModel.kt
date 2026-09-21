package com.flylab.domain.development

import java.util.UUID

data class Organism(
    val id: UUID = UUID.randomUUID(),
    val trajectory: DevelopmentalTrajectory,
    val anatomy: DevelopmentalAnatomy,
    val neuralStructure: NeuralStructure,
    val provenance: Provenance
)

data class DevelopmentalTrajectory(
    val stages: List<DevelopmentalStage>,
    val totalDurationHours: Float
)

data class DevelopmentalStage(
    val name: String, // e.g., "Embryo", "Larva", "Pupa", "Adult"
    val durationHours: Float,
    val milestones: List<String>
)

data class DevelopmentalAnatomy(
    val sizeMillimeters: Float,
    val weightMilligrams: Float,
    val traits: Map<String, String> // Phenotypic expressions (e.g. eyeColor = red)
)

data class NeuralStructure(
    val neuronCount: Int,
    val synapseCount: Int,
    val connectomeHash: String // Compact hash matching the neural wiring state
)
