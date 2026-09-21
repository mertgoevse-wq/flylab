package com.flylab.domain.model

/**
 * Foundation for multi-organism experiments.
 * Represents any biological subject (e.g. Drosophila melanogaster, C. elegans, etc.)
 * participating in a scientific simulation or experiment.
 */
interface Subject : ProvenanceTagged {
    val id: String
    val species: String
    val genotype: String
}
