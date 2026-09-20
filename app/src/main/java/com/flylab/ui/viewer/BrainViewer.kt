package com.flylab.ui.viewer

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.flylab.domain.brain.Brain
import com.flylab.domain.brain.BrainRegion
import com.flylab.domain.brain.Coordinates3D
import com.flylab.domain.neural.RegionalActivity
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D Brain Viewer Component
 *
 * Interactive brain visualization with:
 * - Rotation via drag
 * - Zoom via pinch/scale
 * - Region selection
 * - Activity visualization overlay
 */
@Composable
fun BrainViewer(
    brain: Brain,
    activity: RegionalActivity?,
    selectedRegionId: String?,
    onRegionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var rotation by remember { mutableStateOf(Offset(0f, 0f)) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rotationDelta ->
                        scale = (scale * zoom).coerceIn(0.5f, 5f)
                        offset += pan
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        rotation += Offset(
                            dragAmount.y * 0.005f,
                            dragAmount.x * 0.005f
                        )
                    }
                }
        ) {
            drawBrain(
                brain = brain,
                activity = activity,
                rotation = rotation,
                scale = scale,
                offset = offset,
                selectedRegionId = selectedRegionId
            )
        }
    }
}

private fun DrawScope.drawBrain(
    brain: Brain,
    activity: RegionalActivity?,
    rotation: Offset,
    scale: Float,
    offset: Offset,
    selectedRegionId: String?
) {
    val centerX = size.width / 2f + offset.x
    val centerY = size.height / 2f + offset.y

    brain.regions.forEach { region ->
        if (!region.isVisible) return@forEach

        val projected = projectPoint(
            region.coordinates,
            rotation,
            scale,
            centerX,
            centerY
        )

        val activityLevel = activity?.regionActivities?.get(region.id) ?: 0f
        val isSelected = region.id == selectedRegionId

        val color = when {
            isSelected -> Color(0xFF4CAF50)
            region.isHighlighted -> Color(0xFFFF9800)
            activityLevel > 0.2f -> Color(0xFFE91E63).copy(alpha = activityLevel)
            else -> Color(0xFF2196F3)
        }

        val radius = region.boundingBox.size().let {
            ((it.x + it.y + it.z) / 3f) * scale * 2f
        }.coerceAtLeast(20f)

        drawCircle(
            color = color,
            radius = radius,
            center = Offset(projected.x, projected.y),
            alpha = if (isSelected) 1f else 0.7f
        )

        // Activity pulse effect
        if (activityLevel > 0.3f) {
            drawCircle(
                color = color,
                radius = radius * (1f + activityLevel * 0.5f),
                center = Offset(projected.x, projected.y),
                alpha = activityLevel * 0.3f
            )
        }
    }
}

/**
 * Simple 3D to 2D projection with rotation.
 */
private fun projectPoint(
    point: Coordinates3D,
    rotation: Offset,
    scale: Float,
    centerX: Float,
    centerY: Float
): Offset {
    // Rotate around Y axis
    val cosY = cos(rotation.y)
    val sinY = sin(rotation.y)
    val x1 = point.x * cosY - point.z * sinY
    val z1 = point.x * sinY + point.z * cosY

    // Rotate around X axis
    val cosX = cos(rotation.x)
    val sinX = sin(rotation.x)
    val y2 = point.y * cosX - z1 * sinX
    val z2 = point.y * sinX + z1 * cosX

    // Simple orthographic projection
    return Offset(
        centerX + x1 * scale,
        centerY + y2 * scale
    )
}
