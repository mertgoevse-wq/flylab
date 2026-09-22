#!/bin/bash
find app/src/main/java/com/flylab/ui/components -name "*.kt" -exec sed -i -e 's/elevation = CardDefaults.cardElevation(defaultElevation = [0-9]\+\.dp)/border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(0.dp)/g' {} +

find app/src/main/java/com/flylab/ui/components -name "*.kt" -exec sed -i -e 's/elevation = CardDefaults.cardElevation([0-9]\+\.dp)/border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(0.dp)/g' {} +

# Add outlineVariant import if needed
# We assume it exists in colorScheme. If not, it defaults to outline.
