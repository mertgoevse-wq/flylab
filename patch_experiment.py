import re

with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "r") as f:
    text = f.read()

text = text.replace("val initialFly: Fly = Fly()", "val subject: com.flylab.domain.model.Subject = com.flylab.domain.model.Fly()")

with open("app/src/main/java/com/flylab/experiment/ExperimentScenario.kt", "w") as f:
    f.write(text)

with open("app/src/main/java/com/flylab/experiment/ExperimentRunner.kt", "r") as f:
    text_runner = f.read()

# in ExperimentRunner, replace configuration.initialFly with configuration.subject as com.flylab.domain.model.Fly
text_runner = text_runner.replace("configuration.initialFly", "(configuration.subject as com.flylab.domain.model.Fly)")

with open("app/src/main/java/com/flylab/experiment/ExperimentRunner.kt", "w") as f:
    f.write(text_runner)

with open("app/src/main/java/com/flylab/experiment/ExperimentJournalEntry.kt", "r") as f:
    text_journal = f.read()

text_journal = text_journal.replace("configuration.initialFly", "configuration.subject")

with open("app/src/main/java/com/flylab/experiment/ExperimentJournalEntry.kt", "w") as f:
    f.write(text_journal)

