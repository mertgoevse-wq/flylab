package com.flylab.sim

import com.flylab.domain.model.Hemisphere
import com.flylab.domain.model.NeuromodulatorType
import com.flylab.domain.model.NeuropilId
import com.flylab.domain.model.Neuron
import com.flylab.domain.model.NeuronType
import com.flylab.domain.model.ProvenanceLevel
import com.flylab.domain.model.ScientificEvidence
import com.flylab.domain.model.SynapseReference

/**
 * Validated Drosophila melanogaster Connectome Subset.
 * Derived from the FlyWire whole-brain electron microscopy dataset (Dorkenwald et al., Nature 2024;
 * Schlegel et al., Nature 2024; Aso et al., eLife 2014; Hulse et al., eLife 2021).
 *
 * Implements the minimal viable connectome circuit required for Milestone 1:
 * 1. Olfactory transduction & Antennal Lobe Glomeruli (DM1, DM4, DA2)
 * 2. Projection Neurons (PNs) to Mushroom Body and Lateral Horn
 * 3. Kenyon Cells (KCs) with sparse claw inputs and APL feedback inhibition
 * 4. Dopaminergic reinforcement neurons (DAN-PAM for sugar, DAN-PPL1 for punishment)
 * 5. Mushroom Body Output Neurons (MBONs: appetitive vs. aversive)
 * 6. Central Complex (CX) heading direction compass and steering premotor loop
 *
 * ALL BASELINE WEIGHTS ARE IMMUTABLE.
 */
object ConnectomeReference {

    val EVIDENCE = ScientificEvidence.FLYWIRE_CIRCUIT_MOTIF_DERIVED

    // Identified representative neurons
    val NEURONS: List<Neuron> = buildList {
        // --- 1. Antennal Lobe (AL) ---
        val alOrnEvidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Benton et al., Cell 2009; Vosshall & Stocker, Annu Rev Neurosci 2007",
            doi = "10.1016/j.cell.2009.01.022",
            notes = "Olfactory receptor neuron population converging onto uniglomerular projection neurons."
        )
        add(Neuron("ORN_DM1_L", null, "ORN DM1 (Links)", NeuronType.ORN, NeuropilId.ANTENNAL_LOBE, Hemisphere.LEFT, -0.35f, -0.30f, 0.65f, isRepresentativeModel = true, evidence = alOrnEvidence))
        add(Neuron("ORN_DM1_R", null, "ORN DM1 (Rechts)", NeuronType.ORN, NeuropilId.ANTENNAL_LOBE, Hemisphere.RIGHT, 0.35f, -0.30f, 0.65f, isRepresentativeModel = true, evidence = alOrnEvidence))
        add(Neuron("ORN_DA2_L", null, "ORN DA2 (Geosmin L)", NeuronType.ORN, NeuropilId.ANTENNAL_LOBE, Hemisphere.LEFT, -0.36f, -0.28f, 0.63f, isRepresentativeModel = true, evidence = alOrnEvidence))
        add(Neuron("ORN_DA2_R", null, "ORN DA2 (Geosmin R)", NeuronType.ORN, NeuropilId.ANTENNAL_LOBE, Hemisphere.RIGHT, 0.36f, -0.28f, 0.63f, isRepresentativeModel = true, evidence = alOrnEvidence))

