#!/bin/bash
find app/src/main/java/com/flylab/ui/components -name "*.kt" -exec sed -i 's/border =.*, shape = .*, border = .*, shape = .*, elevation = .*/border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)/g' {} +
