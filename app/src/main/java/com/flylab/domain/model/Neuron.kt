package com.flylab.domain.model

/**
 * Functional cell classification for Drosophila neurons.
 */
enum class NeuronType(
    val description: String,
    val primaryTransmitter: NeuromodulatorType
) {
    ORN("Olfactory Receptor Neuron", NeuromodulatorType.ACETYLCHOLINE),
    PN("Antennal Lobe Projection Neuron", NeuromodulatorType.ACETYLCHOLINE),
    LN("Antennal Lobe Local Interneuron", NeuromodulatorType.GABA),
    KC("Kenyon Cell (Mushroom Body intrinsic)", NeuromodulatorType.ACETYLCHOLINE),
    APL("Anterior Paired Lateral Neuron (MB feedback)", NeuromodulatorType.GABA),
    DAN("Dopaminergic Reinforcement Neuron", NeuromodulatorType.DOPAMINE),
    OAN("Octopaminergic Neuromodulatory Neuron", NeuromodulatorType.OCTOPAMINE),
    MBON("Mushroom Body Output Neuron", NeuromodulatorType.ACETYLCHOLINE), // Some are cholinergic, some GABAergic/glutamatergic
    LHON("Lateral Horn Output Neuron", NeuromodulatorType.ACETYLCHOLINE),
    RING("Ellipsoid Body Ring Neuron (CX Heading/Visual)", NeuromodulatorType.GABA),
    COMPASS("E-PG Compass Neuron (CX Protocerebral Bridge)", NeuromodulatorType.ACETYLCHOLINE),
    MOTOR_DESCENDING("Descending Premotor Command Neuron", NeuromodulatorType.ACETYLCHOLINE)
}

/**
 * An individual identified or representative neuron in the Drosophila connectome.
 */
data class Neuron(
    val id: String,
    val flyWireRootId: Long? = null, // e.g. 720575940621000000L from FlyWire v783
    val name: String,
    val type: NeuronType,
    val neuropil: NeuropilId,
    val hemisphere: Hemisphere = Hemisphere.RIGHT,
    val posX: Float = 0.0f,
    val posY: Float = 0.0f,
    val posZ: Float = 0.0f,
    override val evidence: ScientificEvidence = ScientificEvidence.FLYWIRE_FEMALE_CONNECTOME_V783
) : ProvenanceTagged

enum class Hemisphere {
    LEFT,
    RIGHT,
    MIDLINE
}

/**
 * Dynamic state of an individual neuron during simulation.
 */
data class NeuronDynamicState(
    val neuronId: String,
    val membranePotential: Float = -65.0f, // Resting potential mV (MODELED)
    val firingRate: Float = 0.0f,          // Normalized rate [0.0 .. 1.0]
    val lastSpikeTimeMs: Long = -1L,
    val adaptationCurrent: Float = 0.0f
) {
    init {
        require(firingRate in 0.0f..1.0f) { "Firing rate must be in [0, 1], was $firingRate" }
    }
}
