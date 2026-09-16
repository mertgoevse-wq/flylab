# FlyLab Development Contract

## Mission

Build FlyLab as a serious scientific simulation platform for Drosophila
melanogaster.

The application must eventually provide:

1. Interactive rotatable/zoomable 3D fly.
2. Anatomical organs and internal structures.
3. Nervous system visualization.
4. FlyBrain / connectome viewer.
5. Live neural activity visualization.
6. Brain-region activity overlay on the 3D fly.
7. Connectome graph exploration.
8. Neural dynamics simulation.
9. Sensory input -> neural activity -> motor output -> behavior.
10. Reward and neuromodulation system.
11. Learning and memory.
12. Experimental task environments.
13. Experiment logging and replay.
14. Genetics and genome editing visualization.
15. Physiology and metabolism.
16. Pharmacological perturbation experiments.
17. Sex-specific biological parameters.
18. Reproduction and population simulation.
19. Multiple independent fly agents.
20. Fly-to-fly communication.
21. Future interaction with Android devices and peripherals.

## Scientific integrity

NEVER present a hypothesis as experimentally established.

Every biological mechanism must be classified as one of:

MEASURED
PUBLISHED
DERIVED
MODELED
HYPOTHESIS

The initial complete FlyWire brain connectome is from an adult female fly.
Do not fabricate a complete male connectome.

Male/female simulation may initially use shared structural data plus
parameterized biological differences, but those parameters must be clearly
marked as modeled unless supported by data.

Do not claim that Drosophila has human-style endorphin reward biology.
Create an extensible neuromodulator system and populate it with supported
Drosophila neuromodulators.

## Engineering

Android-first.

Prioritize Samsung Galaxy A56 and OnePlus 6T class hardware.

Performance requirements:

- GPU-accelerated rendering
- level-of-detail rendering
- instancing
- asynchronous data loading
- background simulation
- frame-rate-independent simulation
- memory-aware connectome loading
- progressive visualization
- no requirement to render all 139k neurons at full visual complexity simultaneously

The application must remain usable even when the full connectome dataset is not
loaded.

## Simulation architecture

Keep these systems separate:

- rendering
- simulation
- data
- experiment orchestration
- UI
- persistence

The simulation core must be usable independently of the renderer.

## First milestone

DO NOT attempt the complete FlyLab at once.

Build a vertical slice:

3D fly
+
camera rotate/zoom
+
anatomical placeholder
+
brain region visualization
+
small connectome subset
+
simulated neural activity
+
live activity overlay
+
simple behavioral task
+
reward
+
experiment logging

The vertical slice must actually run.

## Development behavior

Work autonomously.

Inspect the repository before changing it.

Prefer small working increments.

Run tests/build checks after substantial changes.

Do not replace working code without understanding it.

Do not introduce unnecessary dependencies.

Document scientific assumptions.

## Future task environments

The architecture must support tasks such as:

- maze navigation
- food seeking
- associative learning
- music/DJ interface interaction
- instrument interaction
- drawing
- coding
- robotic manipulation
- Android device interaction

These are environments/tasks, not assumptions about real biological fly
capabilities.

## Future multi-fly simulation

Each fly must have an independent:

- genome
- sex
- physiological state
- neural state
- memory
- learning state
- behavioral state

Multiple flies must be able to communicate through explicitly modeled
channels.

## Future genetics

The genome viewer must support:

- chromosomes
- genes
- sequences
- base-level visualization
- edits
- mutations
- genotype/phenotype mappings
- experiment history

Never imply that an arbitrary mutation has a known biological effect when it
does not.

## Future pharmacology

Pharmacological experiments must operate through explicit perturbation models.

Drug/substance effects must be labeled as:

- measured
- literature-derived
- inferred
- hypothetical

## UX

The primary experience is a simulator, not an administrative dashboard.

The user should be able to:

1. Select a fly.
2. Select an environment/task.
3. Start the simulation.
4. Observe the fly.
5. Observe neural activity.
6. Inspect the brain.
7. Inspect internal state.
8. Pause/replay.
9. Inspect experiment logs.

Avoid generic AI-dashboard aesthetics.

Use a clean scientific visualization style.
