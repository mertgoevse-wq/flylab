package com.flylab.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.experiment.ExperimentConfiguration
import com.flylab.experiment.ExperimentType

/**
 * Bottom control panel for running, pausing, scrubbing, and changing experiment protocols.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentControlPanel(
    isPlaying: Boolean,
    currentStep: Long,
    currentTimeSeconds: Float,
    maxHistoryStep: Long,
    playbackSpeed: Float,
    currentConfig: ExperimentConfiguration,
    onPlayPauseToggled: () -> Unit,
    onStepForward: () -> Unit,
    onStepBackward: () -> Unit,
    onSeekToStep: (Long) -> Unit,
    onSpeedChanged: (Float) -> Unit,
    onConfigSelected: (ExperimentConfiguration) -> Unit,
    onOpenJournal: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Row 1: Protocol Dropdown & Journal Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = currentConfig.title,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier.menuAnchor(),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Nahrungssuche (Essigduft)", fontSize = 12.sp) },
                            onClick = {
                                onConfigSelected(ExperimentConfiguration.standardFoodSeeking())
                                dropdownExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Toxin-Meidung (Geosmin)", fontSize = 12.sp) },
                            onClick = {
                                onConfigSelected(ExperimentConfiguration.standardToxinAvoidance())
                                dropdownExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Gehirndämpfung (Antennallobus)", fontSize = 12.sp) },
                            onClick = {
                                onConfigSelected(ExperimentConfiguration.standardALSilencing())
                                dropdownExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = onOpenJournal,
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Text("Tagebuch", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Time-scrub Slider with current time & step
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "t = ${String.format("%.2f", currentTimeSeconds)} s (#$currentStep)",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.ui.text.TextStyle(fontFeatureSettings = "tnum"), modifier = Modifier.width(115.dp)
                )

                Slider(
                    value = currentStep.toFloat(),
                    onValueChange = { onSeekToStep(it.toLong()) },
                    valueRange = 0.0f..maxHistoryStep.coerceAtLeast(1L).toFloat(),
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 3: Playback Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onPlayPauseToggled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlaying) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (isPlaying) "Pause" else "Start", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedButton(onClick = onStepBackward, enabled = !isPlaying && currentStep > 0) {
                        Text("-1", fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedButton(onClick = onStepForward, enabled = !isPlaying) {
                        Text("+1", fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedButton(onClick = onReset) {
                        Text("Reset", fontSize = 11.sp)
                    }
                }

                // Speed Selectors
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val speeds = listOf(0.5f, 1.0f, 2.0f)
                    for (s in speeds) {
                        val isSelected = Math.abs(playbackSpeed - s) < 0.01f
                        FilledTonalButton(
                            onClick = { onSpeedChanged(s) },
                            modifier = Modifier.padding(horizontal = 2.dp),
                            colors = if (isSelected) ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) else ButtonDefaults.filledTonalButtonColors()
                        ) {
                            Text("${s}x", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}
