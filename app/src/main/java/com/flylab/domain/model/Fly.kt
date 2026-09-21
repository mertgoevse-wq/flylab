package com.flylab.domain.model

enum class BiologicalSex(
    val germanName: String,
    val description: String,
    val connectomeProvenance: ScientificEvidence
) {
    FEMALE(
        germanName = "Weiblich (Adult)",
        description = "Basiert direkt auf dem vollständigen FlyWire-Elektronenmikroskopie-Datensatz (Dorkenwald et al. 2024).",
        connectomeProvenance = ScientificEvidence.FLYWIRE_FEMALE_CONNECTOME_V783
    ),

    MALE(
        germanName = "Männlich (Parametrisiertes Modell)",
        description = "Männliches Gehirn existiert als Modell mit belegten dimorphen Unterschieden (z.B. Fru-Neurone, P1-Cluster), kein erfundenes Voll-Konnektom.",
        connectomeProvenance = ScientificEvidence(
            level = ProvenanceLevel.MODELLED,
            citation = "Cachero et al. (2010) Curr Biol; Auer & Benton (2016) Curr Opin Neurobiol.",
            notes = "Male-specific circuits modeled as param-adjusted dimorphic overlays on reference baseline."
        )
    )
}

/**
 * Complete Fly organism state.
 */
data class Fly(
    override val id: String = "fly_canton_s_01",
    val name: String = "Canton-S Wildtyp (Weiblich)",
    val sex: BiologicalSex = BiologicalSex.FEMALE,
    override val genotype: String = "w1118; +/+",
    val anatomy: FlyAnatomy = FlyAnatomy(),
    val behavioralState: BehavioralState = BehavioralState(),
    val neuromodulators: Map<NeuromodulatorType, NeuromodulatorState> = NeuromodulatorType.entries.associateWith {
        NeuromodulatorState(type = it, concentration = 0.15f, baseline = 0.15f)
    },
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        citation = "FlyBase Consortium (2024); Dorkenwald et al. (2024)",
        notes = "Standard laboratory wild-type Drosophila melanogaster female specimen."
    )
) : Subject {
    override val species: String = "Drosophila melanogaster"
    init {
        // Enforce biological integrity rules
        if (sex == BiologicalSex.MALE) {
            require(evidence.level != ProvenanceLevel.MEASURED) {
                "Scientific integrity violation: Male whole-brain connectome cannot be marked MEASURED (CLAUDE.md)."
            }
        }
    }
}
