# FlyLab

Scientific simulation platform for *Drosophila melanogaster* neural dynamics, connectome exploration, and behavior.

FlyLab is an Android-first interactive research sandbox implementing the sensory-neural-motor loop of the fruit fly. The system simulates brain activity derived from the FlyWire whole-brain connectome, reward-modulated learning, and chemotaxis behavior in real time.

## What Is Implemented

**Current milestone: P1 Vertical Slice**

- **3D fly visualization** with rotation, zoom, and anatomical structure overlay
- **Brain region viewer** with real-time neural activity visualization across major neuropils (antennal lobe, mushroom body, central complex, lateral horn, ventral nerve cord)
- **Neural simulation engine** running deterministic rate-based dynamics over a biologically-grounded connectome subset
- **Sensory-motor loop**: bilateral chemotaxis with odor gradient sampling → neural processing → motor output → spatial behavior
- **Reward-modulated plasticity** implementing dopamine-driven synaptic depression at KC→MBON synapses (mushroom body learning circuit)
- **Neuromodulator system** tracking octopamine, dopamine, serotonin, and other fly-validated neuromodulators
- **Time-travel replay** with deterministic simulation snapshots for pause, rewind, and step-through analysis
- **Experiment framework** with configurable scenarios, live perturbations, and journal logging
- **Scientific provenance tracking** for every biological structure and parameter

## Scientific Integrity

Every biological component carries an **evidence level classification**:

- **MEASURED**: Direct experimental data (e.g., FlyWire connectome neuron counts)
- **PUBLISHED**: Peer-reviewed literature (e.g., mushroom body learning circuits)
- **DERIVED**: Computed from validated datasets (e.g., synaptic projection densities)
- **MODELED**: Computational simulation (e.g., rate-based neural dynamics)
- **SIMULATED**: Runtime-generated transient state (e.g., live firing rates)
- **HYPOTHESIS**: Unvalidated speculation

**The system never presents a computational model as experimentally measured biology.** Coordinates, simplified neuron counts, and plasticity equations are explicitly labeled as models. The reference connectome metadata cites the actual FlyWire dataset (Dorkenwald et al., Nature 2024, adult female), but the full 139k-neuron dataset is not loaded—this implementation uses a representative subset for real-time mobile performance.

## Architecture

**Clean separation of concerns:**

```
Domain Models (Pure Kotlin)
    ↓
Simulation Engine (Deterministic, frame-rate independent)
    ↓
Experiment Framework (Scenarios, perturbations, logging)
    ↓
3D Rendering (Canvas-based projection with LOD)
    ↓
Compose UI (Reactive Android interface)
```

The simulation core has no Android dependencies and supports deterministic replay: identical seed + identical config = identical trajectory.

## Key Components

### SimulationEngine
- **Deterministic stepping**: Same initial state and seed produce identical behavior across runs
- **Sensory transduction**: Bilateral odor sampling with inter-antennal gradient computation
- **Neural dynamics**: Rate-based integration with membrane time constants and sigmoid activation
- **Plasticity**: Three-factor rule (pre-activity × post-activity × reward signal) modulating KC→MBON synapses
- **Motor mapping**: Brain region activity → motor command → spatial kinematics (turning, forward walk)
- **Snapshot history**: Ring buffer of complete simulation states for time-travel

### ConnectomeReference
Representative connectome subset covering the olfactory and learning pathways:
- Projection neurons (PNs) from antennal lobe glomeruli
- Kenyon cells (KCs) in mushroom body
- Mushroom body output neurons (MBONs)
- Dopaminergic neurons (DANs) for reward signaling
- Lateral horn and central complex premotor neurons

### Experiment Framework
- **ExperimentRunner**: Configurable scenarios with odor sources, reward locations, and arena geometry
- **ExperimentJournal**: Timestamped event logging for analysis and replay
- **Live perturbations**: Optogenetic-style silencing or excitation of brain regions during runs

### UI
- **FlyLabRootScreen**: Main interactive environment with 3D viewport, brain inspector, sensory-motor dashboard, and playback controls
- **Viewport3DCanvas**: Touch-interactive 3D projection with layer toggles for anatomy, brain regions, and activity overlays
- **BrainRegionInspector**: Live neuropil activation bars with perturbation sliders
- **ExperimentControlPanel**: Play/pause, step, rewind, speed control, scenario selection

