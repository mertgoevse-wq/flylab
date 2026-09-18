package com.flylab.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.NeuropilId
import com.flylab.render3d.Camera3D
import com.flylab.render3d.FlyMeshGeometry
import com.flylab.render3d.ProjectedPoint
import com.flylab.render3d.RenderLayers
import com.flylab.render3d.Vector3D
import com.flylab.sim.ConnectomeReference
import com.flylab.sim.SimulationSnapshot

/**
 * High-performance 3D Viewport rendering the anatomical fly and neural dynamics.
 */
@Composable
fun Viewport3DCanvas(
    snapshot: SimulationSnapshot,
    renderLayers: RenderLayers,
    onLayerChanged: (RenderLayers) -> Unit,
    modifier: Modifier = Modifier
) {
    var camera by remember {
        mutableStateOf(
            Camera3D(
                target = Vector3D(0f, 0.1f, 0.8f),
                distance = 6.5f,
                azimuthDegrees = 35.0f,
                elevationDegrees = 20.0f
            )
        )
    }

    val (anatomyPolygons, anatomyLines) = remember { FlyMeshGeometry.buildAnatomyGeometry() }
    val brainNeuropils = remember { FlyMeshGeometry.buildBrainNeuropils() }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // 1-finger / drag rotates azimuth and elevation
                    if (pan != Offset.Zero) {
                        camera = camera.orbit(
                            deltaAzimuthDeg = pan.x * 0.4f,
                            deltaElevationDeg = -pan.y * 0.4f
                        )
                    }
                    // Pinch to zoom
                    if (zoom != 1.0f) {
                        camera = camera.zoom(1.0f / zoom)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Render Exoskeleton & Cuticle
            if (renderLayers.showExoskeleton) {
                // Polygons sorted from furthest to nearest
                val sortedPolys = anatomyPolygons.mapNotNull { poly ->
                    val projVerts = poly.vertices.map { camera.project(it, width, height) }
                    if (projVerts.any { !it.isVisible }) null
                    else {
                        val meanDepth = projVerts.map { it.depthZ }.average().toFloat()
                        Triple(poly, projVerts, meanDepth)
                    }
                }.sortedByDescending { it.third }

                for ((poly, verts, _) in sortedPolys) {
                    val path = Path().apply {
                        moveTo(verts[0].screenX, verts[0].screenY)
                        for (i in 1 until verts.size) {
                            lineTo(verts[i].screenX, verts[i].screenY)
                        }
                        close()
                    }
                    drawPath(
                        path = path,
                        color = Color(poly.baseColorHex).copy(alpha = poly.alpha)
                    )
                }

                // Line contours (legs, wing veins, bristles)
                for (line in anatomyLines) {
                    val pStart = camera.project(line.start, width, height)
                    val pEnd = camera.project(line.end, width, height)
                    if (pStart.isVisible && pEnd.isVisible) {
                        drawLine(
                            color = Color(line.colorHex),
                            start = Offset(pStart.screenX, pStart.screenY),
                            end = Offset(pEnd.screenX, pEnd.screenY),
                            strokeWidth = line.strokeWidthDp * density,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 2. Render 3D Brain Neuropil Regions
            if (renderLayers.showBrainRegions) {
                for (marker in brainNeuropils) {
                    val pCenter = camera.project(marker.center, width, height)
                    if (!pCenter.isVisible) continue

                    val regionState = snapshot.regionStates[marker.neuropil]
                    val activation = regionState?.meanActivation ?: 0.0f
                    val pulseScale = 1.0f + (activation * 0.45f)

                    // Project radius to screen pixels
                    val pEdge = camera.project(marker.center + Vector3D(marker.radius * pulseScale, 0f, 0f), width, height)
                    val screenRadius = Math.abs(pEdge.screenX - pCenter.screenX).coerceIn(4f, 80f)

                    val baseColor = Color(marker.baseColorHex)
                    val activeColor = if (activation > 0.2f) Color(0xFFFBBF24) else baseColor // Gold glow on high activity

                    // Glowing outer aura
                    drawCircle(
                        color = activeColor.copy(alpha = 0.25f + activation * 0.4f),
                        radius = screenRadius * 1.35f,
                        center = Offset(pCenter.screenX, pCenter.screenY)
                    )

                    // Neuropil nucleus
                    drawCircle(
                        color = activeColor.copy(alpha = 0.85f),
                        radius = screenRadius,
                        center = Offset(pCenter.screenX, pCenter.screenY)
                    )

                    // Perturbation ring if optogenetically modified
                    if (regionState?.isPerturbed == true) {
                        drawCircle(
                            color = Color.Red,
                            radius = screenRadius * 1.5f,
                            center = Offset(pCenter.screenX, pCenter.screenY),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            // 3. Render Connectome Graph Synaptic Projections
            if (renderLayers.showConnectomeGraph) {
                val headCenter = Vector3D(0f, 0.1f, 1.6f)
                val firingRates = snapshot.firingRates

                for (syn in ConnectomeReference.SYNAPSES) {
                    val preNeuron = ConnectomeReference.getNeuron(syn.preNeuronId) ?: continue
                    val postNeuron = ConnectomeReference.getNeuron(syn.postNeuronId) ?: continue

                    val prePos = headCenter + Vector3D(preNeuron.posX * 0.5f, preNeuron.posY * 0.5f, preNeuron.posZ * 0.5f)
                    val postPos = headCenter + Vector3D(postNeuron.posX * 0.5f, postNeuron.posY * 0.5f, postNeuron.posZ * 0.5f)

                    val pPre = camera.project(prePos, width, height)
                    val pPost = camera.project(postPos, width, height)

                    if (pPre.isVisible && pPost.isVisible) {
                        val preRate = firingRates[syn.preNeuronId] ?: 0.0f
                        val overlay = snapshot.synapticOverlays[syn.synapseId]
                        val isDepressed = (overlay?.deltaWeight ?: 0f) < -0.05f

                        val strokeColor = when {
                            isDepressed -> Color(0xFFEF4444) // Red for LTD depressed synapse
                            preRate > 0.3f -> Color(0xFF10B981) // Emerald for active spike transmission
                            else -> Color(0xFF64748B).copy(alpha = 0.3f) // Slate for idle
                        }

                        val strokeWidth = if (preRate > 0.3f) 2.5.dp.toPx() else 1.0.dp.toPx()

                        drawLine(
                            color = strokeColor,
                            start = Offset(pPre.screenX, pPre.screenY),
                            end = Offset(pPost.screenX, pPost.screenY),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 4. Orientation HUD Indicator
            drawOrientationCompass(camera, width, height)
        }

        // Layer Filter Chips at Top Right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            FilterChip(
                selected = renderLayers.showExoskeleton,
                onClick = { onLayerChanged(renderLayers.copy(showExoskeleton = !renderLayers.showExoskeleton)) },
                label = { Text("Körper", fontSize = 11.sp) }
            )
            FilterChip(
                selected = renderLayers.showBrainRegions,
                onClick = { onLayerChanged(renderLayers.copy(showBrainRegions = !renderLayers.showBrainRegions)) },
                label = { Text("Gehirn", fontSize = 11.sp) },
                modifier = Modifier.padding(start = 4.dp)
            )
            FilterChip(
                selected = renderLayers.showConnectomeGraph,
                onClick = { onLayerChanged(renderLayers.copy(showConnectomeGraph = !renderLayers.showConnectomeGraph)) },
                label = { Text("Konnektom", fontSize = 11.sp) },
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

private fun DrawScope.drawOrientationCompass(camera: Camera3D, width: Float, height: Float) {
    val compassCenter = Offset(45.dp.toPx(), height - 45.dp.toPx())
    val compassRadius = 25.dp.toPx()

    drawCircle(
        color = Color(0xFFE2E8F0).copy(alpha = 0.7f),
        radius = compassRadius,
        center = compassCenter
    )

    // Heading axis projection
    val azRad = Math.toRadians(camera.azimuthDegrees.toDouble()).toFloat()
    val needleX = compassCenter.x + (Math.sin(azRad.toDouble()).toFloat() * (compassRadius * 0.75f))
    val needleY = compassCenter.y - (Math.cos(azRad.toDouble()).toFloat() * (compassRadius * 0.75f))

    drawLine(
        color = Color(0xFF059669),
        start = compassCenter,
        end = Offset(needleX, needleY),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
}
