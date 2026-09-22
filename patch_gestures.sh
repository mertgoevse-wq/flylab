#!/bin/bash
sed -i '/detectTransformGestures { _, pan, zoom, _ ->/,/}/c\
                detectTransformGestures { centroid, pan, zoom, rotation ->\
                    // ... Need to check pointer count, but detectTransformGestures does not provide it directly.\
                }' app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt
