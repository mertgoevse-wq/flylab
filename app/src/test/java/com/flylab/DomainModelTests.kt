package com.flylab.domain

import org.junit.Test
import org.junit.Assert.*
import com.flylab.domain.brain.*
import com.flylab.domain.connectome.*
import com.flylab.domain.plasticity.*
import com.flylab.domain.timeline.*
import com.flylab.domain.memory.*
import com.flylab.domain.neural.*

class BrainModelTest {

    @Test
    fun testBrainCreation() {
        val brain = Brain(
            id = "test_brain",
            name = "Test Brain",
            sex = Sex.FEMALE,
            regions = emptyList(),
            metadata = BrainMetadata(
                version = "1.0",
                source = "test",
                evidenceLevel = EvidenceLevel.MODELED,
                description = "Test brain"
            )
        )

        assertEquals("test_brain", brain.id)
        assertEquals(Sex.FEMALE, brain.sex)
        assertEquals("Drosophila melanogaster", brain.species)
    }

    @Test
    fun testBrainRegion() {
        val region = BrainRegion(
            id = "MB",
            name = "Mushroom Body",
            abbreviation = "MB",
            description = "Learning center",
            function = "Memory formation",
            evidenceLevel = EvidenceLevel.PUBLISHED,
            source = "Test",
            coordinates = Coordinates3D(50f, 80f, 60f),
            boundingBox = BoundingBox(
                min = Coordinates3D(40f, 70f, 50f),
                max = Coordinates3D(60f, 90f, 70f)
            )
        )

        assertEquals("MB", region.id)
        assertTrue(region.evidenceLevel.isScientificallyGrounded())
    }
}

class ConnectomeTest {

    @Test
    fun testNeuronCreation() {
        val neuron = Neuron(
            id = "n001",
            name = "Test Neuron",
            type = "projection",
            regionId = "MB",
            coordinates = Coordinates3D(50f, 80f, 60f),
            evidenceLevel = EvidenceLevel.PUBLISHED,
            source = "FlyWire"
        )

        assertEquals("n001", neuron.id)
        assertEquals("MB", neuron.regionId)
    }

    @Test
    fun testSynapseCreation() {
        val synapse = Synapse(
            id = "s001",
            presynapticNeuronId = "n001",
            postsynapticNeuronId = "n002",
            coordinates = Coordinates3D(50f, 80f, 60f),
            evidenceLevel = EvidenceLevel.PUBLISHED,
            source = "FlyWire",
            baselineWeight = 0.5f
        )

        assertEquals(0.5f, synapse.baselineWeight, 0.001f)
        assertEquals(0.5f, synapse.currentWeight, 0.001f)
    }

    @Test
    fun testConnectionCreation() {
        val synapses = listOf(
            Synapse(
                id = "s001",
                presynapticNeuronId = "n001",
                postsynapticNeuronId = "n002",
                coordinates = Coordinates3D(50f, 80f, 60f),
                evidenceLevel = EvidenceLevel.PUBLISHED,
                source = "test",
                baselineWeight = 0.3f
            ),
            Synapse(
                id = "s002",
                presynapticNeuronId = "n001",
                postsynapticNeuronId = "n002",
                coordinates = Coordinates3D(51f, 81f, 61f),
                evidenceLevel = EvidenceLevel.PUBLISHED,
                source = "test",
                baselineWeight = 0.2f
            )
        )

        val connection = Connection(
            sourceNeuronId = "n001",
            targetNeuronId = "n002",
            synapses = synapses,
            totalWeight = 0.5f,
            evidenceLevel = EvidenceLevel.PUBLISHED
        )

        assertEquals(2, connection.synapseCount)
        assertEquals(0.5f, connection.totalWeight, 0.001f)
    }
}

class PlasticityTest {

    @Test
    fun testRewardModulatedHebbian() {
        val rule = RewardModulatedHebbian(learningRate = 0.01f)

        val deltaWeight = rule.computeWeightChange(
            presynapticActivity = 1.0f,
            postsynapticActivity = 1.0f,
            rewardSignal = 1.0f,
            currentWeight = 0.5f
        )

        assertEquals(0.01f, deltaWeight, 0.001f)
        assertEquals(EvidenceLevel.MODELED, rule.evidenceLevel)
    }

    @Test
    fun testPlasticityEngine() {
        val engine = PlasticityEngine(
            rule = RewardModulatedHebbian(learningRate = 0.1f)
        )

        val event = LearningEvent(
            timestamp = System.currentTimeMillis(),
            synapseId = "s001",
            presynapticActivity = 0.8f,
            postsynapticActivity = 0.9f,
            rewardSignal = 1.0f,
            cause = "reward_learning"
        )

        val update = engine.applyPlasticity(event, currentWeight = 0.5f)

        assertTrue(update.newWeight > update.oldWeight)
        assertEquals(EvidenceLevel.MODELED, update.evidenceLevel)
    }

