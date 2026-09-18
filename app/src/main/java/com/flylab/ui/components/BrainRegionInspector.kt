package com.flylab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.BrainRegionState
import com.flylab.domain.model.NeuropilId

/**
 * Interactive inspector for FlyBrain regions with live activation gauges and optogenetic controls.
 */
@Composable
fun BrainRegionInspector(
    regionStates: Map<NeuropilId, BrainRegionState>,
    onPerturbationChanged: (NeuropilId, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedNeuropil by remember { mutableStateOf<NeuropilId?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gehirnbereiche (Neuropile)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Tippen für Details & Dämpfung",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp)
        ) {
            for (neuropil in NeuropilId.entries) {
                val state = regionStates[neuropil] ?: BrainRegionState(neuropil = neuropil)
                val isSelected = selectedNeuropil == neuropil

                NeuropilCard(
                    neuropil = neuropil,
                    state = state,
                    isSelected = isSelected,
                    onClick = {
                        selectedNeuropil = if (isSelected) null else neuropil
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        // Active perturbation inspector panel if selected
        selectedNeuropil?.let { neuropil ->
            val state = regionStates[neuropil] ?: BrainRegionState(neuropil = neuropil)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${neuropil.abbreviation} – ${neuropil.standardName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        ProvenanceBadge(evidence = neuropil.evidence)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = neuropil.primaryFunction,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Simulationseingriff (Dämpfung/Erregung): ${(state.perturbationFactor * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (state.isPerturbed) {
                            Text(
                                text = "MODIFIZIERT",
                                color = Color.Red,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Slider(
                        value = state.perturbationFactor,
                        onValueChange = { onPerturbationChanged(neuropil, it) },
                        valueRange = 0.0f..2.0f,
                        steps = 20,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun NeuropilCard(
    neuropil: NeuropilId,
    state: BrainRegionState,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(135.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = neuropil.abbreviation,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                if (state.isPerturbed) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Red)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("EINGRIFF", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text(
                text = neuropil.germanName.substringBefore("(").trim(),
                fontSize = 10.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Activation level bar
            LinearProgressIndicator(
                progress = state.meanActivation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                color = if (state.meanActivation > 0.4f) Color(0xFF10B981) else Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Aktivität: ${(state.meanActivation * 100).toInt()}%",
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
