package com.flylab.domain.model

/**
 * Anatomical organization of Drosophila melanogaster.
 * Based on published morphological standards (Hartenstein 1993, FlyBase anatomy ontology).
 */
enum class AnatomicalRegion(
    val scientificName: String,
    val germanName: String,
    val defaultRelativeZ: Float, // Anterior-Posterior axis (-1.0 = posterior, +1.0 = anterior)
    val evidence: ScientificEvidence
) {
    HEAD(
        scientificName = "Caput",
        germanName = "Kopf",
        defaultRelativeZ = 0.75f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hartenstein, V. (1993). Atlas of Drosophila Development.",
            notes = "Houses the central brain, compound eyes, ocelli, antennae, and proboscis."
        )
    ),
    ANTENNA(
        scientificName = "Antenna",
        germanName = "Antenne",
        defaultRelativeZ = 0.95f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Stocker, R.F. (1994). The organization of the antennal system of Drosophila.",
            notes = "Primary olfactory and mechanosensory (Johnston's organ) structure."
        )
    ),
    COMPOUND_EYE(
        scientificName = "Oculus compositus",
        germanName = "Komplexauge",
        defaultRelativeZ = 0.78f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Ready, D.F. et al. (1976). Development of the Drosophila retina.",
            notes = "Compound eyes consisting of approximately 750-800 ommatidia per side."
        )
    ),
    PROBOSCIS(
        scientificName = "Proboscis",
        germanName = "Rüssel / Mundwerkzeuge",
        defaultRelativeZ = 0.70f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Stocker, R.F. (1994). Cell Tissue Res.",
            notes = "Feeding apparatus equipped with gustatory receptor neurons."
        )
    ),
    THORAX(
        scientificName = "Thorax",
        germanName = "Brustabschnitt (Thorax)",
        defaultRelativeZ = 0.15f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hartenstein, V. (1993). Atlas of Drosophila Development.",
            notes = "Houses the ventral nerve cord (VNC), flight muscles, and leg attachments."
        )
    ),
    WINGS(
        scientificName = "Alae",
        germanName = "Flügelpaar",
        defaultRelativeZ = 0.10f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Dickinson, M.H. et al. (1999). Science 284:1954-1960.",
            notes = "Dipteran single wing pair beat frequency ~200-220 Hz."
        )
    ),
    LEGS(
        scientificName = "Pedes",
        germanName = "Sechs Laufbeine (T1-T3)",
        defaultRelativeZ = 0.05f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Bidaye, S.S. et al. (2014). Science 344:97-101.",
            notes = "Hexapod coordination: prothoracic, mesothoracic, and metathoracic legs."
        )
    ),
    ABDOMEN(
        scientificName = "Abdomen",
        germanName = "Hinterleib (Abdomen)",
        defaultRelativeZ = -0.55f,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hartenstein, V. (1993). Atlas of Drosophila Development.",
            notes = "Houses digestive, reproductive, fat body, and circulatory organ systems."
        )
    )
}

/**
 * High-level anatomical state of an individual fly.
 */
data class FlyAnatomy(
    val bodyLengthMm: Float = 2.5f,
    val wingSpanMm: Float = 5.0f,
    val activeRegions: Set<AnatomicalRegion> = AnatomicalRegion.entries.toSet(),
    val organs: List<Organ> = emptyList(),
    override val evidence: ScientificEvidence = ScientificEvidence(
        level = ProvenanceLevel.MEASURED,
        citation = "Markow & O'Grady (2005). Drosophila: A Guide to Species Identification and Use.",
        notes = "Standard wild-type Canton-S adult female dimensions: ~2.5 mm length."
    )
) : ProvenanceTagged {
    init {
        require(bodyLengthMm > 0f) { "Body length must be positive" }
        require(wingSpanMm > 0f) { "Wingspan must be positive" }
    }
}
