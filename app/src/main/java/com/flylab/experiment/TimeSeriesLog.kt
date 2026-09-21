package com.flylab.experiment

import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ProvenanceTagged
import com.flylab.domain.model.ScientificEvidence

/**
 * Reusable time-series logging architecture (M14) capable of handling
 * high-frequency neural, behavioral, and environmental events.
 * Designed to scale for Multi-Fly/Swarm iterations without UI rewrites.
 */
data class TimeSeriesLog(
    val entityId: String,          // Subject ID or Environment ID
    val metricName: String,
    val units: String,
    val timestamps: FloatArray,    // Simulation seconds
    val values: FloatArray,
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.SIMULATED,
        notes = "Time-series logged during simulation."
    )
) : ProvenanceTagged {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TimeSeriesLog
        return entityId == other.entityId && metricName == other.metricName
    }
    
    override fun hashCode(): Int {
        var result = entityId.hashCode()
        result = 31 * result + metricName.hashCode()
        return result
    }
}

class EventStore {
    private val timeSeriesData = mutableMapOf<Pair<String, String>, MutableList<Pair<Float, Float>>>()
    
    fun logMetric(entityId: String, metricName: String, timeSeconds: Float, value: Float) {
        val key = Pair(entityId, metricName)
        val list = timeSeriesData.getOrPut(key) { mutableListOf() }
        list.add(Pair(timeSeconds, value))
    }
    
    fun getSeries(entityId: String, metricName: String, units: String = ""): TimeSeriesLog {
        val list = timeSeriesData[Pair(entityId, metricName)] ?: emptyList()
        val timestamps = FloatArray(list.size) { list[it].first }
        val values = FloatArray(list.size) { list[it].second }
        return TimeSeriesLog(entityId, metricName, units, timestamps, values)
    }
    
    fun getAllSeries(unitsMap: Map<String, String> = emptyMap()): List<TimeSeriesLog> {
        return timeSeriesData.map { (key, list) ->
            val (entityId, metricName) = key
            val timestamps = FloatArray(list.size) { list[it].first }
            val values = FloatArray(list.size) { list[it].second }
            val units = unitsMap[metricName] ?: ""
            TimeSeriesLog(entityId, metricName, units, timestamps, values)
        }
    }
}
