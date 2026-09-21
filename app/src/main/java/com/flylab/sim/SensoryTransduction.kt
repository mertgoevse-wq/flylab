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

        // Simple visual sensor model (optic flow proxy / object detection)
        var opticFlowLeft = 0.0f
        var opticFlowRight = 0.0f
        for (obj in env.visualObjects) {
            val dx = obj.posXmm - posX
            val dy = obj.posYmm - posY
            val dist = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            if (dist > 0.1f && dist < 100.0f) {
                val angleToObject = Math.atan2(dy.toDouble(), dx.toDouble()).toFloat()
                var relativeAngle = angleToObject - heading
                while (relativeAngle > Math.PI) relativeAngle -= (2 * Math.PI).toFloat()
                while (relativeAngle < -Math.PI) relativeAngle += (2 * Math.PI).toFloat()

                val visualMagnitude = (obj.radiusMm / dist).coerceIn(0.0f, 1.0f) * (1.1f - obj.luminance) // High value for contrasting dark objects against bright bg
                if (relativeAngle in 0.0f..Math.PI.toFloat()) {
                    opticFlowLeft += visualMagnitude
                } else if (relativeAngle in -Math.PI.toFloat()..0.0f) {
                    opticFlowRight += visualMagnitude
                }
            }
        }

        return SensoryInput(
            odorLeftAntenna = concLeft,
            odorRightAntenna = concRight,
            activeOdor = activeOdorType,
            lightIntensity = env.ambientLuminance,
            lightAngleRadians = 0.0f,
            temperatureCelsius = env.ambientTemperatureCelsius,
            sucroseContact = sucrose,
            opticFlowLeft = opticFlowLeft.coerceIn(0.0f, 1.0f),
            opticFlowRight = opticFlowRight.coerceIn(0.0f, 1.0f)
        )
    }
}
