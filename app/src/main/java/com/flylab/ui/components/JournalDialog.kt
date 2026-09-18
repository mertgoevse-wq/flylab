package com.flylab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.experiment.ExperimentEvent
import com.flylab.experiment.ExperimentJournalEntry
import com.flylab.experiment.ExperimentRunner
import com.flylab.sim.ConnectomeReference
import com.flylab.sim.SimulationSnapshot

/**
 * Scientific Experiment Journal dialog presenting recorded trials, events, and side-by-side comparisons.
 */
@Composable
fun JournalDialog(
    currentSnapshot: SimulationSnapshot,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Verlauf & Ereignisse, 1 = Synaptische Plastizität, 2 = Versuchsvergleich

    // Generate current journal entry from current snapshot
    val journalEntry = remember(currentSnapshot.step) {
        val plasticChanges = currentSnapshot.synapticOverlays.mapValues { it.value.deltaWeight }
        ExperimentJournalEntry(
            id = "trial_live_${currentSnapshot.step}",
            configuration = com.flylab.experiment.ExperimentConfiguration.standardFoodSeeking(seed = currentSnapshot.seed),
            totalSteps = currentSnapshot.step,
            totalSimulatedSeconds = currentSnapshot.timeSeconds,
            totalDistanceTraveledMm = currentSnapshot.fly.behavioralState.distanceTraveledMm,
            foodConsumedUnits = currentSnapshot.fly.behavioralState.foodConsumedUnits,
            behaviorDistribution = mapOf(currentSnapshot.activeBehavior to 100.0f),
            meanRegionActivations = currentSnapshot.regionStates.mapValues { it.value.meanActivation },
            finalSynapticWeightChanges = plasticChanges,
            events = listOf(
                ExperimentEvent.BehaviorChange(0.0f, com.flylab.domain.model.BehaviorType.EXPLORING, currentSnapshot.activeBehavior, "Simulationslauf"),
                ExperimentEvent.OdorEncounter(currentSnapshot.timeSeconds, currentSnapshot.sensoryInput.activeOdor, currentSnapshot.sensoryInput.meanOdorConcentration)
            ),
            outcomeSummary = "Laufzeit: ${String.format("%.1f", currentSnapshot.timeSeconds)} s | Zurückgelegt: ${String.format("%.1f", currentSnapshot.fly.behavioralState.distanceTraveledMm)} mm"
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wissenschaftliches Labortagebuch",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                ProvenanceBadge(evidence = journalEntry.evidence)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Ereignisse", fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Plastizität", fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Vergleich", fontSize = 11.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                when (selectedTab) {
                    0 -> {
                        // Trial Overview & Event Stream
                        Column {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Lauf-Metriken:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text("Simulierte Zeit: ${String.format("%.2f", journalEntry.totalSimulatedSeconds)} s (#${journalEntry.totalSteps} Schritte)", fontSize = 11.sp)
                                    Text("Wegstrecke: ${String.format("%.1f", journalEntry.totalDistanceTraveledMm)} mm", fontSize = 11.sp)
                                    Text("Nahrung: ${String.format("%.2f", journalEntry.foodConsumedUnits)} Einheiten", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Kausaler Ereignisstrom:", fontWeight = FontWeight.Bold, fontSize = 11.sp)

                            LazyColumn(modifier = Modifier.height(200.dp)) {
                                items(journalEntry.events) { event ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "[${String.format("%.2f", event.timeSeconds)}s]",
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.width(55.dp)
                                        )
                                        Text(
                                            text = event.description,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                    1 -> {
                        // Synaptic Plasticity Breakdown
                        Column {
                            Text(
                                text = "Pilzkörper KC -> MBON Synapsengewichte:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Referenz-Baseline bleibt unverändert; Simulation speichert Overlays.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val plasticSynapses = ConnectomeReference.SYNAPSES.filter { it.isPlastic }
                            LazyColumn(modifier = Modifier.height(200.dp)) {
                                items(plasticSynapses) { syn ->
                                    val overlay = currentSnapshot.synapticOverlays[syn.synapseId]
                                    val currentW = overlay?.currentWeight ?: syn.baselineWeight
                                    val deltaW = overlay?.deltaWeight ?: 0.0f

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("${syn.preNeuronId} -> ${syn.postNeuronId}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                            Text("Baseline: ${syn.baselineWeight} | Aktuell: ${String.format("%.3f", currentW)}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }

                                        val deltaColor = when {
                                            deltaW < -0.001f -> Color(0xFFDC2626) // Depression
                                            deltaW > 0.001f -> Color(0xFF059669)  // Potentiation
                                            else -> Color.Gray
                                        }

                                        Text(
                                            text = String.format("%+.3f", deltaW),
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = deltaColor
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                    2 -> {
                        // Trial Comparison
                        val comparison = remember {
                            val trialA = ExperimentRunner(com.flylab.experiment.ExperimentConfiguration.standardFoodSeeking(seed = 101L).copy(durationSeconds = 3.0f)).runTrial()
                            val trialB = ExperimentRunner(com.flylab.experiment.ExperimentConfiguration.standardALSilencing(seed = 101L).copy(durationSeconds = 3.0f)).runTrial()
                            ExperimentRunner.compareTrials(trialA, trialB)
                        }

                        Column(modifier = Modifier.height(200.dp)) {
                            Text("Vergleich zweier Versuchsbedingungen:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Versuch A: Intakte Nahrungssuche", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Versuch B: Antennallobus 80% gedämpft", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Streckendifferenz: ${String.format("%+.1f", comparison.distanceDifferenceMm)} mm", fontSize = 11.sp)
                                    Text("Nahrungsdifferenz: ${String.format("%+.2f", comparison.foodDifferenceUnits)} Einheiten", fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = comparison.primaryDivergenceSummary,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Schließen")
            }
        }
    )
}
