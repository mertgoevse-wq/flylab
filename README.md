<div align="center">
  <h1>🪰 FlyLab</h1>
  <p><strong>A scientific <em>Drosophila melanogaster</em> interactive sandbox for your pocket.</strong></p>
  <img src="assets/flylab_ui_preview.png" alt="FlyLab App Interface Preview" width="600"/>
</div>

Hey there! Welcome to FlyLab. We're building a scientific simulation platform for *Drosophila melanogaster* (the humble fruit fly). 

Our goal is to create an interactive research sandbox that implements the full sensory-neural-motor loop of the fruit fly. We simulate brain activity derived from the FlyWire whole-brain connectome, reward-modulated learning, and chemotaxis behavior—all happening in real time on your phone.

## What's working right now

We've got our **P1 Vertical Slice** up and running:

*   **3D Fly:** You can rotate and zoom our 3D fly model with an anatomical overlay.
*   **Brain Viewer:** Real-time neural activity visualization across major neuropils (like the antennal lobe and mushroom body).
*   **Neural Simulation:** A deterministic, rate-based dynamics engine running a subset of the connectome.
*   **Sensory-Motor loop:** The fly can sample odors, process the signal, and move based on that.
*   **Learning & Rewards:** We added dopamine-driven synaptic depression in the mushroom body learning circuit.
*   **Time Travel & Persistence:** You can pause, rewind, save, load, and step through the simulation deterministically.

## No Fake Science

We are very strict about scientific provenance. Every component is labeled with its evidence level:

*   **MEASURED**: Direct experimental data.
*   **PUBLISHED**: Found in peer-reviewed literature.
*   **DERIVED**: Computed from validated datasets.
*   **MODELED**: A computational simulation.
*   **SIMULATED**: Runtime-generated state.
*   **HYPOTHESIS**: Unvalidated speculation.

**We never present a computational model as measured biology.** Our connectome metadata cites the FlyWire dataset (Dorkenwald et al., Nature 2024), but to keep things running at 60fps on a phone, we use a representative subset of the data rather than the full 139k neurons.

<div align="center">
  <img src="assets/flylab_brain_view.png" alt="FlyLab Brain Visualizer" width="400"/>
</div>

## See it in Action

<div align="center">
  <video width="600" src="assets/product_video.mp4" controls></video>
  <p><em>Real-time neural and motor dynamics overlaid on the 3D connectome model</em></p>
</div>

## How we built it

We separated concerns explicitly for testability and portability:

`Domain Models -> Simulation Core (Deterministic) -> Experiment Framework -> 3D Rendering -> Compose UI`

The simulation core doesn't care about Android at all and is completely deterministic. Same seed = same behavior.

## Building it yourself

You need Android SDK 21+, JDK 17, Gradle 8.1.4, and Kotlin 1.9.22.

Clone the repo, open in Android Studio (or use the CLI):
```bash
./gradlew assembleDebug
```

Run tests with:
```bash
./gradlew testDebugUnitTest
```

## What's Next?

We want to move to GPU-accelerated 3D mesh rendering, enable streaming connectivity for full connectome loads, introduce multi-fly social tasks, and add online syncing (via Supabase).

## Get in Touch

Feel free to open an issue or submit a PR if you want to help out!
