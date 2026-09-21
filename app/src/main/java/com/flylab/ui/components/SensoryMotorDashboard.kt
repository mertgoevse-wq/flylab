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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.BehaviorType
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.sim.SimulationSnapshot
import com.flylab.ui.theme.*

/**
 * Dashboard monitoring real-time sensory inputs, internal drives, neuromodulation, and motor status.
 */
@Composable
fun SensoryMotorDashboard(
    snapshot: SimulationSnapshot,
    modifier: Modifier = Modifier
) {
    val fly = snapshot.fly
    val behavior = snapshot.activeBehavior
    val sensory = snapshot.sensoryInput
    val motor = snapshot.motorCommand
    val needs = fly.behavioralState.needs

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row: Active Behavior + Provenance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val behaviorColor = when (behavior) {
                        BehaviorType.APPROACHING -> ColorBehaviorApproach
                        BehaviorType.AVOIDING -> ColorBehaviorAvoid
                        BehaviorType.FEEDING -> ColorBehaviorFeed
                        BehaviorType.ORIENTING -> ColorBehaviorOrient
                        BehaviorType.EXPLORING -> ColorBehaviorExplore
                        BehaviorType.RESTING -> ColorBehaviorRest
                        BehaviorType.GROOMING -> ColorBehaviorGroom
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(behaviorColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = behavior.germanName.substringBefore("(").trim(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${String.format("%.1f", motor.forwardVelocityMmS)} mm/s",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ProvenanceBadge(evidence = behavior.evidence)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sensory Bilateral Gauges & Drives in 2 Columns
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Left Column: Odor & Antennal Transduction
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Antennen-Geruchsfeld (${sensory.activeOdor.germanName.substringBefore("(").trim()})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("L: ", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        LinearProgressIndicator(
                            progress = { sensory.odorLeftAntenna },
                            modifier = Modifier.weight(1f).height(4.dp),
                            color = ColorBehaviorApproach
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("R: ", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        LinearProgressIndicator(
                            progress = { sensory.odorRightAntenna },
                            modifier = Modifier.weight(1f).height(4.dp),
                            color = ColorBehaviorApproach
                        )
                    }

                    if (sensory.sucroseContact > 0.05f) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Zuckerkontakt: ${(sensory.sucroseContact * 100).toInt()}%",
                            color = ColorBehaviorFeed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Right Column: Motivational Drives
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Physiologische Bedürfnisse",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Hunger: ", fontSize = 10.sp, modifier = Modifier.width(42.dp))
                        LinearProgressIndicator(
                            progress = { needs.hunger },
                            modifier = Modifier.weight(1f).height(4.dp),
                            color = ColorBehaviorFeed
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ermüdung:", fontSize = 10.sp, modifier = Modifier.width(42.dp))
                        LinearProgressIndicator(
                            progress = { needs.fatigue },
                            modifier = Modifier.weight(1f).height(4.dp),
                            color = ColorBehaviorRest
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Neuromodulation Bar (Drosophila-specific: Octopamine, Dopamine, Serotonin)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val oa = fly.neuromodulators[NeuromodulatorType.OCTOPAMINE]?.concentration ?: 0.15f
                val da = fly.neuromodulators[NeuromodulatorType.DOPAMINE]?.concentration ?: 0.15f
                val ht = fly.neuromodulators[NeuromodulatorType.SEROTONIN]?.concentration ?: 0.15f

                ModulatorPill(name = "OA", value = oa, color = ColorModulatorOctopamine)
                ModulatorPill(name = "DA", value = da, color = ColorModulatorDopamine)
                ModulatorPill(name = "5-HT", value = ht, color = ColorModulatorSerotonin)

                Text(
                    text = "Distanz: ${String.format("%.1f", fly.behavioralState.distanceTraveledMm)} mm",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ModulatorPill(name: String, value: Float, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$name: ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = "${(value * 100).toInt()}%",
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = color
        )
    }
}
