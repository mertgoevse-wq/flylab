package com.flylab.data

import com.flylab.domain.connectome.Connectome
import com.flylab.domain.connectome.Neuron
import com.flylab.domain.connectome.Synapse
import kotlinx.coroutines.flow.Flow

/**
 * Scalable connectome data layer.
 *
 * To handle 139k neurons and 54M synapses efficiently on mobile, data is abstracted
 * via pagination and regional loading streams instead of maintaining the entire graph in RAM.
 */
interface ConnectomeDataProvider {
    /**
     * Fetches metadata without neurological data payloads.
     */
    suspend fun getConnectomeMetadata(connectomeId: String): Connectome

    /**
     * Streams neurons specific to a neuropil/region for progressive rendering.
     */
    fun streamNeuronsForRegion(connectomeId: String, regionId: String): Flow<List<Neuron>>

    /**
     * Streams synaptic connections for a specific neuron.
     * Used for exploring single-neuron connectivity without graph explosion.
     */
    fun streamSynapsesForNeuron(connectomeId: String, neuronId: String): Flow<List<Synapse>>

    /**
     * Loads a structural chunk of the connectivity matrix.
     */
    suspend fun fetchSynapseMatrixChunk(
        connectomeId: String,
        sourceRegionId: String,
        targetRegionId: String,
        offset: Int,
        limit: Int
    ): List<Synapse>
}
