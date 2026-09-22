import re

with open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt", "r") as f:
    text = f.read()

imports = """import com.flylab.sim.persistence.PersistenceManager
import androidx.compose.ui.platform.LocalContext
"""
text = text.replace("import kotlinx.coroutines.delay", "import kotlinx.coroutines.delay\n" + imports)

context_line = "    var currentTab by remember { mutableStateOf(0) }\n\n    val context = LocalContext.current"
text = text.replace("    var currentTab by remember { mutableStateOf(0) }", context_line)

save_load_callbacks = """                    onReset = {
                        isPlaying = false
                        engine.reset()
                        currentSnapshot = engine.history.last()
                    },
                    onSaveSession = {
                        PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                    },
                    onLoadSession = {
                        val session = PersistenceManager.loadReplaySession(context)
                        if (session != null && session.history.isNotEmpty()) {
                            isPlaying = false
                            engine.reset()
                            engine._setHistoryForReplay(session.history)
                            engine.seekToStep(session.history.last().step)
                            currentSnapshot = engine.history.last()
                        }
                    }"""

text = text.replace("""                    onReset = {
                        isPlaying = false
                        engine.reset()
                        currentSnapshot = engine.history.last()
                    }""", save_load_callbacks)

with open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt", "w") as f:
    f.write(text)
