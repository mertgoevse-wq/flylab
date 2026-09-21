with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "r") as f:
    text = f.read()

text = text.replace("data class ExperimentConfiguration(", "data class ExperimentConfiguration(")
text = text.replace(") : ProvenanceTagged {", ") : Experiment {\n    override val subjects: List<com.flylab.domain.model.Subject> get() = listOf(subject)\n    override val environment: Environment get() = initialEnvironment")
text = text.replace("override val evidence: ScientificEvidence = ", "override val evidence: ScientificEvidence = ")

with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "w") as f:
    f.write(text)

