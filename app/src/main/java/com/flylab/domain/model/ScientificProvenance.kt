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
    OBSERVED("OBSERVED", "BEOBACHTET", "Beobachtet, aber nicht quantifiziert", true),
    MEASURED("MEASURED", "GEMESSEN", "Experimentell gemessen", true),
    PUBLISHED("PUBLISHED", "PUBLIZIERT", "In Fachliteratur publiziert", true),
    EXPERIMENTAL("EXPERIMENTAL", "EXPERIMENTELL", "Experimentell belegt", true),
    CURATED("CURATED", "KURATIERT", "Kuratiert aus Literatur", true),
    IMPORTED("IMPORTED", "IMPORTIERT", "Aus externem Datensatz importiert", true),
    DERIVED("DERIVED", "ABGELEITET", "Aus Messdaten mathematisch berechnet", true),
    MODELLED("MODELLED", "MODELLIERT", "Berechnetes Modell", false),
    SIMULATED("SIMULATED", "SIMULIERT", "Während der Simulation berechnet", false),
    INFERRED("INFERRED", "GESCHLUSSFOLGERT", "Logisch oder indirekt abgeleitet", false),
    PREDICTED("PREDICTED", "VORHERGESAGT", "Vorhergesagt durch Modell", false),
    HYPOTHETICAL("HYPOTHETICAL", "HYPOTHETISCH", "Wissenschaftliche Annahme ohne Daten", false),
    UNKNOWN("UNKNOWN", "UNBEKANNT", "Herkunft unbekannt", false);

    companion object {
        fun fromString(value: String): ProvenanceLevel {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.canonicalName.equals(value, ignoreCase = true) ||
                it.germanName.contains(value, ignoreCase = true)
            } ?: UNKNOWN
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

        val FLYWIRE_CIRCUIT_MOTIF_DERIVED = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Dorkenwald et al., Nature 2024; Schlegel et al., Nature 2024",
            doi = "10.1038/s41586-024-07558-y",
            datasetSource = "FlyWire Connectome Graph Mining v783",
            datasetVersion = "v783",
            notes = "Circuit wiring topology extracted from whole-brain connectome graph.",
            confidence = 0.92f
        )

        val KENYON_CELL_SPARSE_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "Caron et al., Nature 2013; Li et al., eLife 2020",
            doi = "10.1038/nature12063",
            datasetSource = "FlyLab Mushroom Body Computational Model",
            datasetVersion = "1.0",
            notes = "Representative 16-neuron subset modeling random sparse claw projection of ~2,000 biological Kenyon cells. Simplified for real-time simulation.",
            confidence = 0.75f
        )

        val PREMOTOR_STEERING_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "Rayshubskiy et al., bioRxiv 2020; Hulse et al., eLife 2021",
            datasetSource = "FlyLab Premotor Steering Model",
            datasetVersion = "1.0",
            notes = "Simplified mapping from Central Complex and Lateral Horn to bilateral turning and forward walk.",
            confidence = 0.70f
        )

        val DOPAMINE_PLASTICITY_MATH_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "Aso et al., eLife 2014; Hige et al., Nature 2015; Handler et al., Cell 2019",
            doi = "10.7554/eLife.04577",
            datasetSource = "FlyLab 3-Factor Plasticity Rule",
            datasetVersion = "1.0",
            notes = "Continuous three-factor long-term depression equation modulating KC->MBON synaptic efficacy.",
            confidence = 0.85f
        )

        val SENSORY_TRANSDUCTION_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "Benton et al., Cell 2009; Steck et al., eLife 2012",
            datasetSource = "FlyLab Chemotaxis Model",
            datasetVersion = "1.0",
            notes = "Bilateral 2D Gaussian plume odor sampling across 0.35 mm inter-antennal separation.",
            confidence = 0.80f
        )

        val SIMULATED_RUNTIME_OVERLAY = ScientificEvidence(
            level = ProvenanceLevel.SIMULATED,
            citation = "FlyLab Simulation Engine",
            notes = "Transient dynamic state, firing rate, or synaptic weight offset calculated during active execution.",
            confidence = 1.0f
        )

        val RATE_BASED_SIMULATION_MODEL = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "FlyLab Neural Dynamics Core v1.0",
            notes = "Abstract population rate dynamics with sigmoid activation and membrane leak time constant.",
            confidence = 0.70f
        )

        val HYPOTHETICAL_PERTURBATION = ScientificEvidence(
            level = ProvenanceLevel.HYPOTHETICAL,
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
