package com.flylab.data

import com.flylab.domain.connectome.Connectome
import com.flylab.domain.connectome.Neuron
import com.flylab.domain.connectome.Synapse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Supabase-backed connectome implementation.
 * Ensures we abide by RLS and chunk properly to avoid exceeding Data API limits.
 */
class SupabaseConnectomeProvider : ConnectomeDataProvider {
    override suspend fun getConnectomeMetadata(connectomeId: String): Connectome {
        // TODO: Map to Supabase table 'connectomes' with REST client
        throw NotImplementedError("To be integrated when Supabase is initialized via MCP instructions")
    }

    override fun streamNeuronsForRegion(connectomeId: String, regionId: String): Flow<List<Neuron>> {
        // TODO: Realtime listener on 'neurons' table with eq("region_id", regionId)
        return emptyFlow()
    }

    override fun streamSynapsesForNeuron(connectomeId: String, neuronId: String): Flow<List<Synapse>> {
        // TODO: Paginate via PostgREST
        return emptyFlow()
    }

    override suspend fun fetchSynapseMatrixChunk(
        connectomeId: String,
        sourceRegionId: String,
        targetRegionId: String,
        offset: Int,
        limit: Int
    ): List<Synapse> {
        // TODO: Supabase rpc call for complex topological queries to avoid fetching immense edge lists locally
        return emptyList()
    }
}
