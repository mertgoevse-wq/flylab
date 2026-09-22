content = open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt").read()

new_imports = """import com.flylab.sim.persistence.PersistenceManager
import com.flylab.sim.persistence.OnlinePersistenceManager
import com.flylab.sim.persistence.SaveSession
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext"""

content = content.replace("import com.flylab.sim.persistence.PersistenceManager\nimport androidx.compose.ui.platform.LocalContext", new_imports)

scope_decl = """    val context = LocalContext.current
    val scope = rememberCoroutineScope()"""
    
content = content.replace("    val context = LocalContext.current", scope_decl)

save_logic = """                    onSaveSession = {
                        PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                        // Optional online sync (non-blocking)
                        val sessionData = SaveSession(experimentId = currentConfig.id, history = engine.history)
                        scope.launch {
                            OnlinePersistenceManager.syncSessionToCloud(sessionData)
                        }
                    },"""
content = content.replace("""                    onSaveSession = {
                        PersistenceManager.saveReplaySession(context, engine.history, currentConfig.id)
                    },""", save_logic)

open("app/src/main/java/com/flylab/ui/FlyLabRootScreen.kt", "w").write(content)
