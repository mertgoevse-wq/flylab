package com.flylab.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.sim.SimulationSnapshot

/**
 * 💊 Pharmacology Panel
 * Enables manual experimental perturbations by mimicking chemical injections.
 */
@Composable
fun PharmacologyPanel(
    snapshot: SimulationSnapshot,
    onInjectModulator: (NeuromodulatorType, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Pharmakologie & Neuromodulatoren",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Mikroinjektion für experimentelle Verhaltensänderung",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            NeuromodulatorRow(
                type = NeuromodulatorType.DOPAMINE,
                level = snapshot.fly.neuromodulators[NeuromodulatorType.DOPAMINE]?.concentration ?: 0f,
                description = "Lernen, Belohnung & Arousal",
                onInject = { onInjectModulator(NeuromodulatorType.DOPAMINE, 0.5f) }
            )

            NeuromodulatorRow(
                type = NeuromodulatorType.OCTOPAMINE,
                level = snapshot.fly.neuromodulators[NeuromodulatorType.OCTOPAMINE]?.concentration ?: 0f,
                description = "Flucht, Kampf & metabolischer Stress (Adrenalin-Äquivalent)",
                onInject = { onInjectModulator(NeuromodulatorType.OCTOPAMINE, 0.5f) }
            )

            NeuromodulatorRow(
                type = NeuromodulatorType.SEROTONIN,
                level = snapshot.fly.neuromodulators[NeuromodulatorType.SEROTONIN]?.concentration ?: 0f,
                description = "Aggression, Appetit & Schlaf-Wach-Rhythmus",
                onInject = { onInjectModulator(NeuromodulatorType.SEROTONIN, 0.5f) }
            )
        }
    }
}

@Composable
private fun NeuromodulatorRow(
    type: NeuromodulatorType,
    level: Float,
    description: String,
    onInject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = type.name.lowercase().replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(text = description, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { level },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        OutlinedButton(
            onClick = onInject,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            modifier = Modifier.height(28.dp)
        ) {
            Text("Injektion", fontSize = 10.sp)
        }
    }
}
