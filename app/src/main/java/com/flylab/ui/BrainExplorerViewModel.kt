package com.flylab.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flylab.data.BrainDataRepository
import com.flylab.domain.brain.Brain
import com.flylab.domain.connectome.Connectome
import com.flylab.domain.neural.RegionalActivity
import com.flylab.domain.simulation.SimulationConfig
import com.flylab.domain.simulation.SimulationState
import com.flylab.simulation.SimulationEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the brain explorer.
 * Manages simulation state and updates.
 */
class BrainExplorerViewModel : ViewModel() {

    private val repository = BrainDataRepository()
    private val simulationEngine = SimulationEngine(
        config = SimulationConfig(
            seed = System.currentTimeMillis(),
            timeStep = 100f,
            levelOfDetail = 1,
            enablePlasticity = true,
            enableMemory = true
        )
    )

    private var _state = mutableStateOf<SimulationState?>(null)
    val state: State<SimulationState?> = _state

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
            val brain = repository.loadReferenceBrain()
            val connectome = repository.loadReferenceConnectome()

            _brain.value = brain

            val initialState = simulationEngine.createInitialState(brain, connectome)
            _state.value = initialState
        }
    }

    fun startSimulation() {
        if (_isRunning.value) return

        _isRunning.value = true
        simulationJob = viewModelScope.launch {
            while (_isRunning.value) {
                _state.value?.let { currentState ->
                    val newState = simulationEngine.step(currentState)
                    _state.value = newState
                    _activity.value = newState.neuralActivity as? RegionalActivity
                }
                delay(100) // Update every 100ms
            }
        }
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
