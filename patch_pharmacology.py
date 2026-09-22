content = open("app/src/main/java/com/flylab/ui/components/PharmacologyPanel.kt").read()

imports = """import com.flylab.domain.model.NeuromodulatorType
import com.flylab.sim.SimulationSnapshot
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.model.ProvenanceLevel"""

content = content.replace("import com.flylab.domain.model.NeuromodulatorType\nimport com.flylab.sim.SimulationSnapshot", imports)

ui_part = """            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pharmakologie & Neuromodulatoren",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                ProvenanceBadge(
                    evidence = ScientificEvidence(
                        level = ProvenanceLevel.MODELLED,
                        citation = "Simulation Runtime Engine",
                        notes = "Pharmacological injections here are mathematical models, not direct measurements."
                    )
                )
            }
            Text("""

content = content.replace("""            Text(
                text = "Pharmakologie & Neuromodulatoren",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(""", ui_part)

open("app/src/main/java/com/flylab/ui/components/PharmacologyPanel.kt", "w").write(content)
