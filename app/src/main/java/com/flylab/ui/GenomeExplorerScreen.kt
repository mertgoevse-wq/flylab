package com.flylab.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flylab.data.genome.MockGenomeDatabase
import com.flylab.domain.genome.Chromosome
import com.flylab.domain.genome.Gene
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.genome.MutationExperiment
import com.flylab.domain.genome.Variant
import com.flylab.domain.genome.VariantType
import com.flylab.domain.genome.PhenotypePrediction
import com.flylab.domain.genome.PathwayEffect
import com.flylab.ui.components.ProvenanceBadge

@Composable
fun GenomeExplorerScreen() {
    var selectedChromosome by remember { mutableStateOf<Chromosome?>(null) }
    var selectedGene by remember { mutableStateOf<Gene?>(null) }
    var mutationExperiment by remember { mutableStateOf<MutationExperiment?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Column: Chromosomes & Genes
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            Text("Genome", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Chromosome Selector
            ScrollableTabRow(
                selectedTabIndex = MockGenomeDatabase.chromosomes.indexOf(selectedChromosome).coerceAtLeast(0),
                modifier = Modifier.fillMaxWidth()
            ) {
                MockGenomeDatabase.chromosomes.forEach { chr ->
                    Tab(
                        selected = selectedChromosome == chr,
                        onClick = {
                            selectedChromosome = chr
                            selectedGene = null
                            mutationExperiment = null
                        },
                        text = { Text(chr.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val activeChromosome = selectedChromosome ?: MockGenomeDatabase.chromosomes.first()
            val availableGenes = MockGenomeDatabase.getGenesForChromosome(activeChromosome.name)

            if (availableGenes.isEmpty()) {
                Text("No modeled genes on this chromosome yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(availableGenes) { gene ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedGene = gene
                                    mutationExperiment = null
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(gene.symbol, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(gene.fullName, style = MaterialTheme.typography.bodySmall)
                                Text("${gene.coordinate.startBp} - ${gene.coordinate.endBp}", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.width(1.dp).fillMaxHeight())

        // Right Column: Detail & Mutations
        Column(modifier = Modifier.weight(1.5f).padding(16.dp)) {
            val gene = selectedGene
            if (gene == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a gene to inspect.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                GeneDetailView(
                    gene = gene,
                    experiment = mutationExperiment,
                    onApplyMutation = {
                        mutationExperiment = createMockExperiment(gene)
                    }
                )
            }
        }
    }
}

@Composable
fun GeneDetailView(gene: Gene, experiment: MutationExperiment?, onApplyMutation: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(gene.symbol, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            ProvenanceBadge(evidence = gene.evidence)
        }
        Text(gene.fullName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Locus: chr${gene.coordinate.chromosome}:${gene.coordinate.startBp}-${gene.coordinate.endBp} (${gene.strand})")

        Spacer(modifier = Modifier.height(8.dp))
        Text(gene.description, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        if (experiment == null) {
            Button(onClick = onApplyMutation) {
                Text("Simulate Loss of Function Mutation")
            }
        } else {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Live Mutation Experiment", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Target: ${experiment.targetGene.symbol}")
                    Text("Change: ${experiment.appliedVariant.functionalImpact}")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (experiment.pathwayEffects.isNotEmpty()) {
                        Text("Pathway Effect:", fontWeight = FontWeight.Bold)
                        experiment.pathwayEffects.forEach { pathway ->
                            Text("- ${pathway.pathwayName} (${pathway.severity * 100}% severity) -> ${pathway.functionalImpact}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Predicted Phenotypes:", fontWeight = FontWeight.Bold)
                    experiment.predictedPhenotypes.forEach { pheno ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("- ${pheno.trait}: ${pheno.effectDescription}", modifier = Modifier.weight(1f))
                            ProvenanceBadge(evidence = pheno.evidence)
                        }
                    }
                }
            }
        }
    }
}

fun createMockExperiment(gene: Gene): MutationExperiment {
    val simEvidence = ScientificEvidence(
        level = ProvenanceLevel.SIMULATED,
        notes = "Rule-based simulated consequence."
    )
    val variant = Variant(
        id = "mut_${gene.symbol}",
        coordinate = gene.coordinate.copy(endBp = gene.coordinate.startBp + 1),
        referenceAllele = "A",
        alternateAllele = "-",
        type = VariantType.DELETION,
        functionalImpact = "Frameshift truncation (Loss of Function)",
        evidence = simEvidence
    )

    val effect = when (gene.symbol) {
        "w" -> PhenotypePrediction("Eye color", "White eyes (loss of pigment)", simEvidence)
        "dpp" -> PhenotypePrediction("Development", "Embryonic lethal or defective wings", simEvidence)
        "rut" -> PhenotypePrediction("Memory", "Defective olfactory associative learning", simEvidence)
        else -> PhenotypePrediction("Unknown", "No modeled phenotype", simEvidence)
    }

    val pathway = when (gene.symbol) {
        "w" -> PathwayEffect("ABC Transporter", "Blocks active transport of guanine and tryptophan into pigment cells", 0.95f, simEvidence)
        "dpp" -> PathwayEffect("TGF-beta signaling", "Disrupts smad activation gradient", 1.0f, simEvidence)
        "rut" -> PathwayEffect("cAMP signaling", "Reduces cAMP synthesis in Kenyon Cells during conditioning", 0.80f, simEvidence)
        else -> PathwayEffect("Unknown", "Unknown pathway", 0.0f, simEvidence)
    }

    return MutationExperiment(
        targetGene = gene,
        appliedVariant = variant,
        pathwayEffects = listOf(pathway),
        predictedPhenotypes = listOf(effect)
    )
}
