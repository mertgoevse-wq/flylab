package com.flylab.domain.timeline

/**
 * Timeline for recording and replaying simulation events.
 * Supports pause, resume, rewind, step, and deterministic replay.
 */
data class Timeline(
    val experimentId: String,
    val startTime: Long,
    val events: List<TimelineEvent> = emptyList(),
    val currentTime: Long = startTime,
    val seed: Long = System.currentTimeMillis()
) {
    fun addEvent(event: TimelineEvent): Timeline =
        copy(events = events + event, currentTime = event.timestamp)

    fun eventsUntil(timestamp: Long): List<TimelineEvent> =
        events.filter { it.timestamp <= timestamp }

    fun eventsInRange(startTime: Long, endTime: Long): List<TimelineEvent> =
        events.filter { it.timestamp in startTime..endTime }
}

sealed class TimelineEvent {
    abstract val timestamp: Long
    abstract val eventType: String
}

data class StimulusEvent(
    override val timestamp: Long,
    val stimulusType: String,
    val parameters: Map<String, Any>
) : TimelineEvent() {
    override val eventType = "stimulus"
}

data class SensorActivationEvent(
    override val timestamp: Long,
    val sensorType: String,
    val activationLevel: Float
) : TimelineEvent() {
    override val eventType = "sensor_activation"
}

data class NeuralActivationEvent(
    override val timestamp: Long,
    val regionId: String?,
    val neuronId: String?,
    val activationLevel: Float
) : TimelineEvent() {
    override val eventType = "neural_activation"
}

data class MotorOutputEvent(
    override val timestamp: Long,
    val motorCommand: String,
    val parameters: Map<String, Any>
) : TimelineEvent() {
    override val eventType = "motor_output"
}

data class BehaviorEvent(
    override val timestamp: Long,
    val behavior: String,
    val parameters: Map<String, Any>
) : TimelineEvent() {
    override val eventType = "behavior"
}

data class RewardEvent(
    override val timestamp: Long,
    val rewardValue: Float,
    val source: String
) : TimelineEvent() {
    override val eventType = "reward"
}

data class PlasticityEvent(
    override val timestamp: Long,
    val synapseId: String,
    val oldWeight: Float,
    val newWeight: Float,
    val cause: String
) : TimelineEvent() {
    override val eventType = "plasticity"
}

data class MemoryUpdateEvent(
    override val timestamp: Long,
    val memoryType: String,
    val update: String
) : TimelineEvent() {
    override val eventType = "memory_update"
}
