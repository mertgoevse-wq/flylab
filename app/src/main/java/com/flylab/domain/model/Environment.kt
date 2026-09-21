package com.flylab.domain.model

/**
 * A food patch or odor plume source placed within the experimental arena.
 */
data class OdorSource(
    val id: String,
    val odorType: OdorType,
    val posXmm: Float,
    val posYmm: Float,
    val emissionRate: Float = 1.0f,     // Peak concentration at center [0.0 .. 1.0]
    val plumeSigmaMm: Float = 20.0f,    // Diffusion spread parameter (standard deviation)
    val sucroseConcentration: Float = 1.0f // [0.0 .. 1.0] for gustatory reward on contact
) {
    /**
     * Continuous concentration calculation at coordinate (x, y) via 2D Gaussian diffusion.
     */
    fun concentrationAt(x: Float, y: Float): Float {
        val dx = x - posXmm
        val dy = y - posYmm
        val distSq = dx * dx + dy * dy
        val exponent = -distSq / (2.0f * plumeSigmaMm * plumeSigmaMm)
        return (emissionRate * Math.exp(exponent.toDouble())).toFloat().coerceIn(0.0f, 1.0f)
    }
}

/**
 * Experimental environment / arena settings.
 */
data class Environment(
    val arenaRadiusMm: Float = 45.0f, // Standard 90 mm diameter Petri dish or circular arena
    val ambientTemperatureCelsius: Float = 24.5f,
    val ambientLuminance: Float = 0.6f, // [0.0 (dark) .. 1.0 (bright)]
    val sources: List<OdorSource> = listOf(
        OdorSource(
            id = "food_source_vinegar",
            odorType = OdorType.APPLE_CIDER_VINEGAR,
            posXmm = 20.0f,
            posYmm = 15.0f,
            emissionRate = 0.95f,
            plumeSigmaMm = 18.0f,
            sucroseConcentration = 0.8f
        )
    ),
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.PUBLISHED,
        citation = "Simon & Dickinson (2010) J Exp Biol; Steck et al. (2012) eLife.",
        notes = "Standard circular behavioral arena with continuous chemical plume gradient."
    )
) : ProvenanceTagged {
    fun sampleOdorAt(x: Float, y: Float, odorType: OdorType): Float {
        return sources
            .filter { it.odorType == odorType }
            .sumOf { it.concentrationAt(x, y).toDouble() }
            .toFloat()
            .coerceIn(0.0f, 1.0f)
    }

    fun sampleSucroseAt(x: Float, y: Float, contactDistanceMm: Float = 3.0f): Float {
        val closestSource = sources.find {
            Math.hypot((x - it.posXmm).toDouble(), (y - it.posYmm).toDouble()) <= contactDistanceMm
        }
        return closestSource?.sucroseConcentration ?: 0.0f
    }
}
