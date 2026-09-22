import re

with open("app/src/main/java/com/flylab/ui/components/ExperimentControlPanel.kt", "r") as f:
    text = f.read()

text = text.replace("onReset: () -> Unit,", "onReset: () -> Unit,\n    onSaveSession: () -> Unit = {},\n    onLoadSession: () -> Unit = {},")

buttons_replacement = """                    OutlinedButton(onClick = onReset, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)) {
                        Text("Reset", fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    
                    OutlinedButton(onClick = onSaveSession, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)) {
                        Text("Save", fontSize = 11.sp)
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    OutlinedButton(onClick = onLoadSession, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)) {
                        Text("Load", fontSize = 11.sp)
                    }"""
text = text.replace("""                    OutlinedButton(onClick = onReset, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)) {
                        Text("Reset", fontSize = 11.sp)
                    }""", buttons_replacement)

with open("app/src/main/java/com/flylab/ui/components/ExperimentControlPanel.kt", "w") as f:
    f.write(text)
