package com.flylab.experiment

import com.flylab.domain.model.Subject
import com.flylab.domain.model.Environment
import com.flylab.domain.model.ProvenanceTagged

/**
 * Universal scientific experiment abstraction facilitating in-silico simulation
 * reproducibility, scalable to multiple subjects and differing environments.
 */
interface Experiment : ProvenanceTagged {
    val id: String
    val title: String
    val hypothesis: String
    val subjects: List<Subject>
    val environment: Environment
    
    // Reproducibility constraints
    val datasetVersion: String
    val modelVersion: String
    val seed: Long
}
