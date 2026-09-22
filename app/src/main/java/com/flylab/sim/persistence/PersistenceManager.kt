package com.flylab.sim.persistence

import android.content.Context
import android.util.Log
import com.flylab.sim.SimulationSnapshot
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

object PersistenceManager {
    private const val TAG = "PersistenceManager"
    private const val SAVE_FILE_NAME = "flylab_save.json"

    private val gson: Gson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

    /**
     * Saves a list of snapshots to disk.
     */
    fun saveReplaySession(context: Context, history: List<SimulationSnapshot>, experimentId: String) {
        try {
            val file = File(context.filesDir, SAVE_FILE_NAME)
            val session = SaveSession(
                version = 1,
                experimentId = experimentId,
                history = history
            )
            val json = gson.toJson(session)
            file.writeText(json)
            Log.i(TAG, "Successfully saved session with ${history.size} states to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save session", e)
        }
    }

    /**
     * Loads the saved session if available.
     */
    fun loadReplaySession(context: Context): SaveSession? {
        try {
            val file = File(context.filesDir, SAVE_FILE_NAME)
            if (!file.exists()) {
                return null
            }
            val json = file.readText()
            val type = object : TypeToken<SaveSession>() {}.type
            val session: SaveSession = gson.fromJson(json, type)
            Log.i(TAG, "Successfully loaded session with ${session.history.size} states.")
            return session
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load session", e)
            return null
        }
    }

    fun deleteSession(context: Context) {
        try {
            val file = File(context.filesDir, SAVE_FILE_NAME)
            if (file.exists()) {
                file.delete()
            }
        } catch(e: Exception) {
            Log.e(TAG, "Failed to delete session", e)
        }
    }
}

data class SaveSession(
    val version: Int = 1,
    val experimentId: String,
    val history: List<SimulationSnapshot>
)
