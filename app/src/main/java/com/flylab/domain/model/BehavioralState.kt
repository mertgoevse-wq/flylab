package com.flylab.domain.model

/**
 * Observable behavioral repertoire of the fly.
 * Directly corresponds to SIMULATION_MODEL.md § 5.
 */
enum class BehaviorType(
    val germanName: String,
    val description: String,
    val evidence: ScientificEvidence
) {
    RESTING(
        germanName = "Ruhen / Schlafzustand",
        description = "Bewegungsloser Zustand, verringerte sensorische Reaktivität.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hendricks et al. (2000) Neuron 25:295-307.",
            notes = "Drosophila behavioral quiescence exhibiting sleep homeostatic rebound."
        )
    ),

    EXPLORING(
        germanName = "Erkunden (Spontanes Laufen)",
        description = "Freies Suchverhalten im Raum mit stochastischen Richtungswechseln.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Martin (2004) J Neurogenet 18:83-102.",
            notes = "Spontaneous exploratory walking bouts with wall-following (thigmotaxis)."
        )
    ),

    ORIENTING(
        germanName = "Orientieren (Körperdrehung)",
        description = "Aktive Ausrichtung der Längsachse auf eine Geruchs- oder Lichtquelle.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Gomez-Marin et al. (2011) Nat Commun 2:441.",
            notes = "Active angular reorientation via head and body saccades."
        )
    ),

    APPROACHING(
        germanName = "Nähern (Attraktion)",
        description = "Zielgerichtete Vorwärtsbewegung entlang eines positiven Geruchsgradienten.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Steck et al. (2012) eLife 1:e00040.",
            notes = "Chemotactic tracking of fermenting food odors."
        )
    ),

    AVOIDING(
        germanName = "Meiden / Fliehen (Aversion)",
        description = "Abwenden und Beschleunigen weg von Toxin-Gerüchen oder Hitze.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Stensmyr et al. (2012) Cell 151:1345-1357.",
            notes = "Robust escape response triggered by aversive odorants (e.g. geosmin)."
        )
    ),

    FEEDING(
        germanName = "Essen / Proboscis-Extension",
        description = "Ausfahren des Saugrüssels und Aufnahme von Nährstoffen bei Zuckerkontakt.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Dethier (1976) The Hungry Fly; Flood et al. (2013) Nature.",
            notes = "Proboscis Extension Reflex (PER) driven by labellar gustatory receptor neurons."
        )
    ),

    GROOMING(
        germanName = "Putzen (Antennen-/Flügelpflege)",
        description = "Stereotype Putzbewegungen der Vorderbeine über Kopf und Antennen.",
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Seeds et al. (2014) eLife 3:e02951.",
            notes = "Hierarchical grooming motor program triggered by mechanosensory dust."
        )
    )
}

/**
 * Internal motivational drives and physiological needs.
 */
data class PhysiologicalNeeds(
    val hunger: Float = 0.5f,   // [0.0 (fully fed) .. 1.0 (starving)]
    val fatigue: Float = 0.2f,  // [0.0 (fresh) .. 1.0 (exhausted)]
    val arousal: Float = 0.3f,  // [0.0 (calm/sleeping) .. 1.0 (hyperactive/alarmed)]
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MODELLED,
        citation = "FlyLab Motivational Drive Model",
        notes = "Continuous motivational variables regulating behavioral threshold selection."
    )
) : ProvenanceTagged {
    init {
        require(hunger in 0.0f..1.0f) { "Hunger must be in [0, 1], was $hunger" }
        require(fatigue in 0.0f..1.0f) { "Fatigue must be in [0, 1], was $fatigue" }
        require(arousal in 0.0f..1.0f) { "Arousal must be in [0, 1], was $arousal" }
    }

    fun step(isMoving: Boolean, isFeeding: Boolean, dtSeconds: Float): PhysiologicalNeeds {
        val hungerDelta = if (isFeeding) -0.25f * dtSeconds else 0.015f * dtSeconds
        val fatigueDelta = if (isMoving) 0.03f * dtSeconds else -0.05f * dtSeconds
        val arousalDelta = if (isMoving) 0.01f * dtSeconds else -0.04f * dtSeconds

        return copy(
            hunger = (hunger + hungerDelta).coerceIn(0.0f, 1.0f),
            fatigue = (fatigue + fatigueDelta).coerceIn(0.0f, 1.0f),
            arousal = (arousal + arousalDelta).coerceIn(0.0f, 1.0f)
        )
    }
}

/**
 * Complete behavioral and spatial status of the fly.
 */
data class BehavioralState(
    val activeBehavior: BehaviorType = BehaviorType.EXPLORING,
    val needs: PhysiologicalNeeds = PhysiologicalNeeds(),
    val posXmm: Float = 0.0f,         // 2D Arena coordinates
    val posYmm: Float = 0.0f,
    val headingRadians: Float = 0.0f, // Angle in radians (0 = pointing +X)
    val distanceTraveledMm: Float = 0.0f,
    val foodConsumedUnits: Float = 0.0f,
    override val evidence: ScientificEvidence = ScientificEvidence.SIMULATED_RUNTIME_OVERLAY
) : ProvenanceTagged {
    fun updateSpatial(command: MotorCommand, dtSeconds: Float, arenaRadiusMm: Float): BehavioralState {
        var newHeading = (headingRadians + command.angularVelocityRadS * dtSeconds) % (2.0f * Math.PI.toFloat())
        if (newHeading < 0.0f) newHeading += 2.0f * Math.PI.toFloat()

        val dist = command.forwardVelocityMmS * dtSeconds
        var newX = posXmm + (Math.cos(newHeading.toDouble()).toFloat() * dist)
        var newY = posYmm + (Math.sin(newHeading.toDouble()).toFloat() * dist)

        // Arena boundary collision reflection
        val currentDistFromOrigin = Math.hypot(newX.toDouble(), newY.toDouble()).toFloat()
        if (currentDistFromOrigin > arenaRadiusMm) {
            val scale = arenaRadiusMm / currentDistFromOrigin
            newX *= scale
            newY *= scale
            // Rebound heading towards center
            newHeading = (Math.atan2(-newY.toDouble(), -newX.toDouble())).toFloat()
        }

        val feedingDelta = if (activeBehavior == BehaviorType.FEEDING) 0.1f * dtSeconds else 0.0f

        return copy(
            posXmm = newX,
            posYmm = newY,
            headingRadians = newHeading,
            distanceTraveledMm = distanceTraveledMm + Math.abs(dist),
            foodConsumedUnits = foodConsumedUnits + feedingDelta,
            needs = needs.step(
                isMoving = Math.abs(command.forwardVelocityMmS) > 1.0f,
                isFeeding = activeBehavior == BehaviorType.FEEDING,
                dtSeconds = dtSeconds
            )
        )
    }
}
