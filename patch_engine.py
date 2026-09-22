with open("app/src/main/java/com/flylab/sim/SimulationEngine.kt", "r") as f:
    text = f.read()

setter = """    fun reset() {
"""
replacement = """    fun _setHistoryForReplay(newHistory: List<SimulationSnapshot>) {
        _history.clear()
        _history.addAll(newHistory)
    }

    fun reset() {"""
text = text.replace(setter, replacement)

with open("app/src/main/java/com/flylab/sim/SimulationEngine.kt", "w") as f:
    f.write(text)
