package com.flylab.domain.model

/**
 * Neurotransmitter and Neuromodulator types in Drosophila melanogaster.
 *
 * CRITICAL SCIENTIFIC INTEGRITY RULE:
 * Drosophila does NOT possess mammalian endorphin or opioid reward architecture.
 * Reward and reinforcement are mediated by octopamine and specialized dopamine clusters (PAM/PPL1).
 */
enum class NeuromodulatorType(
    val scientificName: String,
    val germanName: String,
    val primaryFunction: String,
    val defaultDecayHalfLifeMs: Float, // Rate of reuptake / enzymatic degradation
    val evidence: ScientificEvidence
) {
    OCTOPAMINE(
        scientificName = "Octopamine (OA)",
        germanName = "Octopamin",
        primaryFunction = "Invertebraten-spezifisches Signal für Appetenz, Erregung, Flugbereitschaft & Hunger.",
        defaultDecayHalfLifeMs = 450.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Burke et al. (2012) Nature 492:433-437; Roeder (1999) Prog Neurobiol.",
            notes = "Essential for appetitive learning and arousal in insects; invertebrate analog of noradrenaline."
        )
    ),

    DOPAMINE(
        scientificName = "Dopamine (DA)",
        germanName = "Dopamin",
        primaryFunction = "Zwei getrennte Signalwege: PPL1 vermittelt aversive Reize, PAM vermittelt Belohnung/Zucker.",
        defaultDecayHalfLifeMs = 300.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Aso et al. (2012) Neuron 68:943-958; Liu et al. (2012) Nature.",
            notes = "PAM cluster drives positive reinforcement; PPL1 cluster drives negative/aversive reinforcement."
        )
    ),

    SEROTONIN(
        scientificName = "Serotonin (5-HT)",
        germanName = "Serotonin",
        primaryFunction = "Moduliert Schlaf/Wach-Zustand, Aggression, Lokomotionsdämpfung und Nahrungsaufnahme.",
        defaultDecayHalfLifeMs = 600.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Yuan et al. (2006) Cell 127:1271-1283; Dierick & Greenspan (2007) Nat Genet.",
            notes = "Regulates locomotor activity levels and sleep homeostasis."
        )
    ),

    ACETYLCHOLINE(
        scientificName = "Acetylcholine (ACh)",
        germanName = "Acetylcholin",
        primaryFunction = "Hauptsächlicher schneller erregender Neurotransmitter im ZNS der Fliege (z.B. ORNs, PNs).",
        defaultDecayHalfLifeMs = 50.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Lee & O'Dowd (1999) J Neurosci 19:5311-5321.",
            notes = "Fast nicotinic and muscarinic transmission in central sensory and interneuron pathways."
        )
    ),

    GABA(
        scientificName = "Gamma-aminobutyric acid (GABA)",
        germanName = "GABA",
        primaryFunction = "Wichtigster inhibitorischer Botenstoff im Fliegengehirn (z.B. APL-Neuron für Musterverfeinerung).",
        defaultDecayHalfLifeMs = 70.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Lin et al. (2014) Cell Rep 7:1272-1282.",
            notes = "Provides broad recurrent feedback inhibition across Kenyon cells in the mushroom body."
        )
    ),

    TYRAMINE(
        scientificName = "Tyramine (TA)",
        germanName = "Tyramin",
        primaryFunction = "Biochemische Vorstufe von Octopamin; eigene neuromodulatorische Wirkung auf Motorik.",
        defaultDecayHalfLifeMs = 500.0f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Schützler et al. (2019) J Neurogenet 33:143-154.",
            notes = "Involved in motor control and behavioral transitions."
        )
    );

    companion object {
        /**
         * Validates that forbidden mammalian endorphin claims are rejected.
         */
        fun assertValidDrosophilaModulator(name: String) {
            val forbidden = listOf("endorphin", "enkephalin", "dynorphin", "morphine")
            if (forbidden.any { name.contains(it, ignoreCase = true) }) {
                throw IllegalArgumentException(
                    "Biological integrity violation: '$name' is an endorphin/opioid concept from mammalian biology. " +
                    "Drosophila reinforcement uses Octopamine and Dopamine (CLAUDE.md)."
                )
            }
        }
    }
}

/**
 * Concentration levels of neuromodulators at a specific brain region or synapse.
 */
data class NeuromodulatorState(
    val type: NeuromodulatorType,
    val concentration: Float = 0.0f, // [0.0 (baseline) .. 1.0 (saturation)]
    val baseline: Float = 0.1f,
    override val evidence: ScientificEvidence = type.evidence
) : ProvenanceTagged {
    init {
        require(concentration in 0.0f..1.0f) { "Concentration must be in [0, 1], was $concentration" }
        require(baseline in 0.0f..1.0f) { "Baseline must be in [0, 1], was $baseline" }
    }

    fun stepDecay(dtSeconds: Float): NeuromodulatorState {
        val halfLifeSeconds = type.defaultDecayHalfLifeMs / 1000.0f
        val decayRate = Math.pow(0.5, (dtSeconds / halfLifeSeconds).toDouble()).toFloat()
        val newConc = baseline + (concentration - baseline) * decayRate
        return copy(concentration = newConc.coerceIn(0.0f, 1.0f))
    }

    fun inject(amount: Float): NeuromodulatorState {
        return copy(concentration = (concentration + amount).coerceIn(0.0f, 1.0f))
    }
}
