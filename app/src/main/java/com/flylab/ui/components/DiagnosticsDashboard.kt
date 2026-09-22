package com.flylab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.sim.diagnostics.DiagnosticsSnapshot

@Composable
fun DiagnosticsDashboard(
    snapshot: DiagnosticsSnapshot,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "PERFORMANCE DIAGNOSTICS",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Row(modifier = Modifier.padding(top = 4.dp)) {
            DiagnosticsItem("Tick (ms)", "%.2f".format(snapshot.tickDurationMs))
            Spacer(modifier = Modifier.width(16.dp))
            DiagnosticsItem("Infer (ms)", "%.2f".format(snapshot.inferenceLatencyMs))
            Spacer(modifier = Modifier.width(16.dp))
            DiagnosticsItem("Backend", snapshot.computeBackend)
        }
    }
}

@Composable
private fun DiagnosticsItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
        )
    }
}
