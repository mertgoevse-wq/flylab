# FLYLAB PROJECT STATE

## Current Working State
- **APK Status:** Successfully building Debug APK.
- **Completed Components:** 
  - Optimized 3D fly visualization (Viewport3DCanvas - Low Allocation Map)
  - Brain region viewer (BrainRegionInspector)
  - Functional simulation engine with realistic sensory-motor loop
  - Educational Context via `LearnPanel` (Milestone H)
  - Real-time Timeline Visualization (`TimelineGraphView` for Milestone F)
  - Save/Load Persistence + Supabase Sync Stub (OnlinePersistenceManager/Milestone G)
- **Current Architecture:** Composed of Domain Models -> Simulation Runtime -> UI Graph Views & 3D Render Loop.

## Next Big Step (Milestone: Genome / Pharmacology)
- Currently missing: explicit neuromodulator UI controls or Pharmacological perturbations (Milestone J/K).
- *Action*: Build a Pharmacology/Neuromodulator perturbation panel (e.g. injecting Dopamine / Octopamine directly) giving real scientific "What If" scenarios for behavior.
