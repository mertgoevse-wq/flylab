package com.flylab.domain

/**
 * Scientific evidence classification for all biological data and models.
 * Every piece of biological information must be tagged with its evidence level.
 */
enum class EvidenceLevel {
    /**
     * Directly measured experimental data from this implementation's own sensors/logs
     */
    MEASURED,

    /**
     * Data from peer-reviewed scientific literature (e.g., FlyWire connectome)
     */
    PUBLISHED,

    /**
     * Computationally derived from measured/published data using validated methods
     */
    DERIVED,

    /**
     * Simulated behavior using computational models (e.g., neural dynamics)
     */
    MODELED,

    /**
     * Hypothetical mechanism with no experimental validation
     */
    HYPOTHESIS,

    /**
     * Unknown or unverified
     */
    UNKNOWN;

    fun isScientificallyGrounded(): Boolean = when(this) {
        MEASURED, PUBLISHED, DERIVED -> true
        MODELED, HYPOTHESIS, UNKNOWN -> false
    }
}
