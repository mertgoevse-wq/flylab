package com.flylab.experiment

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.OdorType
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence

/**
 * Concrete observable events emitted during the simulation run.
 */
sealed class ExperimentEvent(
    val timeSeconds: Float,
    val description: String
) {
    class OdorEncounter(
        timeSeconds: Float,
        val odorType: OdorType,
        val concentration: Float
    ) : ExperimentEvent(
        timeSeconds,
        "Geruchsreiz wahrgenommen: ${odorType.germanName} (Konzentration: ${String.format("%.2f", concentration)})"
    )

    class BehaviorChange(
        timeSeconds: Float,
        val previousBehavior: BehaviorType,
        val newBehavior: BehaviorType,
        val cause: String
    ) : ExperimentEvent(
        timeSeconds,
        "Verhaltenswechsel: ${previousBehavior.germanName} -> ${newBehavior.germanName} (Ursache: $cause)"
    )

    class SucroseReward(
        timeSeconds: Float,
        val concentration: Float
    ) : ExperimentEvent(
        timeSeconds,
        "Zuckerkontakt am Rüssel (Konzentration: ${String.format("%.2f", concentration)}) -> Aktivierung PAM-Dopamincluster"
    )

    class SynapticPlasticityEvent(
        timeSeconds: Float,
        val synapseId: String,
        val deltaWeight: Float,
        val cause: String
    ) : ExperimentEvent(
        timeSeconds,
        "Synaptische Plastizität ($synapseId): Änderung um ${String.format("%+.3f", deltaWeight)} ($cause)"
    )

    class PerturbationActive(
        timeSeconds: Float,
        val region: NeuropilId,
        val factor: Float
    ) : ExperimentEvent(
        timeSeconds,
        "Simulationseingriff aktiv: ${region.germanName} auf ${String.format("%.0f%%", factor * 100)} gesetzt"
    )
}

/**
 * Complete, reproducible record of an experimental trial for the laboratory journal.
 * Matches EXPERIMENT_MODEL.md § 6 requirements.
 */
data class ExperimentJournalEntry(
    val id: String,
    val configuration: ExperimentConfiguration,
    val timestampCreatedMs: Long = System.currentTimeMillis(),
    val totalSteps: Long,
    val totalSimulatedSeconds: Float,
    val totalDistanceTraveledMm: Float,
    val foodConsumedUnits: Float,
    val behaviorDistribution: Map<BehaviorType, Float>, // Percentage of duration in [0 .. 100]
    val meanRegionActivations: Map<NeuropilId, Float>,
    val finalSynapticWeightChanges: Map<String, Float>,
    val events: List<ExperimentEvent>,
    val outcomeSummary: String,
    val userNotes: String = "",
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MODELLED,
        citation = "FlyLab Experiment Journal Engine",
        notes = "Empirical in-silico simulation journal entry with verifiable causal chains."
    )
) : ProvenanceTagged
