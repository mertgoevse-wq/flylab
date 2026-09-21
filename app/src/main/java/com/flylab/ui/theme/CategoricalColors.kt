package com.flylab.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Validated scientific data visualization palette for FlyLab.
 * Job-based categorizations to avoid rainbow aesthetics and maintain hierarchy.
 */

// Behaviors (Categorical)
val ColorBehaviorApproach = Color(0xFF059669) // Emerald
val ColorBehaviorAvoid = Color(0xFFDC2626)    // Red
val ColorBehaviorFeed = Color(0xFFD97706)     // Amber
val ColorBehaviorOrient = Color(0xFF2563EB)   // Blue
val ColorBehaviorExplore = Color(0xFF0D9488)  // Teal
val ColorBehaviorRest = Color(0xFF64748B)     // Slate
val ColorBehaviorGroom = Color(0xFF7C3AED)    // Violet

// Modulators (Categorical - Consistent mapping to biology equivalents)
val ColorModulatorOctopamine = Color(0xFF10B981) // Action/Stress (Green)
val ColorModulatorDopamine = Color(0xFF3B82F6)   // Reward/Aversive (Blue)
val ColorModulatorSerotonin = Color(0xFF8B5CF6)  // Modulatory state (Purple)

// Needs (Sequential/Diverging mapping)
val ColorNeedHunger = Color(0xFFD97706)      // Amber scale
val ColorNeedFatigue = Color(0xFF64748B)     // Slate scale
val ColorNeedHydration = Color(0xFF38BDF8)   // Sky scale

// Perturbation mapping
val ColorStatusGood = Color(0xFF059669)
val ColorStatusWarning = Color(0xFFF59E0B)
val ColorStatusCritical = Color(0xFFDC2626)
