package com.flylab.sim

import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.OdorType
import com.flylab.domain.model.SensoryInput

/**
 * Computes realistic sensory inputs from fly position/heading within the environment.
 */
object SensoryTransduction {

    const val ANTENNAL_OFFSET_MM = 0.35f // Distance from head center to antenna tip
    const val ANTENNAL_ANGLE_OFFSET_RAD = 0.52f // ~30 degrees left/right of center axis

    /**
     * Samples the environment at the physical sensor locations of the fly.
     */
    fun transduce(fly: Fly, env: Environment): SensoryInput {
        val posX = fly.behavioralState.posXmm
        val posY = fly.behavioralState.posYmm
        val heading = fly.behavioralState.headingRadians

        // Calculate left antenna spatial position in arena
        val leftAngle = heading + ANTENNAL_ANGLE_OFFSET_RAD
        val leftAntennaX = posX + (Math.cos(leftAngle.toDouble()).toFloat() * ANTENNAL_OFFSET_MM)
        val leftAntennaY = posY + (Math.sin(leftAngle.toDouble()).toFloat() * ANTENNAL_OFFSET_MM)

        // Calculate right antenna spatial position in arena
        val rightAngle = heading - ANTENNAL_ANGLE_OFFSET_RAD
        val rightAntennaX = posX + (Math.cos(rightAngle.toDouble()).toFloat() * ANTENNAL_OFFSET_MM)
        val rightAntennaY = posY + (Math.sin(rightAngle.toDouble()).toFloat() * ANTENNAL_OFFSET_MM)

        // Active odor: sample primary odor from first source or default
        val primarySource = env.sources.firstOrNull()
        val activeOdorType = primarySource?.odorType ?: OdorType.APPLE_CIDER_VINEGAR

        val concLeft = env.sampleOdorAt(leftAntennaX, leftAntennaY, activeOdorType)
        val concRight = env.sampleOdorAt(rightAntennaX, rightAntennaY, activeOdorType)

        // Gustatory sucrose contact at proboscis
        val proboscisX = posX + (Math.cos(heading.toDouble()).toFloat() * 0.2f)
        val proboscisY = posY + (Math.sin(heading.toDouble()).toFloat() * 0.2f)
        val sucrose = env.sampleSucroseAt(proboscisX, proboscisY, contactDistanceMm = 2.5f)

        return SensoryInput(
            odorLeftAntenna = concLeft,
            odorRightAntenna = concRight,
            activeOdor = activeOdorType,
            lightIntensity = env.ambientLuminance,
            lightAngleRadians = 0.0f,
            temperatureCelsius = env.ambientTemperatureCelsius,
            sucroseContact = sucrose
        )
    }
}
