package com.flylab.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flylab.domain.brain.Brain
import com.flylab.domain.brain.BrainRegion
import com.flylab.domain.neural.RegionalActivity
import com.flylab.ui.viewer.BrainViewer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrainExplorerScreen(
    brain: Brain,
    activity: RegionalActivity?,
    modifier: Modifier = Modifier
) {
    var selectedRegionId by remember { mutableStateOf<String?>(null) }
    var showRegionList by remember { mutableStateOf(false) }
    var showActivityOverlay by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FlyLab Brain Explorer") },
                actions = {
                    IconButton(onClick = { showActivityOverlay = !showActivityOverlay }) {
                        Icon(
                            imageVector = if (showActivityOverlay) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle activity overlay"
                        )
                    }
                    IconButton(onClick = { showRegionList = !showRegionList }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Show regions"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = modifier.padding(padding).fillMaxSize()) {
            BrainViewer(
                brain = brain,
                activity = if (showActivityOverlay) activity else null,
                selectedRegionId = selectedRegionId,
                onRegionSelected = { regionId ->
                    selectedRegionId = if (selectedRegionId == regionId) null else regionId
                },
                modifier = Modifier.fillMaxSize()
            )

            if (showRegionList) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    RegionList(
                        brain = brain,
                        activity = activity,
                        selectedRegionId = selectedRegionId,
                        onRegionSelected = { selectedRegionId = it }
                    )
                }
            }

            selectedRegionId?.let { regionId ->
                brain.regions.find { it.id == regionId }?.let { region ->
                    RegionInfoCard(
                        region = region,
                        activityLevel = activity?.regionActivities?.get(regionId),
                        onDismiss = { selectedRegionId = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .widthIn(max = 300.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RegionList(
    brain: Brain,
    activity: RegionalActivity?,
    selectedRegionId: String?,
    onRegionSelected: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Brain Regions (${brain.regions.size})",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(brain.regions) { region ->
            RegionListItem(
                region = region,
                activityLevel = activity?.regionActivities?.get(region.id),
                isSelected = region.id == selectedRegionId,
                onClick = { onRegionSelected(region.id) }
            )
        }
    }
}

@Composable
fun RegionListItem(
    region: BrainRegion,
    activityLevel: Float?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${region.abbreviation} - ${region.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                activityLevel?.let {
                    Text(
                        text = "Activity: ${(it * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (region.isHighlighted) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Highlighted",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun RegionInfoCard(
    region: BrainRegion,
    activityLevel: Float?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = region.name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }

            Text(
                text = region.abbreviation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Divider()

            Text(
                text = region.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Function:",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = region.function,
                style = MaterialTheme.typography.bodySmall
            )

            activityLevel?.let {
                Divider()
                Text(
                    text = "Current Activity: ${(it * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Evidence: ${region.evidenceLevel}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = "Source: ${region.source}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
