package com.flylab.domain.neural

/**
 * Neural activity state at different levels of detail.
 *
 * Level 0: Behavior only (no neural detail)
 * Level 1: Regional activity (brain regions as single units)
 * Level 2: Cell population activity (averaged per population)
 * Level 3: Individual neuron activity (selected neurons)
 * Level 4: Synaptic detail (selected synapses)
 */
sealed class NeuralActivity {
    abstract val timestamp: Long // milliseconds
    abstract val levelOfDetail: Int
}

/**
 * Regional-level activity (LOD 1)
 */
data class RegionalActivity(
    override val timestamp: Long,
    val regionActivities: Map<String, Float> // regionId -> activity [0.0, 1.0]
) : NeuralActivity() {
    override val levelOfDetail: Int = 1
}

/**
 * Neuron-level activity (LOD 3)
 */
data class NeuronActivity(
    override val timestamp: Long,
    val neuronActivities: Map<String, NeuronState> // neuronId -> state
) : NeuralActivity() {
    override val levelOfDetail: Int = 3
}

data class NeuronState(
    val neuronId: String,
    val membranePotential: Float, // mV
    val firingRate: Float, // Hz
    val isFiring: Boolean
) {
    /**
     * Normalized activity level [0.0, 1.0] for visualization
     */
    val activityLevel: Float get() = firingRate.coerceIn(0f, 100f) / 100f
}

/**
 * Synaptic activity (LOD 4)
 */
data class SynapticActivity(
    override val timestamp: Long,
    val synapticEvents: List<SynapticEvent>
) : NeuralActivity() {
    override val levelOfDetail: Int = 4
}

data class SynapticEvent(
    val synapseId: String,
    val timestamp: Long,
    val neurotransmitterRelease: Float,
    val postsynapticResponse: Float
)