## Build

**Requirements:**
- Android SDK 21+ (minSdk 21, targetSdk 34)
- JDK 17
- Gradle 8.1.4
- Kotlin 1.9.22

**Local build** (blocked by PRoot aapt2 incompatibility in current environment):
```bash
./gradlew assembleDebug
```

**CI build**: GitHub Actions workflow specification is available for reproducible builds in a standard Ubuntu Android environment. Requires `workflow` OAuth scope to push `.github/workflows/android-build.yml`.

**Tests**:
```bash
./gradlew testDebugUnitTest
```

Unit test coverage includes:
- Deterministic simulation across identical seeds
- Appetitive odor → projection neuron activation
- Associative learning (reward-driven synaptic depression)
- Time-travel snapshot restoration
- Brain region perturbation effects

## Project Structure

```
app/src/main/java/com/flylab/
├── domain/model/          # Core biological entities (Fly, BrainRegion, Neuron, Synapse)
├── sim/                   # Simulation engine, neural dynamics, plasticity, motor mapping
├── experiment/            # Experiment scenarios, runner, journal
├── render3d/              # 3D geometry, camera, vector math
├── ui/                    # Compose screens and components
├── data/                  # Brain data repository
└── MainActivity.kt

app/src/test/java/com/flylab/
├── domain/                # Provenance and model tests
├── sim/                   # Simulation engine tests
├── experiment/            # Experiment framework tests
└── render3d/              # 3D projection tests

docs/                      # Architecture, models, decisions, roadmap
```

## Current Limitations

1. **Connectome scale**: Full FlyWire dataset (139k neurons, 54.5M synapses) not loaded; using representative subset
2. **Neural dynamics**: Rate-based model, not spiking or Hodgkin-Huxley
3. **3D rendering**: Canvas-based orthographic projection, not GPU-accelerated mesh rendering
4. **Physics**: Kinematic motion model, not physics-based body simulation
5. **Environment**: 2D arena with Gaussian odor plumes; no 3D airflow or complex terrain
6. **Mobile optimization**: LOD rendering in place, but connectome streaming and instancing not yet implemented

## Scientific References

**Connectome:**
- Dorkenwald et al. (2024). Neuronal wiring diagram of an adult brain. *Nature*. DOI: 10.1038/s41586-024-07558-y
- Schlegel et al. (2024). Whole-brain annotation and multi-connectome cell typing of *Drosophila*. *Nature*.

**Olfaction:**
- Benton et al. (2009). Variant ionotropic glutamate receptors as chemosensory receptors in *Drosophila*. *Cell*. DOI: 10.1016/j.cell.2009.01.022
- Su et al. (2009). Olfactory perception: receptors, cells, and circuits. *Annu. Rev. Genet.* DOI: 10.1146/annurev.genet.42.110807.091432

**Learning:**
- Aso et al. (2014). Mushroom body output neurons encode valence and guide memory-based action selection. *eLife*. DOI: 10.7554/eLife.04577
- Hige et al. (2015). Heterosynaptic plasticity underlies aversive olfactory learning. *Nature*. DOI: 10.1038/nature14615
- Handler et al. (2019). Distinct dopamine receptor pathways underlie the temporal sensitivity of associative learning. *Cell*. DOI: 10.1016/j.cell.2019.05.040

**Motor control:**
- Rayshubskiy et al. (2020). Neural circuit mechanisms for steering control in walking *Drosophila*. *bioRxiv*.
- Hulse et al. (2021). A connectome of the *Drosophila* central complex reveals network motifs suitable for flexible navigation. *eLife*. DOI: 10.7554/eLife.66039

## Roadmap

**P2:**
- GPU-accelerated 3D mesh rendering
- Streaming connectome data loading
- Multi-fly simulation
- Extended task environments (maze navigation, multi-odor discrimination)
- Genetics viewer

**P3:**
- Android device interaction experiments
- Autonomous FlyAgent with AI decision-making integration
- Pharmacology perturbation experiments
- Population-level analysis and evolution

## License

[Project license not yet specified]

## Contact

Repository: https://github.com/mertgoevse-wq/flylab
