package com.flylab.domain.model

/**
 * Scientific evidence classifications as strictly mandated by FlyLab CLAUDE.md,
 * SCIENTIFIC_MODEL.md, and DATA_MODEL.md.
 *
 * Every biological mechanism, structure, and parameter MUST carry an unambiguous
 * provenance tag. Hypotheses must NEVER be presented as experimentally measured.
 */
enum class ProvenanceLevel(
    val canonicalName: String,
    val germanName: String,
    val description: String,
    val isEmpiricallyVerified: Boolean
) {
    /**
     * Directly measured in laboratory experiments on Drosophila melanogaster.
     * Example: FlyWire full female connectome synaptic count or reconstructed morphology.
     */
    MEASURED(
        canonicalName = "MEASURED",
        germanName = "BELEGT (Gemessen)",
        description = "Experimentell direkt am adulten Drosophila-Präparat gemessen.",
        isEmpiricallyVerified = true
    ),

    /**
     * Published in peer-reviewed scientific literature with verifiable DOI/citations.
     * Example: Kenyon cell odor response sparseness in mushroom body (Turner et al. 2008).
     */
    PUBLISHED(
        canonicalName = "PUBLISHED",
        germanName = "BELEGT (Literatur)",
        description = "In begutachteter Fachliteratur publiziert und repliziert.",
        isEmpiricallyVerified = true
    ),

    /**
     * Analytically or computationally derived from measured/published datasets.
     * Example: Synaptic projection density or pathway summaries calculated from connectome graphs.
     */
    DERIVED(
        canonicalName = "DERIVED",
        germanName = "ABGELEITET",
        description = "Aus Messdaten mathematisch oder graphentheoretisch berechnet.",
        isEmpiricallyVerified = true
    ),

    /**
     * Simplified mathematical or computational model inspired by empirical data.
     * Example: Leaky integrate-and-fire rate dynamics, dopamine-mediated plasticity formula.
     */
    MODELED(
        canonicalName = "MODELED",
        germanName = "MODELLIERT",
        description = "Vereinfachtes Rechenmodell, von biologischen Prinzipien inspiriert.",
        isEmpiricallyVerified = false
    ),

    /**
     * Unverified scientific hypothesis or parameter without empirical support.
     * Example: Novel uncharacterized odor receptor mutations, synthetic pharmacology.
     */
    HYPOTHESIS(
        canonicalName = "HYPOTHESIS",
        germanName = "HYPOTHETISCH",
        description = "Wissenschaftliche Annahme oder offene Frage ohne belastbare Messdaten.",
        isEmpiricallyVerified = false
    );

    companion object {
        fun fromString(value: String): ProvenanceLevel {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.canonicalName.equals(value, ignoreCase = true) ||
                it.germanName.contains(value, ignoreCase = true)
            } ?: MODELED
        }
    }
}

/**
 * Metadata detailing the experimental or literature provenance of a biological entity.
 */
data class ScientificEvidence(
    val level: ProvenanceLevel,
    val citation: String? = null,
    val doi: String? = null,
    val datasetSource: String? = null,
    val datasetVersion: String? = null,
    val notes: String = "",
    val isAiGenerated: Boolean = false,
    val confidence: Float = 1.0f
) {
    init {
        require(confidence in 0.0f..1.0f) { "Confidence must be within [0.0, 1.0], was $confidence" }
    }

    companion object {
        val FLYWIRE_FEMALE_CONNECTOME_V783 = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Dorkenwald et al., Nature 2024 / FlyWire Consortium",
            doi = "10.1038/s41586-024-07558-y",
            datasetSource = "FlyWire Whole-Brain Connectome (Adult Female Drosophila melanogaster)",
            datasetVersion = "v783",
            notes = "Full electron microscopy connectome reconstruction of an adult female brain.",
            confidence = 0.99f
        )

        val DROSOPHILA_OLFACTORY_CIRCUIT = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Benton et al., Cell 2009; Su et al., Annu. Rev. Genet. 2009",
            doi = "10.1016/j.cell.2009.01.022",
            datasetSource = "Antennal Lobe Glomerular Mapping",
            datasetVersion = "2009.1",
            notes = "Olfactory receptor neurons (ORNs) project stereotypic glomeruli to projection neurons (PNs).",
            confidence = 0.95f
        )

        val MUSHROOM_BODY_LEARNING = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Aso et al., eLife 2014; Hige et al., Nature 2015",
            doi = "10.7554/eLife.04577",
            datasetSource = "Mushroom Body Output Network",
            datasetVersion = "2014-2015",
            notes = "Dopaminergic neurons (DANs) drive associative synaptic depression between Kenyon cells and MBONs.",
            confidence = 0.95f
        )

        val RATE_BASED_SIMULATION_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELED,
            citation = "FlyLab Neural Dynamics Core v1.0",
            notes = "Abstract population rate dynamics with sigmoid activation and membrane leak time constant.",
            confidence = 0.70f
        )

        val HYPOTHETICAL_PERTURBATION = ScientificEvidence(
            level = ProvenanceLevel.HYPOTHESIS,
            notes = "User-configured experimental perturbation without empirical calibration.",
            confidence = 0.30f
        )
    }
}

/**
 * Interface guaranteeing that a biological or simulation object carries verifiable provenance.
 */
interface ProvenanceTagged {
    val evidence: ScientificEvidence
    val provenanceLevel: ProvenanceLevel get() = evidence.level
}
