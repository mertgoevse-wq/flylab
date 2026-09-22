package com.flylab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.Neuron
import com.flylab.domain.model.NeuropilId
import com.flylab.sim.ConnectomeReference
import com.flylab.sim.SimulationSnapshot

@Composable
fun ConnectomeExplorerScreen(
    snapshot: SimulationSnapshot,
    onNeuronPerturbationChanged: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNeuropil by remember { mutableStateOf<NeuropilId?>(null) }
    var selectedNeuronId by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Text(
            text = "Connectome Explorer",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        
        Text(
            text = "Filter by Brain Region:",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Filter row
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedNeuropil == null,
                    onClick = { selectedNeuropil = null },
                    label = { Text("App") }
                )
            }
            items(NeuropilId.entries.toTypedArray()) { neuropil ->
                val hasNeurons = ConnectomeReference.NEURONS.any { it.neuropil == neuropil }
                if (hasNeurons) {
                    FilterChip(
                        selected = selectedNeuropil == neuropil,
                        onClick = { selectedNeuropil = neuropil },
                        label = { Text(neuropil.abbreviation) }
                    )
                }
            }
        }
        
        Divider()
        
        Row(modifier = Modifier.fillMaxSize()) {
            // Neuron List
            LazyColumn(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val filteredNeurons = ConnectomeReference.NEURONS.filter { 
                    selectedNeuropil == null || it.neuropil == selectedNeuropil 
                }.sortedBy { it.id }
                
                items(filteredNeurons) { neuron ->
                    val rate = snapshot.firingRates[neuron.id] ?: 0f
                    NeuronListItem(
                        neuron = neuron,
                        rate = rate,
                        isSelected = selectedNeuronId == neuron.id,
                        onClick = { selectedNeuronId = neuron.id }
                    )
                }
            }
            
            // Detail Panel
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant)) {
                if (selectedNeuronId != null) {
                    val neuron = ConnectomeReference.getNeuron(selectedNeuronId!!)
                    if (neuron != null) {
                        val rate = snapshot.firingRates[neuron.id] ?: 0f
                        NeuronDetailPanel(
                            neuron = neuron,
                            rate = rate,
                            snapshot = snapshot,
                            onPerturbationChanged = { factor ->
                                onNeuronPerturbationChanged(neuron.id, factor)
                            }
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a neuron to inspect", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun NeuronListItem(neuron: Neuron, rate: Float, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(neuron.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(neuron.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        // Activity indicator
        Box(
            modifier = Modifier.width(48.dp).height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(rate)
                    .background(if (rate > 0.5f) Color.Green else Color.LightGray)
            )
        }
    }
}

@Composable
fun NeuronDetailPanel(
    neuron: Neuron, 
    rate: Float,
    snapshot: SimulationSnapshot,
    onPerturbationChanged: (Float) -> Unit
) {
    // We don't have the exact current perturbation factor passed inside the snapshot, so we'll just keep a local state that updates.
    // In a real app we'd want to read it from SimulationEngine or Snapshot. Let's add it to Snapshot if we can, 
    // or just assume 1.0f as default. 
    val localPerturbation = snapshot.neuronPerturbations[neuron.id] ?: 1.0f

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(neuron.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("ID: ${neuron.id}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Realtime Activity: ${(rate * 100).toInt()}%", fontWeight = FontWeight.Bold)
                LinearProgressIndicator(
                    progress = { rate },
                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(vertical = 8.dp),
                    color = if (rate > 0.5f) Color.Green else Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Optogenetic Perturbation", fontWeight = FontWeight.Bold)
                Text("Multiplier: ${String.format("%.1fx", localPerturbation)}", fontSize = 12.sp)
                Slider(
                    value = localPerturbation,
                    onValueChange = { 
                        
                        onPerturbationChanged(it)
                    },
                    valueRange = 0.0f..3.0f,
                    steps = 30
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Silenced (0x)", fontSize = 10.sp)
                    Text("Normal (1x)", fontSize = 10.sp)
                    Text("Excited (3x)", fontSize = 10.sp)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { 
                         
                        onPerturbationChanged(1.0f)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset (1.0x)")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Synaptic Connections", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        val synapsesOut = ConnectomeReference.getSynapsesFrom(neuron.id)
        if (synapsesOut.isNotEmpty()) {
            Text("Outputs (${synapsesOut.size}):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            LazyColumn {
                items(synapsesOut) { syn ->
                    val w = snapshot.synapticOverlays[syn.synapseId]?.currentWeight ?: syn.baselineWeight
                    Text("-> ${syn.postNeuronId} (${syn.neurotransmitter.name}) wt: ${String.format("%.2f", w)}", fontSize = 12.sp)
                }
            }
        } else {
            Text("No outgoing connections in this subset.", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        
        val synapsesIn = ConnectomeReference.getSynapsesTo(neuron.id)
        if (synapsesIn.isNotEmpty()) {
            Text("Inputs (${synapsesIn.size}):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            LazyColumn {
                items(synapsesIn) { syn ->
                    val w = snapshot.synapticOverlays[syn.synapseId]?.currentWeight ?: syn.baselineWeight
                    Text("<- ${syn.preNeuronId} (${syn.neurotransmitter.name}) wt: ${String.format("%.2f", w)}", fontSize = 12.sp)
                }
            }
        } else {
            Text("No incoming connections in this subset.", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
