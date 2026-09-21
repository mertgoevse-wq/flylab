package com.flylab.domain.graph

import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.NeuropilId

/**
 * Base interface for the interactive scientific graph architecture (M14).
 * Enforces provenance tracking on every node.
 */
sealed interface GraphNode : ProvenanceTagged {
    val id: String
    val label: String
}

data class OrganismNode(
    override val id: String,
    override val label: String,
    val species: String,
    val sex: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        notes = "Organism graph node representation."
    )
) : GraphNode

data class BrainRegionNode(
    override val id: String,
    override val label: String,
    val neuropilId: NeuropilId,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        notes = "Brain region graph node."
    )
) : GraphNode

data class NeuronNode(
    override val id: String,
    override val label: String,
    val typeName: String,
    val transmitter: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MEASURED,
        notes = "Neuron graph node from connectome."
    )
) : GraphNode

data class SynapseNode(
    override val id: String,
    override val label: String,
    val weight: Float,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.DERIVED,
        notes = "Synapse graph node computed from EM reconstruction."
    )
) : GraphNode

data class MoleculeNode(
    override val id: String,
    override val label: String,
    val formula: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.CURATED,
        notes = "Molecule graph node."
    )
) : GraphNode

data class ReceptorNode(
    override val id: String,
    override val label: String,
    val gene: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        notes = "Receptor protein graph node."
    )
) : GraphNode

data class PathwayNode(
    override val id: String,
    override val label: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.INFERRED,
        notes = "Functional pathway graph node."
    )
) : GraphNode

data class InterventionNode(
    override val id: String,
    override val label: String,
    val interventionType: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.EXPERIMENTAL,
        notes = "Experimental intervention graph node."
    )
) : GraphNode

data class ExperimentNode(
    override val id: String,
    override val label: String,
    val hypothesis: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.OBSERVED,
        notes = "Empirical experiment graph node."
    )
) : GraphNode

data class DataSourceNode(
    override val id: String,
    override val label: String,
    val doi: String,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.IMPORTED,
        notes = "External data source origin node."
    )
) : GraphNode

/**
 * Directed edge representing relationships in the biological graph.
 */
data class GraphEdge(
    val sourceId: String,
    val targetId: String,
    val relationType: String,
    override val evidence: ScientificEvidence
) : ProvenanceTagged
