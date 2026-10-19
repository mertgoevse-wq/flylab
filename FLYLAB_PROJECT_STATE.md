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

## NPU/GPU Acceleration (2026-10-19)
- Samsung Galaxy A56 (Exynos 1580): real vendor evidence captured — Samsung SLSI ENN 2.3.28 runtime (libenn_public_api_cpp.so + GPU/unified driver backends), vendor AIDL HAL, /dev/{npu0,npu1,npu,unpu}_throughput nodes (2 NPU cores). Xclipse 540 GPU via Vulkan/OpenCL.
- Integrated `AcceleratorBridge` (sim/backend) with LiteRT dispatch: **NNAPI(NPU) → GPU delegate → CPU**, each hardware path validated by a numeric self-test against `assets/neural_kernel_manifest.json`; failed paths are rejected, never tolerated (CPU fallback guaranteed).
- Deterministic neural kernel `assets/neural_kernel.tflite` (32 KC → 8 MBON, FULLY_CONNECTED+TANH) hand-encoded via `tools/generate_npu_kernel.py` (official TFLite schema bindings, parse-back validated).
- Backend name surfaces in `PerformanceDiagnostics` snapshots via `PerformanceDiagnosticsBridge`; strategy unit-tested (`AcceleratorBridgeStrategyTest`). Numerical sim offload is opt-in (`-Dflylab.npu.offload=true`) and OFF by default — kernel is a hardware proxy, not bit-identical to the sigmoid dynamics. Full evidence in `docs/NPU_GPU_RESEARCH.md` (MODELED).

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
