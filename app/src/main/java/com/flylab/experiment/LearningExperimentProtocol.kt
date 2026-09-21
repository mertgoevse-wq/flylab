package com.flylab.experiment

import com.flylab.domain.model.NeuropilId

/**
 * Executes a prolonged, multi-stage learning experiment.
 */
class LearningExperimentProtocol(
    val baseConfiguration: ExperimentConfiguration
) {
    /**
     * Runs an acquisition phase followed by an extinction/testing phase.
     */
    fun runConditioningAndExtinction(): Pair<ExperimentJournalEntry, ExperimentJournalEntry> {
        // 1. Acquisition (Conditioning) Phase - Odor paired with Reward
        val conditioningEnv = EnvironmentFactory.createOpenFieldNavigation()
        val conditioningConfig = baseConfiguration.copy(
            id = baseConfiguration.id + "_conditioning",
            title = "${baseConfiguration.title} - Konditionierung",
            initialEnvironment = conditioningEnv,
            durationSeconds = 15.0f
        )
        val conditioningRunner = ExperimentRunner(conditioningConfig)
        val conditioningJournal = conditioningRunner.runTrial()
        
        // Extract the modified fly state (with updated plastic synapses) from end of conditioning
        val conditionedFly = conditioningRunner.engine.currentFly
        val conditionedSynapses = conditioningRunner.engine.currentSynapticOverlays
        
        // 2. Extinction (Testing) Phase - Odor without Reward (sucrose removed)
        val extinctionEnv = conditioningEnv.copy(
            sources = conditioningEnv.sources.map { it.copy(sucroseConcentration = 0.0f) }
        )
        val extinctionConfig = baseConfiguration.copy(
            id = baseConfiguration.id + "_extinction",
            title = "${baseConfiguration.title} - Löschung/Test",
            initialEnvironment = extinctionEnv,
            subject = conditionedFly,         // Carry over state
            durationSeconds = 15.0f
        )
        val extinctionRunner = ExperimentRunner(extinctionConfig)
        
        // Manually inject the plastic overlays into the new runner engine before starting
        // (In a full architecture, these overlays would be persisted on the Fly/Brain domain object,
        // but currently reside in engine state alongside the rate arrays).
        extinctionRunner.engine.restoreSnapshot(
            extinctionRunner.engine.history.last().copy(
                synapticOverlays = conditionedSynapses
            )
        )
        
        val extinctionJournal = extinctionRunner.runTrial()
        
        return Pair(conditioningJournal, extinctionJournal)
    }
}
