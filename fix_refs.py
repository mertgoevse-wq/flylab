import os

for root, _, files in os.walk("app/src"):
    for file in files:
        if file.endswith(".kt"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            if "initialFly" in content:
                # We need to be careful: in SimulationEngine it's still initialFly
                # But when calling ExperimentConfiguration(initialFly=...) we want subject=...
                # Let's just do targeted replaces.
                if "LearningExperimentProtocol.kt" in path:
                    content = content.replace("initialFly = Fly(", "subject = Fly(")
                elif "FlyLabRootScreen.kt" in path:
                    content = content.replace("config.initialFly", "(config.subject as Fly)")
                
                with open(path, "w") as f:
                    f.write(content)
