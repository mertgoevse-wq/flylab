package com.flylab.simulation

import com.flylab.domain.brain.Brain
import com.flylab.domain.connectome.Connectome
import com.flylab.domain.memory.MemorySystem
import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.Environment
import com.flylab.domain.model.Fly
import com.flylab.domain.model.MotorCommand
import com.flylab.domain.model.OdorType
import com.flylab.domain.model.SensoryInput
import com.flylab.domain.neural.NeuralActivity
import com.flylab.domain.neural.RegionalActivity
import com.flylab.domain.plasticity.PlasticityEngine
import com.flylab.domain.plasticity.RewardModulatedHebbian
import com.flylab.domain.simulation.SimulationConfig
import com.flylab.domain.simulation.SimulationState
import com.flylab.domain.timeline.Timeline
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Core simulation engine.
 * Implements the Mobile Deterministic Simulation Pattern.
 * Runs neural activity, behavioral loop, and sensory updates at configurable levels of detail.
 */
class SimulationEngine(
    private val config: SimulationConfig
) {
    private val random = Random(config.seed)
    private val plasticityEngine = PlasticityEngine(
        rule = RewardModulatedHebbian(config.learningRate)
    )

    fun createInitialState(
        brain: Brain,
        connectome: Connectome,
        fly: Fly = Fly(),
        environment: Environment = Environment()
    ): SimulationState {
        return SimulationState(
            simulationId = generateId(),
            timestamp = 0L,
            fly = fly,
            environment = environment,
            brain = brain,
            connectome = connectome,
            neuralActivity = null,
            memorySystem = MemorySystem(),
            timeline = Timeline(
                experimentId = generateId(),
                startTime = System.currentTimeMillis(),
                seed = config.seed
            ),
            levelOfDetail = config.levelOfDetail
        )
    }

    /**
     * Steps the entire simulation by config.timeStep.
     */
    fun step(state: SimulationState): SimulationState {
        if (state.isPaused) return state

        val dtSeconds = config.timeStep / 1000.0f

        // 1. Sensory Phase
        val sensoryInput = computeSensoryInput(state.fly, state.environment)

        // 2. Neural/Brain Phase (LOD 1)
        val regionActivities = computeRegionalActivity(state, sensoryInput)
        val activity = RegionalActivity(
            timestamp = state.timestamp + config.timeStep.toLong(),
            regionActivities = regionActivities
        )

        // 3. Needs/Drive Phase (Motivational)
        val currentNeeds = state.fly.behavioralState.needs

        // 4. Behavioral & Motor Phase
        val nextBehavior = selectBehavior(sensoryInput, currentNeeds)
        val motorCommand = executeBehavior(nextBehavior, sensoryInput)

        // 5. Spatial Update
        val updatedBehavioralState = state.fly.behavioralState.copy(
            activeBehavior = nextBehavior
        ).updateSpatial(motorCommand, dtSeconds, state.environment.arenaRadiusMm)

        val updatedFly = state.fly.copy(
            behavioralState = updatedBehavioralState
        )

        return state.copy(
            timestamp = state.timestamp + config.timeStep.toLong(),
            neuralActivity = activity,
            fly = updatedFly
        )
    }

    private fun computeSensoryInput(fly: Fly, environment: Environment): SensoryInput {
        val posX = fly.behavioralState.posXmm
        val posY = fly.behavioralState.posYmm
        val heading = fly.behavioralState.headingRadians

        // Offset antennae positions relative to heading
        val antennaOffset = 1.0f // 1mm
        val leftX = posX + cos(heading + (PI / 4.0).toFloat()) * antennaOffset
        val leftY = posY + sin(heading + (PI / 4.0).toFloat()) * antennaOffset

        val rightX = posX + cos(heading - (PI / 4.0).toFloat()) * antennaOffset
        val rightY = posY + sin(heading - (PI / 4.0).toFloat()) * antennaOffset

        // Simple single odor track for now
        val activeOdor = OdorType.APPLE_CIDER_VINEGAR
        val leftConcentration = environment.sampleOdorAt(leftX, leftY, activeOdor)
        val rightConcentration = environment.sampleOdorAt(rightX, rightY, activeOdor)

        val sucrose = environment.sampleSucroseAt(posX, posY)

        return SensoryInput(
            odorLeftAntenna = leftConcentration,
            odorRightAntenna = rightConcentration,
            activeOdor = activeOdor,
            lightIntensity = environment.ambientLuminance,
            temperatureCelsius = environment.ambientTemperatureCelsius,
            sucroseContact = sucrose
        )
    }

    private fun selectBehavior(sensory: SensoryInput, needs: com.flylab.domain.model.PhysiologicalNeeds): BehaviorType {
        if (sensory.sucroseContact > 0.5f && needs.hunger > 0.2f) {
            return BehaviorType.FEEDING
        }

        if (sensory.thermalStress > 0.5f) {
            return BehaviorType.AVOIDING
        }

        if (sensory.meanOdorConcentration > 0.1f && sensory.activeOdor.naturalValence > 0f) {
            return BehaviorType.APPROACHING
        }

        if (sensory.meanOdorConcentration > 0.1f && sensory.activeOdor.naturalValence < 0f) {
            return BehaviorType.AVOIDING
        }

        if (needs.fatigue > 0.8f) {
            return BehaviorType.RESTING
        }

        return BehaviorType.EXPLORING
    }

    private fun executeBehavior(behavior: BehaviorType, sensory: SensoryInput): MotorCommand {
        return when (behavior) {
            BehaviorType.RESTING, BehaviorType.FEEDING, BehaviorType.GROOMING -> {
                MotorCommand.IDLE.copy(proboscisExtension = if (behavior == BehaviorType.FEEDING) 1.0f else 0.0f)
            }
            BehaviorType.EXPLORING -> {
                // Stochastic exploration
                val turn = (random.nextFloat() * 2f - 1f) * 1.5f
                MotorCommand(forwardVelocityMmS = 18.0f, angularVelocityRadS = turn)
            }
            BehaviorType.APPROACHING -> {
                // Tropotactic steering: turn towards higher concentration
                val turn = sensory.odorGradientBilateral * -10.0f
                MotorCommand(forwardVelocityMmS = 25.0f, angularVelocityRadS = turn)
            }
            BehaviorType.AVOIDING -> {
                MotorCommand(forwardVelocityMmS = -5.0f, angularVelocityRadS = 3.0f) // Back up and turn
            }
            BehaviorType.ORIENTING -> {
                val turn = if (sensory.odorGradientBilateral < 0) 5.0f else -5.0f
                MotorCommand(forwardVelocityMmS = 0.0f, angularVelocityRadS = turn)
            }
        }
    }

    private fun computeRegionalActivity(state: SimulationState, sensory: SensoryInput): Map<String, Float> {
        // Map sensory input to regional activity for LOD 1
        val regions = mutableMapOf<String, Float>()
        state.brain.regions.forEach { region ->
            // Baseline
            var act = 0.1f + random.nextFloat() * 0.05f

            // Excite antennal lobe based on odor
            if (region.id.contains("AL") || region.id.contains("antennal")) {
                act += sensory.meanOdorConcentration * 0.8f
            }

            // Excite mushroom body if hungry and sensing food
            if (region.id.contains("MB") && state.fly.behavioralState.needs.hunger > 0.5f) {
                act += sensory.meanOdorConcentration * 0.5f
            }

            regions[region.id] = act.coerceIn(0.0f, 1.0f)
        }
        return regions
    }

    private fun generateId(): String {
        return "sim_${System.currentTimeMillis()}_${random.nextInt(10000)}"
    }
}
