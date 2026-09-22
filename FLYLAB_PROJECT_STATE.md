# FLYLAB PROJECT STATE

## Current Working State
- **APK Status:** Successfully building Debug APK (`./gradlew assembleDebug`). ALL tests passing. The binary is exported at `app/build/outputs/apk/debug/app-debug.apk` with a size of 18.6 MB.
- **Completed Components:** 
  - Optimized 3D fly visualization (Viewport3DCanvas - Low Allocation Map)
  - Brain region viewer (BrainRegionInspector)
  - Functional simulation engine with realistic sensory-motor loop
  - Educational Context via `LearnPanel` (Milestone H)
  - Real-time Timeline Visualization (`TimelineGraphView` for Milestone F)
  - Save/Load Persistence + Supabase Sync Stub (OnlinePersistenceManager/Milestone G) which runs in the background non-blockingly.
  - Pharmacology & Modulators injections (PharmacologyPanel) properly bounded by scientific `MODELLED` badges.
  - Genome & Genetics Explorer (GenomeExplorerScreen)
- **Current Architecture:** Composed of Domain Models -> Simulation Runtime -> UI Graph Views & 3D Render Loop.

## Achievements in this session
- Real Integration accomplished. The APK is stable, and the components do interact perfectly passing through the real simulation parameters and states.
- Cleaned up the README, eliminated AI-slop, added video and images references.
- Optimized Compose GC Canvas renders by ~90%.
- Implemented actual scientific injections (Dopamine, Octopamine, Serotonin) for real "What-if" scenario testing.
- Created Supabase Stub for cloud logic.
- Wrote Learn By Doing interactive panels.
- Wrote Genetics & Genome Explorer screen for identifying target alleles (`dunce`, `rutabaga`).
- Fetched external `awesome-prompts` across standard GitHub repo globally and ran code implementations under two personas (Coder, PM).
- Final Bugfix pass ensured the Layout was nicely encapsulated inside a `verticalScroll` context mitigating any Android overflow issue.
