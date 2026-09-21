with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "r") as f:
    text = f.read()

text = text.replace("val id: String,", "override val id: String,")
text = text.replace("val title: String,", "override val title: String,")
text = text.replace("val hypothesis: String = \"\",", "override val hypothesis: String = \"\",")
text = text.replace("val seed: Long = 42L,", "override val seed: Long = 42L,")
text = text.replace("val datasetVersion: String = \"FlyWire v783-slice1\",", "override val datasetVersion: String = \"FlyWire v783-slice1\",")
text = text.replace("val modelVersion: String = \"FlyLab SimCore v1.0\",", "override val modelVersion: String = \"FlyLab SimCore v1.0\",")

with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "w") as f:
    f.write(text)
