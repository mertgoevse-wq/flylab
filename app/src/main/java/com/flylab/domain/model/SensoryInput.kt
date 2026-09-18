package com.flylab.domain.model

/**
 * Types of odorants and chemical cues encountered by the fly.
 */
enum class OdorType(
    val scientificName: String,
    val germanName: String,
    val naturalValence: Float, // [-1.0 (aversive) .. +1.0 (appetitive)]
    val relevantGlomerulus: String,
    val evidence: ScientificEvidence
) {
    APPLE_CIDER_VINEGAR(
        scientificName = "Acetic acid / Fermenting fruit odor",
        germanName = "Gärungsgeruch (Apfelessig)",
        naturalValence = 0.85f,
        relevantGlomerulus = "DM1 / DM4",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Semmelhack & Wang (2009) Nature 459:218-223.",
            notes = "Strongly appetitive food odor activating projection neurons via DM1/DM4."
        )
    ),

    ETHYL_BUTYRATE(
        scientificName = "Ethyl butyrate (Fruity ester)",
        germanName = "Fruchtester (Ethylbutyrat)",
        naturalValence = 0.75f,
        relevantGlomerulus = "DM2",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hallem & Carlson (2006) Cell 125:143-160.",
            notes = "Broadly activates multiple olfactory receptor types with net positive attraction."
        )
    ),

    GEOSMIN(
        scientificName = "Geosmin (Harmful mold odor)",
        germanName = "Geosmin (Schimmelpilz-Toxin)",
        naturalValence = -0.95f,
        relevantGlomerulus = "DA2",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Stensmyr et al. (2012) Cell 151:1345-1357.",
            notes = "Dedicated ultra-selective line for toxic microbial avoidance; activates DA2 glomerulus."
        )
    ),

    BENZALDEHYDE(
        scientificName = "Benzaldehyde (Almond scent / Repellent at high conc)",
        germanName = "Benzaldehyd",
        naturalValence = -0.60f,
        relevantGlomerulus = "DL5",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "de Bruyne et al. (2001) Neuron 30:537-552.",
            notes = "Commonly used conditioning odorant with mild innate aversion at laboratory concentrations."
        )
    ),

    CITRONELLAL(
        scientificName = "Citronellal (Insect repellent)",
        germanName = "Citronellal (Insektenrepellent)",
        naturalValence = -0.80f,
        relevantGlomerulus = "DC3",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Kwon et al. (2010) Curr Biol 20:1672-1679.",
            notes = "TRP channel and olfactory receptor mediated avoidance."
        )
    );

    companion object {
        fun defaultAppetitive() = APPLE_CIDER_VINEGAR
        fun defaultAversive() = GEOSMIN
    }
}

/**
 * Continuous sensory stimuli impinging upon the fly at a given time step.
 * Continuous values, NEVER boolean flags (SIMULATION_MODEL.md § 1.4).
 */
data class SensoryInput(
    val odorLeftAntenna: Float = 0.0f,     // Odor concentration at left antenna [0.0 .. 1.0]
    val odorRightAntenna: Float = 0.0f,    // Odor concentration at right antenna [0.0 .. 1.0]
    val activeOdor: OdorType = OdorType.APPLE_CIDER_VINEGAR,
    val lightIntensity: Float = 0.5f,      // Ambient luminance [0.0 (dark) .. 1.0 (bright daylight)]
    val lightAngleRadians: Float = 0.0f,   // Angle of light source relative to heading (-PI to +PI)
    val windVelocityMmS: Float = 0.0f,     // Mechanosensory airflow velocity
    val temperatureCelsius: Float = 24.0f, // Ambient temperature (24°C is physiological optimum)
    val sucroseContact: Float = 0.0f,      // Proboscis/tarsal gustatory sugar concentration [0.0 .. 1.0]
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.DERIVED,
        citation = "FlyLab Sensory Transduction Pipeline",
        notes = "Continuous bilateral sensory gradients computed from simulated 2D/3D arena positions."
    )
) : ProvenanceTagged {
    init {
        require(odorLeftAntenna in 0.0f..1.0f) { "Left odor concentration must be in [0, 1]" }
        require(odorRightAntenna in 0.0f..1.0f) { "Right odor concentration must be in [0, 1]" }
        require(lightIntensity in 0.0f..1.0f) { "Light intensity must be in [0, 1]" }
        require(sucroseContact in 0.0f..1.0f) { "Sucrose contact must be in [0, 1]" }
    }

    /**
     * Bilateral odor difference for tropotactic steering.
     * Positive = higher concentration on right antenna.
     */
    val odorGradientBilateral: Float
        get() = odorRightAntenna - odorLeftAntenna

    /**
     * Mean odor concentration across both antennae.
     */
    val meanOdorConcentration: Float
        get() = (odorLeftAntenna + odorRightAntenna) * 0.5f

    /**
     * Thermal stress penalty: Drosophila preferred temperature is ~24-25°C.
     * Returns a stress value in [0.0 .. 1.0].
     */
    val thermalStress: Float
        get() {
            val delta = Math.abs(temperatureCelsius - 24.5f)
            return (delta / 12.0f).coerceIn(0.0f, 1.0f)
        }
}
