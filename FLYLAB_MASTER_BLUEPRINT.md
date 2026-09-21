# FlyLab Master Blueprint

## A. Mission
Build FlyLab as a serious scientific simulation platform for Drosophila melanogaster running on Android hardware, focusing on the sensory-neural-motor loop, connectomics, and behavioral models without fabricating biological certainty.

## B. Scientific scope
- Simulation of *Drosophila melanogaster* (adult female, based on FlyWire connectome).
- Neuromodulator models, plasticity rules, behavioral chemotaxis, reproductive models, pharmacological perturbations.
- All components tagged as MEASURED, PUBLISHED, DERIVED, MODELED, or HYPOTHESIS.

## C. Product scope
- Android app (minSdk 21, targetSdk 34) focusing on Compose and 3D rendering.
- Offline-first execution, deterministic simulation, experiment saving/replay, and time-travel.

## D. UX principles
- Scientific, calm, brutalist/industrial science visual language.
- Non-distracting UI (no glassmorphism, no neon).
- Dark/light mode compatible.
- Support for Begginer to Research modes.

## E. System architecture
Android Compose UI -> 3D Canvas / Timeline Presenter -> Simulation Engine -> Domain Model.
Deterministic offline simulation running in a background thread or coroutine, independent of frame rate.

## F. Domain architecture
Domain layer specifies the purely biological and simulation state abstractions in Kotlin without UI dependencies (e.g., `Brain`, `Fly`, `Environment`, `Memory`).

## G. Simulation architecture
Time-stepped deterministic simulation (SimulationEngine). Computes sensory transduction, neural integration, plasticity updates, motor mapping, and coordinates replay buffers.

## H. Connectome architecture
Subset of FlyWire connectome (adult female) focused on olfactory pathway (PN, KC, MBON, DAN, motor targets). Levels of detail (LOD) control neural granularity mapping.

## I. Genome architecture
Interactive chromosome-to-base visualization. Will allow in-silico mutation modeling and linking genotypes to modeled phenotypes.

## J. Developmental model
Will model development from genome + priors to generate synthetic organism variance. Clearly marked as computational model.

## K. Fly anatomy model
3D interactive hierarchy (head, thorax, abdomen, nervous system). Allows filtering, explosion views, and real-time state visualization (e.g. movement, breathing).

## L. Sensory model
Odor/Temperature/Vision/Mechano abstraction. Compute spatial gradients translating to receptor potentials.

## M. Neural model
Rate-based integration (not spiking) for performance. Sigmoid activation boundaries.

## N. Motor model
Kinematic output (speed, turn rate) converted from premotor descending neuron activity.

## O. Behavior model
Spatial navigation mapped directly from motor model in 2D/3D environments.

## P. Learning model
Three-factor plasticity (hebbian + neuromodulatory reward) implemented primarily at KC->MBON synapses.

## Q. Environment engine
Supports standard lab assays (odor arenas, mazes) and abstract economic tasks (computational paradigms).

## R. Experiment engine
ExperimentRunner coordinates saving/loading deterministic parameters + timeline events into an ExperimentJournal.

## S. Multi-fly/swarm engine
To be implemented using aggressive LOD and ECS (Entity Component System) optimizations.

## T. DNA/genome explorer
Upcoming module: deep-zoom interactive genome viewer. 

## U. Multi-species architecture
Capability-based extension for species beyond Drosophila (e.g. C. elegans) via modular domain representations.

## V. Data provenance
ScientificProvenance embedded in every domain entity tracking source, date, license and evidence level.

## W. Design system
Compose-based component library emphasizing crisp margins, categorical science colors, and accessible contrast.

## X. MCP/tool architecture
GitHub for source control, Figma for design system synchronization, Supabase for data integration if required.

## Y. Skill orchestration
Dynamic invocation of Claude Code skills for implementation, design validation, QA testing and security.

## Z. Testing
Unit testing of determinism; integration testing of experiments; Playwright UI testing/snapshotting; visual regression.

## AA. Performance
LOD, object pooling, coroutine tuning, Canvas optimization for Galaxy A56 and OnePlus 6T profiles.

## AB. Android packaging
APK build and delivery via gradle and GitHub Actions.

## AC. CI/CD
`.github/workflows/android-build.yml` for linting, testing, and APK packaging.

## AD. GitHub release
Automated tagging and artifact uploads.

## AE. Current limitations
- Canvas-based 3D is not fully hardware accelerated yet.
- Full 139k connectome not fit in memory yet; using subsets.

## AF. Roadmap
- M0/M1: Core app stability & Static 3D fly.
- M2-M6: Environments, behavior, visual tracing, learning, experiment workflow.
- M7: P1 Vertical slice complete with robust UX.
- M8+: Genomes, Multi-fly, and complex permutations.
