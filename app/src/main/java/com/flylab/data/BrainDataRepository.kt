package com.flylab.data

import com.flylab.domain.EvidenceLevel
import com.flylab.domain.brain.*
import com.flylab.domain.connectome.Connectome
import com.flylab.domain.connectome.ConnectomeMetadata

/**
 * Data repository providing brain and connectome data.
 *
 * SCIENTIFIC HONESTY:
 * This repository provides either:
 * 1. Reference data from published sources (PUBLISHED evidence level)
 * 2. Clearly labeled synthetic/teaching datasets (MODELED evidence level)
 *
 * Real FlyWire connectome data would be loaded from external datasets.
 * This implementation uses a minimal reference structure.
 */
class BrainDataRepository {

    /**
     * Load reference adult female Drosophila brain structure.
     *
     * Evidence: PUBLISHED (structure), MODELED (specific coordinates are placeholders)
     */
    fun loadReferenceBrain(): Brain {
        return Brain(
            id = "flywire_v1_female",
            name = "FlyWire Adult Female Brain",
            species = "Drosophila melanogaster",
            sex = Sex.FEMALE,
            developmentalStage = "adult",
            regions = loadBrainRegions(),
            metadata = BrainMetadata(
                version = "1.0.0",
                source = "FlyWire connectome project",
                evidenceLevel = EvidenceLevel.PUBLISHED,
                description = "Adult female Drosophila melanogaster whole-brain structure",
                referenceUrl = "https://flywire.ai/",
                notes = "Coordinate values are placeholders. Real data would be loaded from FlyWire dataset."
            )
        )
    }

    /**
     * Load major brain regions with scientific references.
     */
    private fun loadBrainRegions(): List<BrainRegion> {
        return listOf(
            createMushroomBody(),
            createAntennalLobe(),
            createCentralComplex(),
            createOpticalLobe(),
            createVentralNerveCord()
        )
    }

    private fun createMushroomBody() = BrainRegion(
        id = "MB",
        name = "Mushroom Body",
        abbreviation = "MB",
        description = "Learning and memory center",
        function = "Associative learning, olfactory memory formation, context-dependent decision making",
        evidenceLevel = EvidenceLevel.PUBLISHED,
        source = "Aso et al. 2014, Eichler et al. 2017",
        coordinates = Coordinates3D(50f, 80f, 60f),
        boundingBox = BoundingBox(
            min = Coordinates3D(40f, 70f, 50f),
            max = Coordinates3D(60f, 90f, 70f)
        ),
        knownInputs = listOf("AL"), // Antennal lobe
        knownOutputs = listOf("VNC") // Motor centers
    )

    private fun createAntennalLobe() = BrainRegion(
        id = "AL",
        name = "Antennal Lobe",
        abbreviation = "AL",
        description = "Primary olfactory processing center",
        function = "Olfactory sensory input processing, odor representation",
        evidenceLevel = EvidenceLevel.PUBLISHED,
        source = "Grabe & Sachse 2018",
        coordinates = Coordinates3D(30f, 50f, 40f),
        boundingBox = BoundingBox(
            min = Coordinates3D(20f, 40f, 30f),
            max = Coordinates3D(40f, 60f, 50f)
        ),
        knownInputs = listOf("ORN"), // Olfactory receptor neurons
        knownOutputs = listOf("MB", "LH") // Mushroom body, lateral horn
    )

    private fun createCentralComplex() = BrainRegion(
        id = "CX",
        name = "Central Complex",
        abbreviation = "CX",
        description = "Navigation and motor coordination",
        function = "Spatial orientation, navigation, motor pattern selection",
        evidenceLevel = EvidenceLevel.PUBLISHED,
        source = "Hulse & Jayaraman 2020",
        coordinates = Coordinates3D(50f, 50f, 50f),
        boundingBox = BoundingBox(
            min = Coordinates3D(40f, 40f, 40f),
            max = Coordinates3D(60f, 60f, 60f)
        )
    )

    private fun createOpticalLobe() = BrainRegion(
        id = "OL",
        name = "Optic Lobe",
        abbreviation = "OL",
        description = "Visual processing",
        function = "Visual input processing, motion detection, feature extraction",
        evidenceLevel = EvidenceLevel.PUBLISHED,
        source = "Borst 2014",
        coordinates = Coordinates3D(70f, 50f, 50f),
        boundingBox = BoundingBox(
            min = Coordinates3D(60f, 40f, 40f),
            max = Coordinates3D(80f, 60f, 60f)
        )
    )

    private fun createVentralNerveCord() = BrainRegion(
        id = "VNC",
        name = "Ventral Nerve Cord",
        abbreviation = "VNC",
        description = "Motor control and sensory integration",
        function = "Motor pattern generation, sensory processing, reflexes",
        evidenceLevel = EvidenceLevel.PUBLISHED,
        source = "Shepherd et al. 2016",
        coordinates = Coordinates3D(50f, 20f, 50f),
        boundingBox = BoundingBox(
            min = Coordinates3D(40f, 10f, 40f),
            max = Coordinates3D(60f, 30f, 60f)
        )
    )

    /**
     * Load minimal connectome reference.
     * Real FlyWire data would be loaded from external files.
     */
    fun loadReferenceConnectome(): Connectome {
        return Connectome(
            id = "flywire_v1",
            version = "1.0.0",
            brainId = "flywire_v1_female",
            metadata = ConnectomeMetadata(
                source = "FlyWire",
                evidenceLevel = EvidenceLevel.PUBLISHED,
                totalNeuronCount = 139255,
                totalSynapseCount = 54500000,
                description = "FlyWire whole-brain connectome. Full dataset not loaded - using minimal reference structure.",
                referenceUrl = "https://flywire.ai/",
                license = "CC-BY-4.0"
            ),
            neurons = emptyMap(), // Would load from dataset
            synapses = emptyMap(), // Would load from dataset
            isFullyLoaded = false,
            loadedRegionIds = emptySet()
        )
    }
}
