package com.flylab.domain.development

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class OrganismDevelopmentEngineTest {

    private val context = DevelopmentalContext(
        genome = DevelopmentGenome(
            sequenceSnapshot = "ATCGATCGATCG",
            dominantAlleles = listOf("w-")
        ),
        rules = DevelopmentalRules(
            growthRateFactor = 1.0f,
            mutationImpact = 0.5f
        ),
        priors = SpeciesPriors(
            baseDevelopmentTime = 240.0f, // 10 days
            baseSize = 2.5f,
            baseNeuronCount = 139000
        ),
        constraints = Constraints(
            maxSize = 3.0f,
            minDuration = 200.0f,
            maxNeurons = 150000
        )
    )

    private val engine = DefaultDevelopmentEngine()

    @Test
    fun developmentalTrajectoryIsDeterministicUnderSeed() {
        val organism1 = engine.develop(context, seed = 42L)
        val organism2 = engine.develop(context, seed = 42L)

        assertEquals(organism1.trajectory.totalDurationHours, organism2.trajectory.totalDurationHours, 0.001f)
        assertEquals(organism1.anatomy.sizeMillimeters, organism2.anatomy.sizeMillimeters, 0.001f)
        assertEquals(organism1.neuralStructure.neuronCount, organism2.neuralStructure.neuronCount)
    }

    @Test
    fun differentSeedsProduceDifferentOrganisms() {
        val organism1 = engine.develop(context, seed = 42L)
        val organism2 = engine.develop(context, seed = 99L)

        assertNotEquals(organism1.trajectory.totalDurationHours, organism2.trajectory.totalDurationHours, 0.0001f)
    }

    @Test
    fun genomeMappedPhenotypesAreExpressed() {
        val organism = engine.develop(context, seed = 42L)
        assertEquals("white", organism.anatomy.traits["eyeColor"])
        assertEquals("wild-type", organism.anatomy.traits["wingShape"])
    }
}
