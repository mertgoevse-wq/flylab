package com.flylab.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.sim.SimulationSnapshot

/**
 * 🧬 Genome Explorer
 * Interactively explores the Drosophila genome, and visualizes defined genes
 * whose knockouts/mutations have proven biological effects on the simulation.
 */
@Composable
fun GenomeExplorerScreen(
    snapshot: SimulationSnapshot,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Drosophila Genom & CRISPR Visualisierung",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Erkunden und manipulieren Sie Schlüsselgene (MEASURED / PUBLISHED).",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            GeneRowItem(
                chromosome = "X",
                geneName = "white (w)",
                function = "Transport von Augenpigmenten",
                mutationEffect = "Weiße Augen, verändertes Balzverhalten",
                status = "Wildtype (w+)"
            )

            GeneRowItem(
                chromosome = "X",
                geneName = "dunce (dnc)",
                function = "Dopamin/cAMP Abbau im Pilzkörper",
                mutationEffect = "Stark reduziertes Kurzzeitgedächtnis",
                status = "Wildtype (dnc+)"
            )

            GeneRowItem(
                chromosome = "X",
                geneName = "rutabaga (rut)",
                function = "Calcium/Calmodulin-abhängige Adenylatzyklase",
                mutationEffect = "Lernunfähigkeit in olfaktorischer Konditionierung",
                status = "Wildtype (rut+)"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /* TODO: CRISPR Implementation */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CRISPR/Cas9 Mutation auslösen", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun GeneRowItem(
    chromosome: String,
    geneName: String,
    function: String,
    mutationEffect: String,
    status: String
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Chr. $chromosome: $geneName", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(text = status, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        }
        Text(text = "Funktion: $function", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "Mutationsfolge: $mutationEffect", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
    }
}
