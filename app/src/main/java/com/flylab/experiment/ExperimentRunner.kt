package com.flylab.experiment

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.NeuropilId
import com.flylab.sim.SimulationEngine

/**
 * Orchestrates experiment execution, event detection, and generates finalized journal entries.
 */
class ExperimentRunner(
    val configuration: ExperimentConfiguration
) {
    val engine = SimulationEngine(
        initialFly = (configuration.subject as com.flylab.domain.model.Fly),
        initialEnvironment = configuration.initialEnvironment,
        seed = configuration.seed
    )

    private val recordedEvents = mutableListOf<ExperimentEvent>()
    private var lastRecordedBehavior: BehaviorType = (configuration.subject as com.flylab.domain.model.Fly).behavioralState.activeBehavior
    private var lastOdorConcentrationReported: Float = 0.0f
    private var lastSucroseEncountered: Boolean = false

    init {
        // Apply experimental perturbations
        for ((region, factor) in configuration.regionPerturbations) {
            engine.setRegionPerturbation(region, factor)
            recordedEvents.add(
                ExperimentEvent.PerturbationActive(
                    timeSeconds = 0.0f,
                    region = region,
                    factor = factor
                )
            )
        }
    }

    /**
     * Executes the experiment for the configured duration and returns the completed journal entry.
     */
    fun runTrial(): ExperimentJournalEntry {
        val totalStepsNeeded = (configuration.durationSeconds / configuration.dtSeconds).toInt()
        val behaviorCounts = mutableMapOf<BehaviorType, Int>()
        val regionActivationSums = mutableMapOf<NeuropilId, Float>()

        for (stepIdx in 1..totalStepsNeeded) {
            val snapshot = engine.step(configuration.dtSeconds)
            val currentBehavior = snapshot.activeBehavior

            behaviorCounts[currentBehavior] = (behaviorCounts[currentBehavior] ?: 0) + 1

            // Track region activations
            for ((region, state) in snapshot.regionStates) {
                regionActivationSums[region] = (regionActivationSums[region] ?: 0.0f) + state.meanActivation
            }

            // Detect behavior transition
            if (currentBehavior != lastRecordedBehavior) {
                recordedEvents.add(
                    ExperimentEvent.BehaviorChange(
                        timeSeconds = snapshot.timeSeconds,
                        previousBehavior = lastRecordedBehavior,
                        newBehavior = currentBehavior,
                        cause = "Neuronale Neubewertung / Gradientenänderung"
                    )
                )
                lastRecordedBehavior = currentBehavior
            }

            // Detect odor detection onset
            val odorConc = snapshot.sensoryInput.meanOdorConcentration
            if (odorConc > 0.15f && lastOdorConcentrationReported < 0.15f) {
                recordedEvents.add(
                    ExperimentEvent.OdorEncounter(
                        timeSeconds = snapshot.timeSeconds,
                        odorType = snapshot.sensoryInput.activeOdor,
                        concentration = odorConc
                    )
                )
            }
            lastOdorConcentrationReported = odorConc

            // Detect sucrose contact
            val sucrose = snapshot.sensoryInput.sucroseContact
            if (sucrose > 0.2f && !lastSucroseEncountered) {
                recordedEvents.add(
                    ExperimentEvent.SucroseReward(
                        timeSeconds = snapshot.timeSeconds,
                        concentration = sucrose
                    )
                )
                lastSucroseEncountered = true
            } else if (sucrose < 0.1f) {
                lastSucroseEncountered = false
            }
        }

        // Calculate statistics
        val totalSteps = engine.currentStep
        val behaviorDistribution = BehaviorType.entries.associateWith { type ->
            val count = behaviorCounts[type] ?: 0
            if (totalSteps > 0) (count.toFloat() / totalSteps.toFloat()) * 100.0f else 0.0f
        }

        val meanRegionActivations = NeuropilId.entries.associateWith { region ->
            val sum = regionActivationSums[region] ?: 0.0f
            if (totalSteps > 0) (sum / totalSteps.toFloat()).coerceIn(0.0f, 1.0f) else 0.0f
        }

        val weightChanges = engine.currentSynapticOverlays.mapValues { it.value.deltaWeight }

        val outcome = generateOutcomeSummary(
            behaviorDistribution = behaviorDistribution,
            distanceTraveled = engine.currentFly.behavioralState.distanceTraveledMm,
            foodConsumed = engine.currentFly.behavioralState.foodConsumedUnits,
            weightChanges = weightChanges
        )

        return ExperimentJournalEntry(
            id = "trial_${configuration.id}_${System.currentTimeMillis()}",
            configuration = configuration,
            totalSteps = totalSteps,
            totalSimulatedSeconds = engine.currentTimeSeconds,
            totalDistanceTraveledMm = engine.currentFly.behavioralState.distanceTraveledMm,
            foodConsumedUnits = engine.currentFly.behavioralState.foodConsumedUnits,
            behaviorDistribution = behaviorDistribution,
            meanRegionActivations = meanRegionActivations,
            finalSynapticWeightChanges = weightChanges,
            events = recordedEvents,
            outcomeSummary = outcome
        )
    }

    private fun generateOutcomeSummary(
        behaviorDistribution: Map<BehaviorType, Float>,
        distanceTraveled: Float,
        foodConsumed: Float,
        weightChanges: Map<String, Float>
    ): String {
        val dominantBehavior = behaviorDistribution.maxByOrNull { it.value }?.key ?: BehaviorType.EXPLORING
        val plasticChanges = weightChanges.filter { Math.abs(it.value) > 0.01f }

        return buildString {
            append("Versuch erfolgreich abgeschlossen (${String.format("%.1f", engine.currentTimeSeconds)} s simuliert). ")
            append("Dominantes Verhalten: ${dominantBehavior.germanName} (${String.format("%.1f%%", behaviorDistribution[dominantBehavior] ?: 0f)}). ")
            append("Zurückgelegte Wegstrecke: ${String.format("%.1f", distanceTraveled)} mm. ")
            if (foodConsumed > 0.01f) {
                append("Nahrungsaufnahme: ${String.format("%.2f", foodConsumed)} Einheiten. ")
            }
            if (plasticChanges.isNotEmpty()) {
                append("${plasticChanges.size} synaptische Verbindungen im Pilzkörper plastisch modifiziert.")
            }
        }
    }

    companion object {
        data class ComparisonResult(
            val trialAId: String,
            val trialBId: String,
            val distanceDifferenceMm: Float,
            val foodDifferenceUnits: Float,
            val primaryDivergenceSummary: String
        )

        /**
         * Compares two experiment trials side-by-side (EXPERIMENT_MODEL.md § 7).
         */
        fun compareTrials(trialA: ExperimentJournalEntry, trialB: ExperimentJournalEntry): ComparisonResult {
            val distDiff = trialB.totalDistanceTraveledMm - trialA.totalDistanceTraveledMm
            val foodDiff = trialB.foodConsumedUnits - trialA.foodConsumedUnits

            val diffSummary = buildString {
                append("Vergleich: '${trialA.configuration.title}' vs. '${trialB.configuration.title}'. ")
                if (Math.abs(distDiff) > 1.0f) {
                    append("Streckendifferenz: ${String.format("%+.1f", distDiff)} mm. ")
                }
                if (Math.abs(foodDiff) > 0.01f) {
                    append("Nahrungsdifferenz: ${String.format("%+.2f", foodDiff)} Einheiten. ")
                }
            }

            return ComparisonResult(
                trialAId = trialA.id,
                trialBId = trialB.id,
                distanceDifferenceMm = distDiff,
                foodDifferenceUnits = foodDiff,
                primaryDivergenceSummary = diffSummary
            )
        }
    }
}
