package com.flylab.experiment

import com.flylab.domain.model.Environment
import com.flylab.domain.model.OdorSource
import com.flylab.domain.model.OdorType

/**
 * Factory for creating standardized experimental environments.
 */
object EnvironmentFactory {
    
    fun createOpenFieldNavigation(): Environment {
        return Environment(
            arenaRadiusMm = 50.0f,
            sources = listOf(
                OdorSource(
                    id = "central_vinegar",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = 0.0f,
                    posYmm = 0.0f,
                    emissionRate = 1.0f,
                    plumeSigmaMm = 30.0f,
                    sucroseConcentration = 1.0f
                )
            )
        )
    }

    fun createTwoChoiceDiscrimination(): Environment {
        return Environment(
            arenaRadiusMm = 40.0f,
            sources = listOf(
                OdorSource(
                    id = "reward_zone_left",
                    odorType = OdorType.APPLE_CIDER_VINEGAR,
                    posXmm = -20.0f,
                    posYmm = 20.0f,
                    emissionRate = 0.8f,
                    plumeSigmaMm = 15.0f,
                    sucroseConcentration = 1.0f
                ),
                OdorSource(
                    id = "aversive_zone_right",
                    odorType = OdorType.GEOSMIN,
                    posXmm = 20.0f,
                    posYmm = 20.0f,
                    emissionRate = 0.8f,
                    plumeSigmaMm = 15.0f,
                    sucroseConcentration = 0.0f
                )
            )
        )
    }
    
    fun createReversalLearningArena(): Environment {
        return Environment(
            arenaRadiusMm = 45.0f,
            sources = listOf(
                OdorSource(
                    id = "zone_a",
                    odorType = OdorType.OCTANOL,
                    posXmm = -25.0f,
                    posYmm = 0.0f,
                    emissionRate = 0.8f,
                    plumeSigmaMm = 15.0f,
                    sucroseConcentration = 1.0f
                ),
                OdorSource(
                    id = "zone_b",
                    odorType = OdorType.METHYLCYCLOHEXANOL,
                    posXmm = 25.0f,
                    posYmm = 0.0f,
                    emissionRate = 0.8f,
                    plumeSigmaMm = 15.0f,
                    sucroseConcentration = 0.0f
                )
            )
        )
    }
}
