package com.flylab.experiment

import com.flylab.domain.model.NeuropilId
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExperimentSerializationTest {

    @Test
    fun `experiment configuration serializes cleanly to JSON`() {
        val config = ExperimentConfiguration.standardFoodSeeking(seed = 42L).copy(
            regionPerturbations = mapOf(NeuropilId.ANTENNAL_LOBE to 0.5f)
        )
        
        val jsonStr = ExperimentSerialization.serializeConfiguration(config)
        
        val parsed = JSONObject(jsonStr)
        assertEquals("exp_food_seeking", parsed.getString("id"))
        assertEquals(42L, parsed.getLong("seed"))
        
        val perturbations = parsed.getJSONArray("regionPerturbations")
        assertEquals(1, perturbations.length())
        assertEquals(NeuropilId.ANTENNAL_LOBE.name, perturbations.getJSONObject(0).getString("regionId"))
        assertEquals(0.5, perturbations.getJSONObject(0).getDouble("factor"), 0.001)
    }
}