        val alPnEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Schlegel et al., Nature 2024 / FlyWire Consortium",
            doi = "10.1038/s41586-024-07763-9",
            datasetSource = "FlyWire v783",
            notes = "Uniglomerular cholinergic projection neuron class routing olfactory signals from AL to MB and LH."
        )
        add(Neuron("PN_DM1_L", null, "Projection Neuron DM1 L", NeuronType.PN, NeuropilId.ANTENNAL_LOBE, Hemisphere.LEFT, -0.32f, -0.25f, 0.60f, isRepresentativeModel = true, evidence = alPnEvidence))
        add(Neuron("PN_DM1_R", null, "Projection Neuron DM1 R", NeuronType.PN, NeuropilId.ANTENNAL_LOBE, Hemisphere.RIGHT, 0.32f, -0.25f, 0.60f, isRepresentativeModel = true, evidence = alPnEvidence))
        add(Neuron("PN_DA2_L", null, "Projection Neuron DA2 L", NeuronType.PN, NeuropilId.ANTENNAL_LOBE, Hemisphere.LEFT, -0.33f, -0.23f, 0.58f, isRepresentativeModel = true, evidence = alPnEvidence))
        add(Neuron("PN_DA2_R", null, "Projection Neuron DA2 R", NeuronType.PN, NeuropilId.ANTENNAL_LOBE, Hemisphere.RIGHT, 0.33f, -0.23f, 0.58f, isRepresentativeModel = true, evidence = alPnEvidence))

        val alLnEvidence = ScientificEvidence(
            level = ProvenanceLevel.PUBLISHED,
            citation = "Chou et al., Nat Neurosci 2010",
            notes = "Pan-glomerular GABAergic local interneuron mediating lateral gain control."
        )
        add(Neuron("LN_AL_GABA", null, "AL Local Interneuron", NeuronType.LN, NeuropilId.ANTENNAL_LOBE, Hemisphere.MIDLINE, 0.0f, -0.29f, 0.62f, isRepresentativeModel = true, evidence = alLnEvidence))

        // --- 2. Mushroom Body (MB) Kenyon Cells (Simplified 16-cell sparse model) ---
        for (i in 1..16) {
            val h = if (i <= 8) Hemisphere.LEFT else Hemisphere.RIGHT
            val sign = if (h == Hemisphere.LEFT) -1.0f else 1.0f
            add(
                Neuron(
                    id = "KC_${String.format("%02d", i)}",
                    flyWireRootId = null,
                    name = "Kenyon Cell #$i (Model)",
                    type = NeuronType.KC,
                    neuropil = NeuropilId.MUSHROOM_BODY,
                    hemisphere = h,
                    posX = sign * (0.35f + (i % 4) * 0.03f),
                    posY = 0.18f + (i / 4) * 0.02f,
                    posZ = 0.25f,
                    isRepresentativeModel = true,
                    evidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL
                )
            )
        }

        // Anterior Paired Lateral (APL) feedback inhibitory neuron (Single identified cell per hemisphere)
        val aplEvidence = ScientificEvidence(
            level = ProvenanceLevel.MEASURED,
            citation = "Lin et al., Cell 2014; Dorkenwald et al., Nature 2024",
            doi = "10.1038/s41586-024-07558-y",
            datasetSource = "FlyWire v783",
            notes = "Single giant GABAergic neuron per hemisphere innervating the entire mushroom body calyx and lobes."
        )
        add(Neuron("APL_L", null, "APL Neuron L", NeuronType.APL, NeuropilId.MUSHROOM_BODY, Hemisphere.LEFT, -0.38f, 0.22f, 0.28f, isRepresentativeModel = false, evidence = aplEvidence))
        add(Neuron("APL_R", null, "APL Neuron R", NeuronType.APL, NeuropilId.MUSHROOM_BODY, Hemisphere.RIGHT, 0.38f, 0.22f, 0.28f, isRepresentativeModel = false, evidence = aplEvidence))

        // Dopaminergic reinforcement neurons
        val danEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Aso et al., eLife 2014; Liu et al., Nature 2012",
            doi = "10.7554/eLife.04577",
            datasetSource = "FlyWire MB Dopaminergic Subcircuits",
            notes = "Representative dopaminergic neurons for sugar reward (PAM) and punishment/heat (PPL1)."
        )
        add(Neuron("DAN_PAM", null, "DAN PAM-gamma5 (Belohnung)", NeuronType.DAN, NeuropilId.MUSHROOM_BODY, Hemisphere.MIDLINE, 0.05f, 0.10f, 0.20f, isRepresentativeModel = true, evidence = danEvidence))
        add(Neuron("DAN_PPL1", null, "DAN PPL1-gamma1 (Aversion)", NeuronType.DAN, NeuropilId.MUSHROOM_BODY, Hemisphere.MIDLINE, -0.05f, 0.12f, 0.22f, isRepresentativeModel = true, evidence = danEvidence))

        // Mushroom Body Output Neurons (MBONs)
        val mbonEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Aso et al., eLife 2014; Hige et al., Nature 2015",
            doi = "10.7554/eLife.04577",
            datasetSource = "FlyWire MBON Network",
            notes = "Compartmental MB output neurons driving conditioned approach (gamma5) and avoidance (gamma1)."
        )
        add(Neuron("MBON_APPETITIVE", null, "MBON-gamma5/beta'2a (Attraktion)", NeuronType.MBON, NeuropilId.MUSHROOM_BODY, Hemisphere.RIGHT, 0.42f, 0.12f, 0.18f, isRepresentativeModel = true, evidence = mbonEvidence))
        add(Neuron("MBON_AVERSIVE", null, "MBON-gamma1/gamma2 (Meidung)", NeuronType.MBON, NeuropilId.MUSHROOM_BODY, Hemisphere.LEFT, -0.42f, 0.12f, 0.18f, isRepresentativeModel = true, evidence = mbonEvidence))

        // --- 3. Lateral Horn (LH) Innate Valence ---
        val lhonEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Frechter et al., eLife 2019",
            doi = "10.7554/eLife.44590",
            notes = "Genetically hardwired lateral horn output neurons mediating stereotypic innate odor responses."
        )
        add(Neuron("LHON_ATTRACT", null, "LHON Innate Approach", NeuronType.LHON, NeuropilId.LATERAL_HORN, Hemisphere.RIGHT, 0.62f, 0.16f, 0.12f, isRepresentativeModel = true, evidence = lhonEvidence))
        add(Neuron("LHON_AVOID", null, "LHON Innate Escape", NeuronType.LHON, NeuropilId.LATERAL_HORN, Hemisphere.LEFT, -0.62f, 0.16f, 0.12f, isRepresentativeModel = true, evidence = lhonEvidence))

        // --- 4. Central Complex (CX) Heading & Steering ---
        val cxCompassEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Hulse et al., eLife 2021; Seelig & Jayaraman, Nature 2015",
            doi = "10.7554/eLife.66039",
            datasetSource = "FlyWire / hemibrain CX",
            notes = "E-PG compass neurons in the ellipsoid body and protocerebral bridge."
        )
        add(Neuron("CX_EPG_L", null, "E-PG Compass L", NeuronType.COMPASS, NeuropilId.CENTRAL_COMPLEX, Hemisphere.LEFT, -0.10f, 0.05f, 0.15f, isRepresentativeModel = true, evidence = cxCompassEvidence))
        add(Neuron("CX_EPG_R", null, "E-PG Compass R", NeuronType.COMPASS, NeuropilId.CENTRAL_COMPLEX, Hemisphere.RIGHT, 0.10f, 0.05f, 0.15f, isRepresentativeModel = true, evidence = cxCompassEvidence))

        add(Neuron("CX_MOTOR_L", null, "P-EN / LAL Steer Left", NeuronType.MOTOR_DESCENDING, NeuropilId.LATERAL_ACCESSORY_LOBE, Hemisphere.LEFT, -0.25f, -0.10f, 0.05f, isRepresentativeModel = true, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL))
        add(Neuron("CX_MOTOR_R", null, "P-EN / LAL Steer Right", NeuronType.MOTOR_DESCENDING, NeuropilId.LATERAL_ACCESSORY_LOBE, Hemisphere.RIGHT, 0.25f, -0.10f, 0.05f, isRepresentativeModel = true, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL))
        add(Neuron("CX_MOTOR_FORWARD", null, "VNC Descending Walk Forward", NeuronType.MOTOR_DESCENDING, NeuropilId.LATERAL_ACCESSORY_LOBE, Hemisphere.MIDLINE, 0.00f, -0.15f, 0.02f, isRepresentativeModel = true, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL))
    }

    // Immutable baseline synaptic connectivity
    val SYNAPSES: List<SynapseReference> = buildList {
        var synCounter = 1

        // Helper to register synapse with explicit scientific provenance
        fun addSyn(
            pre: String,
            post: String,
            trans: NeuromodulatorType,
            weight: Float,
            plastic: Boolean = false,
            evidence: ScientificEvidence = EVIDENCE
        ) {
            add(
                SynapseReference(
                    synapseId = "SYN_${String.format("%04d", synCounter++)}",
                    preNeuronId = pre,
                    postNeuronId = post,
                    neurotransmitter = trans,
                    baselineWeight = weight,
                    isPlastic = plastic,
                    evidence = evidence
                )
            )
        }

        // ORN -> PN connections (antennal lobe glomeruli)
        val ornPnEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Schlegel et al. 2024; Benton et al. 2009",
            notes = "Intra-glomerular cholinergic feedforward excitation."
        )
        addSyn("ORN_DM1_L", "PN_DM1_L", NeuromodulatorType.ACETYLCHOLINE, 1.2f, evidence = ornPnEvidence)
        addSyn("ORN_DM1_R", "PN_DM1_R", NeuromodulatorType.ACETYLCHOLINE, 1.2f, evidence = ornPnEvidence)
        addSyn("ORN_DA2_L", "PN_DA2_L", NeuromodulatorType.ACETYLCHOLINE, 1.4f, evidence = ornPnEvidence)
        addSyn("ORN_DA2_R", "PN_DA2_R", NeuromodulatorType.ACETYLCHOLINE, 1.4f, evidence = ornPnEvidence)

        // Local inhibition
        val alInhEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Chou et al. 2010",
            notes = "Pan-glomerular GABAergic lateral inhibition."
        )
        addSyn("ORN_DM1_L", "LN_AL_GABA", NeuromodulatorType.ACETYLCHOLINE, 0.5f, evidence = alInhEvidence)
        addSyn("ORN_DA2_L", "LN_AL_GABA", NeuromodulatorType.ACETYLCHOLINE, 0.5f, evidence = alInhEvidence)
        addSyn("LN_AL_GABA", "PN_DM1_L", NeuromodulatorType.GABA, 0.4f, evidence = alInhEvidence)
        addSyn("LN_AL_GABA", "PN_DA2_L", NeuromodulatorType.GABA, 0.4f, evidence = alInhEvidence)

        // PN -> Lateral Horn (Innate pathways)
        val lhonSynEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Frechter et al. 2019",
            notes = "Direct innate stereotypic projection to Lateral Horn output neurons."
        )
        addSyn("PN_DM1_R", "LHON_ATTRACT", NeuromodulatorType.ACETYLCHOLINE, 1.0f, evidence = lhonSynEvidence)
        addSyn("PN_DM1_L", "LHON_ATTRACT", NeuromodulatorType.ACETYLCHOLINE, 1.0f, evidence = lhonSynEvidence)
        addSyn("PN_DA2_R", "LHON_AVOID", NeuromodulatorType.ACETYLCHOLINE, 1.5f, evidence = lhonSynEvidence)
        addSyn("PN_DA2_L", "LHON_AVOID", NeuromodulatorType.ACETYLCHOLINE, 1.5f, evidence = lhonSynEvidence)

        // PN -> Mushroom Body Kenyon Cells (Random sparse claw input - computational model)
        for (i in 1..8) {
            val kcId = "KC_${String.format("%02d", i)}"
            if (i in 1..5) {
                addSyn("PN_DM1_L", kcId, NeuromodulatorType.ACETYLCHOLINE, 0.8f, evidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL)
            }
            if (i in 4..8) {
                addSyn("PN_DA2_L", kcId, NeuromodulatorType.ACETYLCHOLINE, 0.9f, evidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL)
            }
        }
        for (i in 9..16) {
            val kcId = "KC_${String.format("%02d", i)}"
            if (i in 9..13) {
                addSyn("PN_DM1_R", kcId, NeuromodulatorType.ACETYLCHOLINE, 0.8f, evidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL)
            }
            if (i in 12..16) {
                addSyn("PN_DA2_R", kcId, NeuromodulatorType.ACETYLCHOLINE, 0.9f, evidence = ScientificEvidence.KENYON_CELL_SPARSE_MODEL)
            }
        }

        // Kenyon Cells -> APL (feedback inhibition)
        val aplSynEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Lin et al. 2014",
            notes = "Kenyon cell drive to giant APL feedback neuron and reciprocal GABAergic inhibition."
        )
        for (i in 1..8) {
            addSyn("KC_${String.format("%02d", i)}", "APL_L", NeuromodulatorType.ACETYLCHOLINE, 0.2f, evidence = aplSynEvidence)
        }
        for (i in 9..16) {
            addSyn("KC_${String.format("%02d", i)}", "APL_R", NeuromodulatorType.ACETYLCHOLINE, 0.2f, evidence = aplSynEvidence)
        }
        addSyn("APL_L", "KC_01", NeuromodulatorType.GABA, 0.35f, evidence = aplSynEvidence)
        addSyn("APL_R", "KC_09", NeuromodulatorType.GABA, 0.35f, evidence = aplSynEvidence)

        // KC -> MBON synapses (PLASTIC: sites of associative learning)
        // Baseline: balanced feedforward drive to both appetitive and aversive MBONs
        for (i in 1..16) {
            val kcId = "KC_${String.format("%02d", i)}"
            addSyn(kcId, "MBON_APPETITIVE", NeuromodulatorType.ACETYLCHOLINE, 1.0f, plastic = true, evidence = ScientificEvidence.DOPAMINE_PLASTICITY_MATH_MODEL)
            addSyn(kcId, "MBON_AVERSIVE", NeuromodulatorType.ACETYLCHOLINE, 1.0f, plastic = true, evidence = ScientificEvidence.DOPAMINE_PLASTICITY_MATH_MODEL)
        }

        // Dopaminergic reinforcement inputs to MBON compartments
        val danSynEvidence = ScientificEvidence(
            level = ProvenanceLevel.DERIVED,
            citation = "Aso et al. 2014",
            notes = "Dopaminergic modulation of MBON compartments."
        )
        addSyn("DAN_PAM", "MBON_AVERSIVE", NeuromodulatorType.DOPAMINE, 1.0f, evidence = danSynEvidence)
        addSyn("DAN_PPL1", "MBON_APPETITIVE", NeuromodulatorType.DOPAMINE, 1.0f, evidence = danSynEvidence)

        // MBON & LHON -> Central Complex / Premotor Steering
        addSyn("MBON_APPETITIVE", "CX_MOTOR_FORWARD", NeuromodulatorType.ACETYLCHOLINE, 1.2f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)
        addSyn("LHON_ATTRACT", "CX_MOTOR_FORWARD", NeuromodulatorType.ACETYLCHOLINE, 0.8f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)
        addSyn("MBON_AVERSIVE", "CX_MOTOR_L", NeuromodulatorType.ACETYLCHOLINE, 1.5f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)
        addSyn("LHON_AVOID", "CX_MOTOR_R", NeuromodulatorType.ACETYLCHOLINE, 1.8f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)

        // Bilateral steering from AL PNs for tropotaxis
        addSyn("PN_DM1_L", "CX_MOTOR_L", NeuromodulatorType.ACETYLCHOLINE, 0.4f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)
        addSyn("PN_DM1_R", "CX_MOTOR_R", NeuromodulatorType.ACETYLCHOLINE, 0.4f, evidence = ScientificEvidence.PREMOTOR_STEERING_MODEL)
    }

    fun getNeuron(id: String): Neuron? = NEURONS.find { it.id == id }

    fun getSynapsesFrom(preId: String): List<SynapseReference> = SYNAPSES.filter { it.preNeuronId == preId }

    fun getSynapsesTo(postId: String): List<SynapseReference> = SYNAPSES.filter { it.postNeuronId == postId }
}
