with open("docs/MILESTONES.md", "a") as f:
    f.write("\n## M14 - Scientific Reproducibility & Foundation for Multi-Fly\n**Ziel:** Experiment-Abstraktion (Experimentmodell), verbesserte Graphen-Architektur, Zeitreihen-Logging, Herkunftsnachweis-Ausbau (Provenance).\n**Umfang:**\n- Kanonisches Experimentmodell (`Subject`, `ExperimentRun`, etc).\n- Provenance auf 11 Kategorien (MEASURED bis UNKNOWN) erweitert.\n- Reusable Graph-Abstraktionen (GraphNode, NeuronNode, etc).\n- Zeitreihen/Event-Logging mit deterministischen Seeds gefestigt zur Reproduzierbarkeit.\n**Abnahme:** Tests bestehen.\n**Abhängigkeiten:** M13.\n")

with open("docs/ROADMAP.md", "a") as f:
    f.write("\n### M14 - Scientific Reproducibility Foundation\nVorbereitung auf P2 Scale (Multi-Fly, Swarm, Graphen).\n- Einheitliches Experiment-Abstraktionsmodell (Determinismus, Seeds).\n- Strikte Provenienz (Observed bis Hypothetical).\n- Graph-Abstraktionen (Nodes/Edges für Neuro-Modelle).\n- Zeitreihen- und Event-Logging (ExperimentJournal).\n")

