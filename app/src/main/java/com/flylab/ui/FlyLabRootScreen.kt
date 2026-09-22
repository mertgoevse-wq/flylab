package com.flylab.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.NeuropilId
import com.flylab.experiment.ExperimentConfiguration
import com.flylab.render3d.RenderLayers
import com.flylab.sim.SimulationEngine
import com.flylab.ui.components.BrainRegionInspector
import com.flylab.ui.components.CausalChainView
import com.flylab.ui.components.TimelineGraphView
import com.flylab.ui.components.ExperimentControlPanel
import com.flylab.ui.components.JournalDialog
import com.flylab.ui.components.ProvenanceBadge
import com.flylab.ui.components.SensoryMotorDashboard
import com.flylab.ui.components.Viewport3DCanvas
import com.flylab.ui.components.ConnectomeExplorerScreen
import kotlinx.coroutines.delay
import com.flylab.sim.persistence.PersistenceManager
import androidx.compose.ui.platform.LocalContext


/**
 * Root interactive simulation environment for FlyLab.
 * Fulfills the complete Milestone 1 Vertical Slice requirements (CLAUDE.md).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlyLabRootScreen() {
    var currentConfig by remember {
        mutableStateOf(ExperimentConfiguration.standardFoodSeeking())
    }

    val engine = remember(currentConfig.id, currentConfig.seed) {
        SimulationEngine(
            initialFly = (currentConfig.subject as com.flylab.domain.model.Fly),
            initialEnvironment = currentConfig.initialEnvironment,
            seed = currentConfig.seed
        )
    }

    var isPlaying by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var renderLayers by remember { mutableStateOf(RenderLayers()) }
    var showJournalDialog by remember { mutableStateOf(false) }

    // State triggering UI recomposition on each step
    var currentSnapshot by remember {
        mutableStateOf(engine.history.last())
    }

    var currentTab by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // Coroutine simulation loop
    LaunchedEffect(isPlaying, playbackSpeed, engine) {
        while (isPlaying) {
            val dt = 0.05f
            currentSnapshot = engine.step(dt)
            val delayMs = (dt * 1000f / playbackSpeed).toLong().coerceAtLeast(10L)
            delay(delayMs)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FlyLab",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Drosophila melanogaster (♀)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "FlyWire Connectome v783 • Neuronale Dynamik & Chemotaxis",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    ProvenanceBadge(
                        evidence = (currentConfig.subject as com.flylab.domain.model.Fly).evidence,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            androidx.compose.material3.NavigationBar {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = { Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Simulation"
                    ) },
                    label = { Text("Simulation") }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = { Icon(
                        imageVector = Icons.Default.AccountTree,
                        contentDescription = "Connectome"
                    ) },
                    label = { Text("Connectome") }
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = { Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Genome"
                    ) },
                    label = { Text("Genome") }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (currentTab == 0) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 1. 3D Fly Viewport (Flexible weight: 45% of screen height)
                Box(modifier = Modifier.weight(0.42f).fillMaxWidth()) {
                    Viewport3DCanvas(
                        snapshot = currentSnapshot,
                        renderLayers = renderLayers,
                        onLayerChanged = { renderLayers = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Diagnostics
                com.flylab.ui.components.DiagnosticsDashboard(snapshot = engine.diagnostics.getSnapshot(1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

                // 2. Brain Region Inspector (Neuropil activation & live perturbation)
                BrainRegionInspector(
                    regionStates = currentSnapshot.regionStates,
                    onPerturbationChanged = { region: NeuropilId, factor: Float ->
                        engine.setRegionPerturbation(region, factor)
                        currentSnapshot = engine.history.last()
                    }
                )

                // 3. Sensory-Motor & Neuromodulator Dashboard
                SensoryMotorDashboard(
                    snapshot = currentSnapshot,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )

                // 3.5 Causal Chain Step-by-Step UI
                CausalChainView(
                    snapshot = currentSnapshot,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )

                // 3.6 Plasticity UI
                com.flylab.ui.components.PlasticityDashboard(
                    snapshot = currentSnapshot,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )

                // 3.7 Visual Timeline Graph
                TimelineGraphView(
                    history = engine.history,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )

                // 4. Experiment Playback & Scrub Controls
                ExperimentControlPanel(
                    isPlaying = isPlaying,
                    currentStep = currentSnapshot.step,
                    currentTimeSeconds = currentSnapshot.timeSeconds,
                    maxHistoryStep = engine.history.last().step,
                    playbackSpeed = playbackSpeed,
                    currentConfig = currentConfig,
                    onPlayPauseToggled = { isPlaying = !isPlaying },
                    onStepForward = {
                        currentSnapshot = engine.step(0.05f)
                    },
                    onStepBackward = {
                        if (currentSnapshot.step > 0) {
                            engine.seekToStep(currentSnapshot.step - 1)
                            currentSnapshot = engine.history.find { it.step == currentSnapshot.step - 1 } ?: engine.history.last()
                        }
                    },
                    onSeekToStep = { targetStep ->
                        engine.seekToStep(targetStep)
                        currentSnapshot = engine.history.find { it.step == targetStep } ?: engine.history.last()
                    },
                    onSpeedChanged = { playbackSpeed = it },
                    onConfigSelected = { newConfig ->
                        isPlaying = false
                        currentConfig = newConfig
                    },
                    onOpenJournal = { showJournalDialog = true },
                    onReset = {
                        isPlaying = false
                        engine.reset()
                        currentSnapshot = engine.history.last()
                    },
                    onSaveSession = {
                        PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                    },
                    onLoadSession = {
                        val session = PersistenceManager.loadReplaySession(context)
                        if (session != null && session.history.isNotEmpty()) {
                            isPlaying = false
                            engine.reset()
                            engine._setHistoryForReplay(session.history)
                            engine.seekToStep(session.history.last().step)
                            currentSnapshot = engine.history.last()
                        }
                    }
                )
            }
            } else if (currentTab == 1) {
                ConnectomeExplorerScreen(
                    snapshot = currentSnapshot,
                    onNeuronPerturbationChanged = { neuronId, factor ->
                        engine.setNeuronPerturbation(neuronId, factor)
                    }
                )
            } else {
                GenomeExplorerScreen()
            }
        }

        if (showJournalDialog) {
            JournalDialog(
                currentSnapshot = currentSnapshot,
                onDismiss = { showJournalDialog = false }
            )
        }
    }
}
