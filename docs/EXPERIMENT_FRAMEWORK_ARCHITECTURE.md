# Experiment Framework Architecture

**Date:** 2026-09-21  
**Status:** VALIDATED - Framework supports reproducible research workflows

## Core Capabilities

The FlyLab experiment framework provides a rigorous foundation for running biological simulation protocols.

### 1. Reproducibility
- **Deterministic Seeding:** Every `ExperimentConfiguration` requires an explicit random seed.
- **Identical Trajectories:** The same configuration (fly + environment + seed) guarantees bit-for-bit identical simulation execution.
- **Time-Travel:** Complete deterministic snapshot history enables pausing, rewinding, and stepping through historical states.

### 2. Configuration Abstraction
`ExperimentConfiguration` encapsulates the entire experimental protocol:
- Initial environment setup (odor sources, parameters)
- Simulated fly initial state
- Explicit hypothesis and research question
- Perturbations (optogenetic silencing/excitation equivalents)
- Execution characteristics (duration, time step)

### 3. Scenario Catalog
Standard protocols implemented:
1. `ODOR_SOURCE_PLACEMENT`: Chemotaxis and spatial navigation
2. `ASSOCIATIVE_CONDITIONING`: Odor + reward associative learning
3. `BRAIN_REGION_PERTURBATION`: Systematic silencing of specific neural pathways
4. `ENVIRONMENT_CHANGE`: Modification of behavioral context

### 4. Experiment Journaling
`ExperimentJournalEntry` records the comprehensive outcome of a trial:
- Total travel distance and food consumed
- Behavioral distribution analytics
- Transient event detection (behavior changes, reward contacts, significant odor thresholds)
- Final synaptic weight changes (plasticity verification)

### 5. Control vs. Intervention Comparison
`ExperimentRunner.compareTrials()` enables A/B analysis between a baseline control trial and an intervention (e.g., perturbed brain region), outputting divergence metrics for distance, food, and behavior.

### 6. Serialization
`ExperimentSerialization` provides JSON serialization for experimental configurations, enabling sharing, persistence, and external execution tracking.

## Scientific Integrity Principles

All experiments operate within the bounds of the proven provenance system:
- Synthetic perturbations are explicitly tagged as `MODELED` or `HYPOTHESIS`.
- Simulation results clearly identify themselves as derived behaviors, not direct biological measurements.
- The default configurations utilize established _Drosophila_ assay conditions.

## Next Evolution Path

While the current framework supports the Milestone 1 functional requirements, future expansion (Phase J/K) will add:

1. **Maze Environments**: T-maze and Y-maze spatial constraints for explicit decision making.
2. **Repeated Conditioning**: Multiple discrete trial loops (e.g., condition, rest, test) within a single journal entry.
3. **Population Analytics**: Running $N=30$ flies under identical condition configurations to compute statistical variance.
