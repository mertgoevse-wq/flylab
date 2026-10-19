package com.flylab.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flylab.data.BrainDataRepository
import com.flylab.domain.brain.Brain
import com.flylab.domain.neural.RegionalActivity
import com.flylab.sim.SimulationEngine
import com.flylab.sim.SimulationSnapshot
import com.flylab.sim.toRegionalActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the brain explorer.
 *
 * Driven by the CANONICAL [SimulationEngine] (`com.flylab.sim`) — the same engine and
 * snapshot type that powers the world view. There is exactly one simulation state;
 * this ViewModel only projects it to regional activity for the brain viewer.
 * Seed is fixed for determinism (was: System.currentTimeMillis()).
 */
class BrainExplorerViewModel(
    seed: Long = 42L
) : ViewModel() {

    private val repository = BrainDataRepository()
    private val simulationEngine = SimulationEngine(seed = seed)

    private var _snapshot = mutableStateOf<SimulationSnapshot?>(null)
    val snapshot: State<SimulationSnapshot?> = _snapshot

    private var _brain = mutableStateOf<Brain?>(null)
    val brain: State<Brain?> = _brain

    private var _activity = mutableStateOf<RegionalActivity?>(null)
    val activity: State<RegionalActivity?> = _activity

    private var _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private var simulationJob: Job? = null

    init {
        loadBrainData()
    }

    private fun loadBrainData() {
        viewModelScope.launch {
            _brain.value = repository.loadReferenceBrain()
            val snapshot = simulationEngine.history.last()
            _snapshot.value = snapshot
            _activity.value = snapshot.toRegionalActivity()
        }
    }

    fun startSimulation() {
        if (_isRunning.value) return

        _isRunning.value = true
        simulationJob = viewModelScope.launch {
            while (_isRunning.value) {
                val snapshot = simulationEngine.step(0.05f)
                _snapshot.value = snapshot
                _activity.value = snapshot.toRegionalActivity()
                delay(50)
            }
        }
    }

    /** Single deterministic step; used by tests and frame-stepping UI. */
    fun stepOnce(dtSeconds: Float = 0.05f) {
        val snapshot = simulationEngine.step(dtSeconds)
        _snapshot.value = snapshot
        _activity.value = snapshot.toRegionalActivity()
    }

    fun stopSimulation() {
        _isRunning.value = false
        simulationJob?.cancel()
        simulationJob = null
    }

    fun toggleSimulation() {
        if (_isRunning.value) {
            stopSimulation()
        } else {
            startSimulation()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopSimulation()
    }
}

