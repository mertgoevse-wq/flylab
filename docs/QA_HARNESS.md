# FlyLab QA Harness & Verification Strategy

## 1. Scope & Objective
This document outlines the end-to-end verification strategy for **FlyLab**, an Android-native scientific simulation platform for *Drosophila melanogaster*.

## 2. Testing Levels

### 2.1. Unit & Domain Tests
- **Determinism Tests**: Validate that `SimulationEngine` given the same `seed` and `ExperimentConfiguration` outputs byte-identical snapshot histories.
- **Scientific Integrity Rules**: Enforce that male Drosophila whole-brain connectome is never tagged as `MEASURED` (as per `CLAUDE.md`).
- **Plasticity Engine Bounds**: Synaptic updates cannot exceed defined physiological boundaries (e.g. `[0.0, baseline * 2.5]`).

### 2.2. Integration Tests
- **Sensory-to-Motor Pipeline**: Assert that active appetitive odors (e.g. Apple Cider Vinegar) result in positive forward velocity and orientation towards higher concentration gradients.
- **Optogenetic Perturbation Flow**: Verify that setting `AL` or `MB` region perturbation factor impacts downstream firing rates dynamically.

### 2.3. Performance & Resource Profiles
Target devices: **Samsung Galaxy A56** & **OnePlus 6T**.
- **Frame Rate Target**: ≥ 60 FPS in interactive 3D viewport.
- **Simulation Time Budget**: ≤ 8ms per simulation step tick to prevent UI jank.
- **Memory Footprint**: Target `< 150 MB` heap usage by employing progressive connectome loading and Level of Detail (LOD) representations.

### 2.4. Visual & UI Review
- **Aesthetic Principles**: Strictly enforce high-information-density scientific visualization. Reject generic AI dashboards, gradients, neon, and glassmorphism.
- **Accessibility**: Dual-language support (German/English), accessible contrast ratios, and clear provenance tagging (`MEASURED`, `PUBLISHED`, `DERIVED`, `MODELED`, `HYPOTHESIS`).

## 3. Automation Harness
Run local verification via:
```bash
./gradlew test
./gradlew lint
./gradlew assembleDebug
```
Continuous Integration runs on every push using `.github/workflows/android-build.yml`.
