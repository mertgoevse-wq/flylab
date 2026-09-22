#!/bin/bash
find app/src/main/java/com/flylab/ui/components -name "*.kt" -exec sed -i -E 's/(Button|FilledTonalButton|OutlinedButton|TextButton)\(([^)]*)\) \{/\1(\2, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)) {/g' {} +
