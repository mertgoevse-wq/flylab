package com.flylab.experiment

import org.json.JSONArray
import org.json.JSONObject

/**
 * Ensures experiments are fully serializable for saving, loading, and sharing.
 */
object ExperimentSerialization {
    
    fun serializeConfiguration(config: ExperimentConfiguration): String {
        val json = JSONObject()
        json.put("version", "1.0")
        json.put("id", config.id)
        json.put("type", config.type.name)
        json.put("title", config.title)
        json.put("researchQuestion", config.researchQuestion)
        json.put("hypothesis", config.hypothesis)
        json.put("seed", config.seed)
        json.put("durationSeconds", config.durationSeconds.toDouble())
        json.put("dtSeconds", config.dtSeconds.toDouble())
        
        // Serialize perturbations
        val perturbationsArray = JSONArray()
        for ((region, factor) in config.regionPerturbations) {
            val pObj = JSONObject()
            pObj.put("regionId", region.name)
            pObj.put("factor", factor.toDouble())
            perturbationsArray.put(pObj)
        }
        json.put("regionPerturbations", perturbationsArray)
        
        return json.toString(2)
    }
}
