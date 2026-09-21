package com.flylab.experiment

import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.NeuropilId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExperimentRunnerTest {

    @Test
    fun `standard food seeking protocol executes and produces complete journal entry`() {
        val config = ExperimentConfiguration.standardFoodSeeking(seed = 555L).copy(durationSeconds = 2.0f)
        val runner = ExperimentRunner(config)

        val journal = runner.runTrial()

        assertNotNull(journal)
        assertTrue(journal.totalSteps > 0)
        assertEquals(2.0f, journal.totalSimulatedSeconds, 0.1f)
        assertTrue("Distance traveled should be positive", journal.totalDistanceTraveledMm > 0.0f)
        assertTrue("Events should be recorded during trial", journal.events.isNotEmpty())

        val totalPercent = journal.behaviorDistribution.values.sum()
        assertEquals(100.0f, totalPercent, 1.0f)
        assertNotNull(journal.outcomeSummary)
    }

    @Test
    fun `antennal lobe silencing perturbation is recorded in journal entry`() {
        val config = ExperimentConfiguration.standardALSilencing(seed = 444L).copy(durationSeconds = 1.5f)
        val runner = ExperimentRunner(config)

        val journal = runner.runTrial()

        val perturbationEvents = journal.events.filterIsInstance<ExperimentEvent.PerturbationActive>()
        assertTrue("Perturbation event must be logged", perturbationEvents.isNotEmpty())
        assertEquals(NeuropilId.ANTENNAL_LOBE, perturbationEvents.first().region)
        assertEquals(0.2f, perturbationEvents.first().factor, 0.001f)
    }

    @Test
    fun `trial comparison computes divergence metrics`() {
        val configA = ExperimentConfiguration.standardFoodSeeking(seed = 101L).copy(durationSeconds = 1.0f)
        val configB = ExperimentConfiguration.standardALSilencing(seed = 101L).copy(durationSeconds = 1.0f)

        val runnerA = ExperimentRunner(configA)
        val runnerB = ExperimentRunner(configB)

        val trialA = runnerA.runTrial()
        val trialB = runnerB.runTrial()

        val comparison = ExperimentRunner.compareTrials(trialA, trialB)
        assertNotNull(comparison)
        assertEquals(trialA.id, comparison.trialAId)
        assertEquals(trialB.id, comparison.trialBId)
        assertTrue(comparison.primaryDivergenceSummary.isNotEmpty())
    }
}
