#!/bin/bash
sed -i 's/import androidx.compose.foundation.gestures.detectTransformGestures/import androidx.compose.foundation.gestures.detectTransformGestures\nimport androidx.compose.foundation.gestures.detectTapGestures/' app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt
sed -i '/\.pointerInput(Unit) {/c\
            .pointerInput(Unit) {\
                detectTapGestures(\
                    onTap = { offset -> /* Select / Tap */ },\
                    onLongPress = { offset -> /* Inspect */ }\
                )\
            }\
            .pointerInput(Unit) {' app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt
