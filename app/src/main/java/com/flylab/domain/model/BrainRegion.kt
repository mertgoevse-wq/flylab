package com.flylab.domain.model

/**
 * Standard Drosophila melanogaster adult brain neuropils.
 * Nomenclature matches the Insect Brain Name Working Group (Ito et al. 2014, Neuron).
 * Centroid coordinates normalized to the FlyWire/JRC2018 template brain space.
 */
enum class NeuropilId(
    val abbreviation: String,
    val standardName: String,
    val germanName: String,
    val primaryFunction: String,
    val centroidX: Float, // Normalized [-1.0 (left) .. 1.0 (right)]
    val centroidY: Float, // Normalized [-1.0 (ventral) .. 1.0 (dorsal)]
    val centroidZ: Float, // Normalized [-1.0 (posterior) .. 1.0 (anterior)]
    val approximateVolumeUm3: Float,
    val estimatedNeuronCount: Int,
    val evidence: ScientificEvidence
) {
    ANTENNAL_LOBE(
        abbreviation = "AL",
        standardName = "Antennal Lobe",
        germanName = "Antennallobus (Geruchszentrum 1. Ordnung)",
        primaryFunction = "Empfängt olfaktorische Signale der Antennen; ca. 54 Glomeruli pro Hemisphäre.",
        centroidX = 0.35f,
        centroidY = -0.30f,
        centroidZ = 0.65f,
        approximateVolumeUm3 = 180_000f,
        estimatedNeuronCount = 1_200,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Schlegel et al., Nature 2024 / FlyWire Consortium",
            doi = "10.1038/s41586-024-07763-9",
            datasetSource = "FlyWire Whole-Brain Connectome v783",
            notes = "Reconstructed glomerular partitions and projection neuron arborizations."
        )
    ),

    MUSHROOM_BODY(
        abbreviation = "MB",
        standardName = "Mushroom Body",
        germanName = "Pilzkörper (Assoziatives Lernen & Gedächtnis)",
        primaryFunction = "Zentrum für assoziatives olfaktorisches Lernen; ca. 2000 Kenyon-Zellen pro Hemisphäre.",
        centroidX = 0.40f,
        centroidY = 0.20f,
        centroidZ = 0.25f,
        approximateVolumeUm3 = 350_000f,
        estimatedNeuronCount = 2_200,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Aso et al., eLife 2014; Dorkenwald et al., Nature 2024",
            doi = "10.7554/eLife.04577",
            datasetSource = "FlyWire Mushroom Body Circuit",
            notes = "Kenyon cell populations, dopaminergic inputs (DANs), and output neurons (MBONs)."
        )
    ),

    LATERAL_HORN(
        abbreviation = "LH",
        standardName = "Lateral Horn",
        germanName = "Laterales Horn (Angeborene Geruchsreaktion)",
        primaryFunction = "Vermittelt angeborene Geruchsreaktionen (Anziehung vs. Meidung/Flucht).",
        centroidX = 0.65f,
        centroidY = 0.15f,
        centroidZ = 0.10f,
        approximateVolumeUm3 = 220_000f,
        estimatedNeuronCount = 1_400,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Frechter et al., eLife 2019",
            doi = "10.7554/eLife.44590",
            datasetSource = "FlyWire Lateral Horn Reconstruction",
            notes = "Stereotyped projection neuron connectivity to lateral horn output neurons."
        )
    ),

    CENTRAL_COMPLEX(
        abbreviation = "CX",
        standardName = "Central Complex",
        germanName = "Zentralkomplex (Navigation & Raumorientierung)",
        primaryFunction = "Kompasssystem (Ring-Neurone im Ellipsoid-Körper), Wegfindung, Schrittmacher.",
        centroidX = 0.00f, // Midline structure
        centroidY = 0.05f,
        centroidZ = 0.15f,
        approximateVolumeUm3 = 420_000f,
        estimatedNeuronCount = 3_000,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Hulse et al., eLife 2021",
            doi = "10.7554/eLife.66039",
            datasetSource = "hemibrain v1.2 / FlyWire CX",
            notes = "Toroid heading direction representation and path integration circuits."
        )
    ),

    OPTIC_LOBE(
        abbreviation = "OL",
        standardName = "Optic Lobe",
        germanName = "Optischer Lobus (Sehverarbeitung)",
        primaryFunction = "Verarbeitung visueller Reize: Lamina, Medulla, Lobula und Lobulaplatte.",
        centroidX = 0.85f,
        centroidY = -0.05f,
        centroidZ = 0.30f,
        approximateVolumeUm3 = 2_100_000f,
        estimatedNeuronCount = 60_000,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Fischbach & Dittrich, Cell Tissue Res 1989; Schlegel et al. 2024",
            notes = "Largest neuropil in the brain; motion detection (T4/T5 elementary motion detectors)."
        )
    ),

    SUBESOPHAGEAL_ZONE(
        abbreviation = "SEZ",
        standardName = "Subesophageal Zone",
        germanName = "Subösophageale Zone (Geschmack & Fütterung)",
        primaryFunction = "Primäres Geschmacks- und Fütterungszentrum; Rüssel-Motorsteuerung.",
        centroidX = 0.00f,
        centroidY = -0.65f,
        centroidZ = 0.35f,
        approximateVolumeUm3 = 310_000f,
        estimatedNeuronCount = 2_500,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Hampel et al., eLife 2020",
            doi = "10.7554/eLife.55727",
            notes = "Processes sweet, bitter, and water sensations and drives proboscis extension."
        )
    ),

    LATERAL_ACCESSORY_LOBE(
        abbreviation = "LAL",
        standardName = "Lateral Accessory Lobe",
        germanName = "Lateraler Akzessorischer Lobus (Vormotorische Bahn)",
        primaryFunction = "Überträgt Navigationssignale des Zentralkomplexes zu absteigenden VNC-Bahnen.",
        centroidX = 0.30f,
        centroidY = -0.10f,
        centroidZ = 0.05f,
        approximateVolumeUm3 = 140_000f,
        estimatedNeuronCount = 800,
        evidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Rayshubskiy et al., bioRxiv 2020; Hulse et al. 2021",
            notes = "Translates central complex heading commands into asymmetric motor drive."
        )
    );

    companion object {
        fun byAbbreviation(abbr: String): NeuropilId? {
            return entries.find { it.abbreviation.equals(abbr, ignoreCase = true) }
        }
    }
}

/**
 * Dynamic state of a brain region during simulation.
 */
data class BrainRegionState(
    val neuropil: NeuropilId,
    val meanActivation: Float = 0.0f, // [0.0 (silent) .. 1.0 (maximal burst)]
    val activeNeuronFraction: Float = 0.0f,
    val isPerturbed: Boolean = false, // True if user or experiment optogenetically silenced/stimulated
    val perturbationFactor: Float = 1.0f // 0.0 = completely silenced, >1.0 = stimulated
) {
    init {
        require(meanActivation in 0.0f..1.0f) { "Mean activation must be in [0, 1], was $meanActivation" }
        require(activeNeuronFraction in 0.0f..1.0f) { "Active fraction must be in [0, 1], was $activeNeuronFraction" }
    }
}
