# FLYLAB_PROJECT_STATE.md

## Current Working State
- **APK Status:** Successfully building Debug APK (`./gradlew assembleDebug`).
- **Completed Components:** 
  - 3D fly visualization (Viewport3DCanvas)
  - Brain region viewer (BrainRegionInspector)
  - Functional simulation engine with realistic sensory-motor loop
  - UI Dashboards (DiagnosticsDashboard, PlasticityDashboard, SensoryMotorDashboard)
  - Time travel, save/load persistence (Gson based in PersistenceManager)
- **Current Architecture:** Composed of Domain Models (Kotlin) -> Simulation Engine (Deterministic) -> Experiment Framework -> 3D Canvas / Compose UI.

## Open Technical Risks & Next Big Steps
- **Learn-By-Doing / UX Explanations:** The app needs to explain itself to users without prior biological knowledge (Milestone H in the master prompt).
- **Online Backup / Alternative features:** Integrating Supabase/Figma where sensible as an online alternative for syncing the FlyLab simulation data, per user instructions.
- **Rendering & Design refinement:** Improving rendering efficiency and following a strict scientific design system, eliminating "AI-Slop".

## Work In Progress (Next Ready Items)
- Enhance the UI based on 2 `awesome-prompts` for UI/UX (e.g., Clean Code, Scientific Mobile UI).
- Review and refine `README.md` to ensure it is very "human-style" and incorporates real screenshots and visuals. 
- Implement Supabase offline/online architecture for storing and sharing experimentation sessions.
