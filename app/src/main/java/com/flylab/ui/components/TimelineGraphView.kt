package com.flylab.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.flylab.sim.SimulationSnapshot

/**
 * Renders a timeline of neural events and behavior over the most recent history.
 */
@Composable
fun TimelineGraphView(
    history: List<SimulationSnapshot>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

    val historySubset = history.takeLast(100) // Render up to 100 recent steps

    Canvas(modifier = modifier.fillMaxWidth().height(80.dp).padding(4.dp)) {
        val width = size.width
        val height = size.height

        val stepWidth = width / historySubset.size.coerceAtLeast(1).toFloat()
        
        val pathSensory = Path()
        val pathMotor = Path()

        var first = true

        historySubset.forEachIndexed { index, snapshot ->
            val x = index * stepWidth
            
            // Map smell intensity to Y
            val sensoryY = height - (snapshot.sensoryInput.meanOdorConcentration.coerceIn(0f, 1f) * height)
            
            // Map motor command (turn bias) to Y
            val turnVal = snapshot.motorCommand.angularVelocityRadS.coerceIn(-10f, 10f) / 10f
            val motorY = (height / 2f) - (turnVal * (height / 2f))

            if (first) {
                pathSensory.moveTo(x, sensoryY)
                pathMotor.moveTo(x, motorY)
                first = false
            } else {
                pathSensory.lineTo(x, sensoryY)
                pathMotor.lineTo(x, motorY)
            }
        }

        // Draw zero-line for motor
        drawLine(
            color = Color.LightGray.copy(alpha = 0.5f),
            start = Offset(0f, height / 2f),
            end = Offset(width, height / 2f)
        )

        // Draw Sensory Path (Blue)
        drawPath(
            path = pathSensory,
            color = Color(0xFF3B82F6),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Motor Path (Orange)
        drawPath(
            path = pathMotor,
            color = Color(0xFFF97316),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
