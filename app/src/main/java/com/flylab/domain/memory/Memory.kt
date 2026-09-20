package com.flylab.domain.memory

/**
 * Memory subsystem tracking learned associations and experiences.
 */
data class MemorySystem(
    val shortTermMemory: MutableMap<String, MemoryTrace> = mutableMapOf(),
    val longTermMemory: MutableMap<String, MemoryTrace> = mutableMapOf()
) {
    fun store(trace: MemoryTrace, isLongTerm: Boolean = false) {
        if (isLongTerm) {
            longTermMemory[trace.id] = trace
        } else {
            shortTermMemory[trace.id] = trace
        }
    }

    fun consolidate(traceId: String) {
        shortTermMemory[traceId]?.let { trace ->
            longTermMemory[traceId] = trace.copy(strength = trace.strength * 1.5f)
            shortTermMemory.remove(traceId)
        }
    }
}

data class MemoryTrace(
    val id: String,
    val type: MemoryType,
    val content: Map<String, Any>,
    val strength: Float, // [0.0, 1.0]
    val timestamp: Long,
    val associations: List<String> = emptyList() // IDs of associated memories
)

enum class MemoryType {
    SENSORY,
    PROCEDURAL,
    ASSOCIATIVE,
    REWARD_ASSOCIATION,
    SPATIAL
}
