content = open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt", "r").read()

new_imports = "import androidx.compose.foundation.rememberScrollState\nimport androidx.compose.foundation.verticalScroll\n"
if "import androidx.compose.foundation.verticalScroll" not in content:
    content = content.replace("import androidx.compose.foundation.layout.width\n", "import androidx.compose.foundation.layout.width\n" + new_imports)

old_logic = """                // Diagnostics
                com.flylab.ui.components.DiagnosticsDashboard(snapshot = engine.diagnostics.getSnapshot(1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

                // 2. Brain Region Inspector (Neuropil activation & live perturbation)
                BrainRegionInspector(
                    regionStates = currentSnapshot.regionStates,
                    onPerturbationChanged = { region: NeuropilId, factor: Float ->
                        engine.setRegionPerturbation(region, factor)
                        currentSnapshot = engine.history.last()
                    }
                )

                // 3. Sensory-Motor Status Row
                SensoryMotorDashboard(
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

                // 3.8 Pharmacology & Micro-Injections
                PharmacologyPanel(
                    snapshot = currentSnapshot,
                    onInjectModulator = { type, amount ->
                        engine.injectNeuromodulator(type, amount)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )

                // 3.9 Genome & Genetics
                GenomeExplorerScreen(
                    snapshot = currentSnapshot,
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
                    onSpeedChanged = { s -> playbackSpeed = s },
                    onReset = {
                        isPlaying = false
                        engine.reset()
                        currentSnapshot = engine.history.last()
                        playbackSpeed = 1.0f
                    },
                    onJournalOpen = { showJournalDialog = true },
                    onSeek = { step ->
                        engine.seekToStep(step)
                        currentSnapshot = engine.history.last()
                    },
                    onSaveSession = {
                        PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                        // Optional online sync (non-blocking)
                        val sessionData = SaveSession(experimentId = currentConfig.id, history = engine.history)
                        scope.launch {
                            OnlinePersistenceManager.syncSessionToCloud(sessionData)
                        }
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
            } else if (currentTab == 1) {"""

new_logic = """                // Scrollable container for all controls below 3D viewport
                Column(modifier = Modifier.weight(0.58f).fillMaxWidth().verticalScroll(rememberScrollState())) {
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

                    // 3. Sensory-Motor Status Row
                    SensoryMotorDashboard(
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

                    // 3.8 Pharmacology & Micro-Injections
                    PharmacologyPanel(
                        snapshot = currentSnapshot,
                        onInjectModulator = { type, amount ->
                            engine.injectNeuromodulator(type, amount)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )

                    // 3.9 Genome & Genetics
                    GenomeExplorerScreen(
                        snapshot = currentSnapshot,
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
                        onSpeedChanged = { s -> playbackSpeed = s },
                        onReset = {
                            isPlaying = false
                            engine.reset()
                            currentSnapshot = engine.history.last()
                            playbackSpeed = 1.0f
                        },
                        onJournalOpen = { showJournalDialog = true },
                        onSeek = { step ->
                            engine.seekToStep(step)
                            currentSnapshot = engine.history.last()
                        },
                        onSaveSession = {
                            PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                            // Optional online sync (non-blocking)
                            val sessionData = SaveSession(experimentId = currentConfig.id, history = engine.history)
                            scope.launch {
                                OnlinePersistenceManager.syncSessionToCloud(sessionData)
                            }
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            } else if (currentTab == 1) {"""

content = content.replace(old_logic, new_logic)
open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt", "w").write(content)
