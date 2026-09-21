package com.flylab.experiment

import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.OdorSource
import com.flylab.domain.model.OdorType
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence

/**
 * Standard experiment paradigms as defined in EXPERIMENT_MODEL.md § 2.
 */
enum class ExperimentType(
    val germanTitle: String,
    val description: String
) {
    ODOR_SOURCE_PLACEMENT(
        germanTitle = "Geruchsquellen & Chemotaxis",
        description = "Geruchsstoffe (z.B. Apfelessig oder Geosmin) platzieren und Anziehungs-/Meidungsverhalten untersuchen."
    ),

    ASSOCIATIVE_CONDITIONING(
        germanTitle = "Assoziative Konditionierung (Lernen)",
        description = "Kombination von Geruchsreiz mit Zuckerbelohnung zur Untersuchung der Pilzkörper-Plastizität."
    ),

    BRAIN_REGION_PERTURBATION(
        germanTitle = "Gehirnbereich-Störung (Simulationseingriff)",
        description = "Gezieltes Dämpfen oder Abschalten von Neuropilen (z.B. Antennallobus oder Pilzkörper)."
    ),

    ENVIRONMENT_CHANGE(
        germanTitle = "Umwelt- & Temperaturveränderung",
        description = "Simulation von Hitzestress, Beleuchtungswechseln oder Trockenheit auf die Lokomotion."
    )
}

/**
 * Full configuration needed to start and reproduce an experiment deterministically.
 */
data class ExperimentConfiguration(
    val id: String,
    val type: ExperimentType,
    val title: String,
    val researchQuestion: String,
    val hypothesis: String = "",
    val seed: Long = 42L,
    val initialFly: Fly = Fly(),
    val initialEnvironment: Environment = Environment(),
    val regionPerturbations: Map<NeuropilId, Float> = emptyMap(),
    val durationSeconds: Float = 10.0f,
    val dtSeconds: Float = 0.05f,
    val datasetVersion: String = "FlyWire v783-slice1",
    val modelVersion: String = "FlyLab SimCore v1.0",
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MODELED,
        citation = "FlyLab Experiment Protocol Specification",
        notes = "Standardized in-silico Drosophila behavioral assay protocol."
    )
) : ProvenanceTagged {

    companion object {
        /**
         * Standard protocol 1: Chemotactic attraction to vinegar food odor.
         */
        fun standardFoodSeeking(seed: Long = 101L): ExperimentConfiguration {
            val env = Environment(
                sources = listOf(
                    OdorSource(
                        id = "vinegar_patch",
                        odorType = OdorType.APPLE_CIDER_VINEGAR,
                        posXmm = 18.0f,
                        posYmm = 12.0f,
                        emissionRate = 0.9f,
                        plumeSigmaMm = 20.0f,
                        sucroseConcentration = 0.8f
                    )
                )
            )
            return ExperimentConfiguration(
                id = "exp_food_seeking",
                type = ExperimentType.ODOR_SOURCE_PLACEMENT,
                title = "Nahrungssuche im Essigduft-Gradienten",
                researchQuestion = "Wie orientiert sich die Fliege im diffusen Geruchsgradienten von gärendem Obst?",
                hypothesis = "Bilateraler Antennengradient aktiviert DM1-Glomeruli und steuert Vorwärtslokomotion zur Quelle.",
                seed = seed,
                initialEnvironment = env,
                durationSeconds = 12.0f
            )
        }

        /**
         * Standard protocol 2: Innate avoidance of toxic mold odor (Geosmin).
         */
        fun standardToxinAvoidance(seed: Long = 202L): ExperimentConfiguration {
            val env = Environment(
                sources = listOf(
                    OdorSource(
                        id = "geosmin_mold",
                        odorType = OdorType.GEOSMIN,
                        posXmm = 12.0f,
                        posYmm = 8.0f,
                        emissionRate = 1.0f,
                        plumeSigmaMm = 22.0f,
                        sucroseConcentration = 0.0f
                    )
                )
            )
            return ExperimentConfiguration(
                id = "exp_geosmin_avoidance",
                type = ExperimentType.ODOR_SOURCE_PLACEMENT,
                title = "Angeborene Fluchtreaktion auf Schimmelpilz (Geosmin)",
                researchQuestion = "Löst Geosmin über DA2-Projektionsneuronen und das Laterale Horn Fluchtverhalten aus?",
                hypothesis = "Aversive Stimulation führt zur Ausrichtung weg von der Quelle und schnellerem Sprint.",
                seed = seed,
                initialEnvironment = env,
                durationSeconds = 10.0f
            )
        }

        /**
         * Standard protocol 3: Antennal lobe optogenetic silencing perturbation.
         */
        fun standardALSilencing(seed: Long = 303L): ExperimentConfiguration {
            val base = standardFoodSeeking(seed)
            return base.copy(
                id = "exp_al_silenced",
                type = ExperimentType.BRAIN_REGION_PERTURBATION,
                title = "Optogenetische Dämpfung des Antennallobus",
                researchQuestion = "Verliert die Fliege ihre zielgerichtete Chemotaxis bei 80% Dämpfung des Antennallobus?",
                hypothesis = "Ohne intakte Glomerulus-Übertragung bricht das Orientierungsverhalten zusammen.",
                regionPerturbations = mapOf(NeuropilId.ANTENNAL_LOBE to 0.2f),
                evidence = ScientificEvidence(
                    level = ProvenanceLevel.MODELED,
                    notes = "Simulationseingriff: gezielte künstliche Dämpfung neuronaler Übertragung."
                )
            )
        }
    }
}
