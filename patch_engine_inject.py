content = open("app/src/main/java/com/flylab/sim/SimulationEngine.kt").read()

new_fun = """
    fun injectNeuromodulator(type: NeuromodulatorType, amount: Float) {
        val currentMod = currentFly.neuromodulators[type]
        if (currentMod != null) {
            val updatedMap = currentFly.neuromodulators.toMutableMap()
            updatedMap[type] = currentMod.inject(amount)
            currentFly = currentFly.copy(neuromodulators = updatedMap)
        }
    }

    fun setRegionPerturbation"""

content = content.replace("    fun setRegionPerturbation", new_fun)
open("app/src/main/java/com/flylab/sim/SimulationEngine.kt", "w").write(content)
