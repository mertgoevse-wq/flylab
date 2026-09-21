package com.flylab.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import com.flylab.sim.SimulationSnapshot

data class CausalStep(
    val title: String,
    val subtitle: String,
    val details: String,
    val evidence: ScientificEvidence,
    val color: Color
)

/**
 * An interactive pipeline rendering the causal loop:
 * Stimulus -> Sense Organ -> Brain Processing -> Motor Mapping -> Behavior.
 * Each node can be tapped to reveal scientific provenance, mathematical models, and live values.
 */
@Composable
fun CausalChainView(
    snapshot: SimulationSnapshot,
    modifier: Modifier = Modifier
) {
    var expandedStepIndex by remember { mutableStateOf<Int?>(null) }

    val steps = remember(snapshot) {
        listOf(
            CausalStep(
                title = "1. Stimulus",
                subtitle = snapshot.sensoryInput.activeOdor.germanName.substringBefore("(").trim(),
                details = "Konzentration: L=${String.format("%.2f", snapshot.sensoryInput.odorLeftAntenna)}, R=${String.format("%.2f", snapshot.sensoryInput.odorRightAntenna)} (Gradient: ${String.format("%.2f", snapshot.sensoryInput.odorGradientBilateral)})",
                evidence = snapshot.sensoryInput.activeOdor.evidence,
                color = Color(0xFF059669)
            ),
            CausalStep(
                title = "2. Sinnesorgan",
                subtitle = "Antennen-Sensillen (ORNs)",
                details = "Transduktion über Geruchsrezeptor-Neuronen in den Antennen/Maxillarpalpen.",
                evidence = ScientificEvidence(
                    level = ProvenanceLevel.MEASURED,
                    citation = "Hallem & Carlson (2006) Cell",
                    notes = "Elektrophysiologische Reizantwortprofile der Drosophila Olfaktorischen Rezeptor-Neurone."
                ),
                color = Color(0xFF0D9488)
            ),
            CausalStep(
                title = "3. Gehirn / AL & MB",
                subtitle = "Projektions- & Kenyon-Zellen",
                details = "Antennallobus-Glomeruli -> Pilzkörper/Laterales Horn. Firing Rate: ${String.format("%.1f", snapshot.firingRates.values.average() * 100)}% Max.",
                evidence = ScientificEvidence(
                    level = ProvenanceLevel.MEASURED,
                    citation = "FlyWire Whole-Brain Connectome (2024)",
                    notes = "Rekonstruierte synaptische Schaltkreise des gesamten weiblichen Drosophila-Gehirns."
                ),
                color = Color(0xFF3B82F6)
            ),
            CausalStep(
                title = "4. Motorkommando",
                subtitle = "Absteigende VNC-Neurone",
                details = "Vorwärts: ${String.format("%.1f", snapshot.motorCommand.forwardVelocityMmS)} mm/s, Drehung: ${String.format("%.1f", Math.toDegrees(snapshot.motorCommand.angularVelocityRadS.toDouble()))} °/s.",
                evidence = ScientificEvidence(
                    level = ProvenanceLevel.MODELLED,
                    citation = "Namiki et al. (2018) eLife",
                    notes = "Absteigende Kontrollbahnen zur Steuerung von Lauf- und Flugbewegungen."
                ),
                color = Color(0xFFF59E0B)
            ),
            CausalStep(
                title = "5. Verhalten",
                subtitle = snapshot.activeBehavior.germanName.substringBefore("(").trim(),
                details = "Zustand: ${snapshot.activeBehavior.name}, Rüsselausstülpung: ${String.format("%.2f", snapshot.motorCommand.proboscisExtension)}",
                evidence = snapshot.activeBehavior.evidence,
                color = Color(0xFFEF4444)
            )
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Kausalkette: Reiz → Verhalten",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, step ->
                    val isSelected = expandedStepIndex == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) step.color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) step.color else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                expandedStepIndex = if (isSelected) null else index
                            }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = step.title.substringAfter(" "),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) step.color else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = step.subtitle,
                                fontSize = 8.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (index < steps.size - 1) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = expandedStepIndex != null,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                expandedStepIndex?.let { index ->
                    val step = steps[index]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${step.title}: ${step.subtitle}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = step.color
                            )
                            ProvenanceBadge(evidence = step.evidence)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = step.details,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (!step.evidence.notes.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Biologische Anmerkung: ${step.evidence.notes}",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