    @Test
    fun testPlasticityWeightBounds() {
        val engine = PlasticityEngine(
            rule = RewardModulatedHebbian(learningRate = 10.0f)
        )

        val event = LearningEvent(
            timestamp = System.currentTimeMillis(),
            synapseId = "s001",
            presynapticActivity = 1.0f,
            postsynapticActivity = 1.0f,
            rewardSignal = 1.0f,
            cause = "test"
        )

        val update = engine.applyPlasticity(event, currentWeight = 9.0f)

        assertTrue(update.newWeight <= 10f)
        assertTrue(update.newWeight >= 0f)
    }
}

class TimelineTest {

    @Test
    fun testTimelineCreation() {
        val timeline = Timeline(
            experimentId = "exp001",
            startTime = 1000L,
            seed = 12345L
        )

        assertEquals("exp001", timeline.experimentId)
        assertEquals(1000L, timeline.startTime)
        assertEquals(0, timeline.events.size)
    }

    @Test
    fun testAddEvent() {
        val timeline = Timeline(
            experimentId = "exp001",
            startTime = 1000L
        )

        val event = StimulusEvent(
            timestamp = 1100L,
            stimulusType = "odor",
            parameters = mapOf("concentration" to 0.5)
        )

        val updated = timeline.addEvent(event)

        assertEquals(1, updated.events.size)
        assertEquals(1100L, updated.currentTime)
    }

    @Test
    fun testEventsInRange() {
        var timeline = Timeline(
            experimentId = "exp001",
            startTime = 1000L
        )

        timeline = timeline.addEvent(StimulusEvent(1100L, "odor", emptyMap()))
        timeline = timeline.addEvent(RewardEvent(1200L, 1.0f, "food"))
        timeline = timeline.addEvent(PlasticityEvent(1300L, "s001", 0.5f, 0.6f, "reward"))

        val eventsInRange = timeline.eventsInRange(1100L, 1250L)

        assertEquals(2, eventsInRange.size)
    }
}

class MemoryTest {

    @Test
    fun testMemoryStorage() {
        val memorySystem = MemorySystem()

        val trace = MemoryTrace(
            id = "m001",
            type = MemoryType.REWARD_ASSOCIATION,
            content = mapOf("stimulus" to "odor", "outcome" to "food"),
            strength = 0.7f,
            timestamp = System.currentTimeMillis()
        )

        memorySystem.store(trace, isLongTerm = false)

        assertEquals(1, memorySystem.shortTermMemory.size)
        assertEquals(0, memorySystem.longTermMemory.size)
    }

    @Test
    fun testMemoryConsolidation() {
        val memorySystem = MemorySystem()

        val trace = MemoryTrace(
            id = "m001",
            type = MemoryType.ASSOCIATIVE,
            content = mapOf("test" to "data"),
            strength = 0.5f,
            timestamp = System.currentTimeMillis()
        )

        memorySystem.store(trace, isLongTerm = false)
        memorySystem.consolidate("m001")

        assertEquals(0, memorySystem.shortTermMemory.size)
        assertEquals(1, memorySystem.longTermMemory.size)

        val consolidated = memorySystem.longTermMemory["m001"]!!
        assertTrue(consolidated.strength > trace.strength)
    }
}

class EvidenceLevelTest {

    @Test
    fun testScientificallyGrounded() {
        assertTrue(EvidenceLevel.MEASURED.isScientificallyGrounded())
        assertTrue(EvidenceLevel.PUBLISHED.isScientificallyGrounded())
        assertTrue(EvidenceLevel.DERIVED.isScientificallyGrounded())

        assertFalse(EvidenceLevel.MODELED.isScientificallyGrounded())
        assertFalse(EvidenceLevel.HYPOTHESIS.isScientificallyGrounded())
        assertFalse(EvidenceLevel.UNKNOWN.isScientificallyGrounded())
    }
}

class Coordinates3DTest {

    @Test
    fun testDistanceCalculation() {
        val p1 = Coordinates3D(0f, 0f, 0f)
        val p2 = Coordinates3D(3f, 4f, 0f)

        assertEquals(5f, p1.distanceTo(p2), 0.001f)
    }

    @Test
    fun testBoundingBox() {
        val bbox = BoundingBox(
            min = Coordinates3D(10f, 20f, 30f),
            max = Coordinates3D(50f, 80f, 90f)
        )

        val center = bbox.center()
        assertEquals(30f, center.x, 0.001f)
        assertEquals(50f, center.y, 0.001f)
        assertEquals(60f, center.z, 0.001f)

        val size = bbox.size()
        assertEquals(40f, size.x, 0.001f)
        assertEquals(60f, size.y, 0.001f)
        assertEquals(60f, size.z, 0.001f)
    }
}
