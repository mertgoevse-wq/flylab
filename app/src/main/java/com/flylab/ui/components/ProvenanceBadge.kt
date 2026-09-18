package com.flylab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence

/**
 * Clean scientific badge indicating the evidence level of a biological parameter.
 * Required by SCIENTIFIC_MODEL.md § 4.
 */
@Composable
fun ProvenanceBadge(
    evidence: ScientificEvidence,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    val (bgColor, textColor) = when (evidence.level) {
        ProvenanceLevel.MEASURED -> Color(0xFF059669) to Color.White   // Emerald
        ProvenanceLevel.PUBLISHED -> Color(0xFF0284C7) to Color.White  // Sky Blue
        ProvenanceLevel.DERIVED -> Color(0xFF6366F1) to Color.White    // Indigo
        ProvenanceLevel.MODELED -> Color(0xFFD97706) to Color.White    // Amber
        ProvenanceLevel.HYPOTHESIS -> Color(0xFFDC2626) to Color.White // Red
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor.copy(alpha = 0.15f))
            .clickable { showDialog = true }
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = evidence.level.canonicalName,
            color = bgColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }

    if (showDialog) {
        ProvenanceDetailDialog(
            evidence = evidence,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * 3-Level scientific provenance inspector dialog (Einfach / Standard / Wissenschaftlich).
 */
@Composable
fun ProvenanceDetailDialog(
    evidence: ScientificEvidence,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(1) } // Default: Standard

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Wissenschaftlicher Beleg",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = evidence.level.germanName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Einfach", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Standard", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Wissenschaft", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                when (selectedTab) {
                    0 -> {
                        // Einfache Erklärung
                        Text(
                            text = when (evidence.level) {
                                ProvenanceLevel.MEASURED -> "Dieser Wert wurde im echten Labor an einer echten Taufliege unter dem Elektronenmikroskop direkt gezählt."
                                ProvenanceLevel.PUBLISHED -> "Dieser Mechanismus wurde von Wissenschaftlern in Fachartikeln untersucht und genau beschrieben."
                                ProvenanceLevel.DERIVED -> "Dieser Wert wurde mathematisch aus echten Messungen des Fliegengehirns ausgerechnet."
                                ProvenanceLevel.MODELED -> "Das ist ein vereinfachtes Computermodell, das sich an echten biologischen Prinzipien orientiert."
                                ProvenanceLevel.HYPOTHESIS -> "Das ist eine Annahme oder Vermutung, für die es noch keine sicheren Messdaten gibt."
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    1 -> {
                        // Standard-Erklärung
                        Text(
                            text = evidence.level.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        if (evidence.notes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = evidence.notes,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    2 -> {
                        // Wissenschaftliche Detailansicht
                        Column {
                            if (evidence.citation != null) {
                                Text(
                                    text = "Literaturbeleg:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = evidence.citation,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            if (evidence.doi != null) {
                                Text(
                                    text = "DOI: ${evidence.doi}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            if (evidence.datasetSource != null) {
                                Text(
                                    text = "Datensatz: ${evidence.datasetSource} (${evidence.datasetVersion ?: "unversioniert"})",
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Konfidenzniveau: ${(evidence.confidence * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            LinearProgressIndicator(
                                progress = evidence.confidence,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            )
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
