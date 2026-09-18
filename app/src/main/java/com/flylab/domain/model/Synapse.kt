package com.flylab.domain.model

/**
 * Immutable reference synapse from measured connectome data (e.g. FlyWire v783).
 *
 * MANDATORY RULE (DATA_MODEL.md § 3):
 * Reference connectome data is NEVER mutated by the simulation.
 * All dynamic changes during learning or experiments reside exclusively in SynapseSimulationOverlay.
 */
data class SynapseReference(
    val synapseId: String,
    val preNeuronId: String,
    val postNeuronId: String,
    val neurotransmitter: NeuromodulatorType,
    val baselineWeight: Float, // Empirical synaptic count or normalized baseline weight
    val isExcitatory: Boolean = neurotransmitter != NeuromodulatorType.GABA,
    val isPlastic: Boolean = false, // True for KC -> MBON synapses
    override val evidence: ScientificEvidence = ScientificEvidence.FLYWIRE_FEMALE_CONNECTOME_V783
) : ProvenanceTagged {
    init {
        require(baselineWeight >= 0f) { "Baseline weight must be non-negative" }
    }
}

/**
 * Dynamic simulation overlay representing plastic changes during the simulation run.
 */
data class SynapseSimulationOverlay(
    val synapseId: String,
    val baselineWeight: Float,
    val currentWeight: Float,
    val deltaWeight: Float = currentWeight - baselineWeight,
    val lastUpdateStep: Long = 0L,
    val lastUpdateTimestampMs: Long = 0L,
    val lastPlasticityCause: String = "INITIAL_STATE",
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MODELED,
        citation = "FlyLab Plasticity Engine",
        notes = "Synaptic weight change calculated through dopamine/octopamine gated plasticity rule."
    )
) : ProvenanceTagged {
    init {
        require(currentWeight >= 0f) { "Synaptic weight cannot be negative" }
    }

    val relativeChangePercent: Float
        get() = if (baselineWeight > 0.0001f) ((currentWeight - baselineWeight) / baselineWeight) * 100.0f else 0.0f

    fun updateWeight(newWeight: Float, step: Long, timestampMs: Long, cause: String): SynapseSimulationOverlay {
        val clampedWeight = newWeight.coerceAtLeast(0.0f)
        return copy(
            currentWeight = clampedWeight,
            deltaWeight = clampedWeight - baselineWeight,
            lastUpdateStep = step,
            lastUpdateTimestampMs = timestampMs,
            lastPlasticityCause = cause
        )
    }
}
