package com.flylab.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.sim.SimulationSnapshot

/**
 * Renders a low-resolution representation of the bilateral visual input
 * received by the fly's compound eyes.
 */
@Composable
fun VisualFieldView(snapshot: SimulationSnapshot, modifier: Modifier = Modifier) {
    val sensory = snapshot.sensoryInput

    // Invert the flow so high obstacle magnitude appears as a dark looming object
    // Assuming background is ambient luminance (e.g. gray/white), objects are darker.
    val baseLuminance = sensory.lightIntensity
    val leftObjectStrength = sensory.opticFlowLeft
    val rightObjectStrength = sensory.opticFlowRight

    Column(modifier = modifier) {
        Text(
            text = "Compound Eye Field of View (Modeled)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Left Eye
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF80CAFF).copy(alpha = baseLuminance * 0.5f))
            ) {
                // Looming dark object visualization
                if (leftObjectStrength > 0.05f) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val maxTargetRadius = size.minDimension * 0.8f
                        val currentRadius = maxTargetRadius * leftObjectStrength.coerceIn(0f, 1f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent),
                                radius = currentRadius
                            ),
                            radius = currentRadius,
                            center = center.copy(x = center.x + 20f) // offset slightly towards center
                        )
                    }
                }
                Text(
                    text = "L",
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.BottomStart).padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.DarkGray))

            // Right Eye
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF80CAFF).copy(alpha = baseLuminance * 0.5f))
            ) {
                // Looming dark object visualization
                if (rightObjectStrength > 0.05f) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val maxTargetRadius = size.minDimension * 0.8f
                        val currentRadius = maxTargetRadius * rightObjectStrength.coerceIn(0f, 1f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent),
                                radius = currentRadius
                            ),
                            radius = currentRadius,
                            center = center.copy(x = center.x - 20f) // offset slightly towards center
                        )
                    }
                }
                Text(
                    text = "R",
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                )
            }
        }
    }
}
