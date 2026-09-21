# FlyLab Task Graph

## INITALIZATION & SETUP
TASK-ID: ORG-001
DOMAIN: Infrastructure
DESCRIPTION: Define capability matrix, blueprint, task graph and orchestrator logs.
DEPENDENCIES: None
INPUTS: Project state
OUTPUTS: .artifacts files, FLYLAB_MASTER_BLUEPRINT.md
RELEVANT SKILLS: flylab-autonomous-orchestrator, project-stage-detect
IMPLEMENTATION STATUS: Complete
TEST STATUS: N/A
DESIGN STATUS: N/A
SCIENTIFIC STATUS: N/A
PERFORMANCE STATUS: N/A
RELEASE STATUS: Internal

## M0 - FOUNDATION & STABILIZATION
TASK-ID: M0-001
DOMAIN: Android / Build
DESCRIPTION: Stabilize Android build, Gradle wrapper, ensure project compiles and runs locally/CI. 
DEPENDENCIES: ORG-001
OUTPUTS: Working APK via CLI
RELEVANT SKILLS: android-profiler, testing-setup
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

TASK-ID: M0-002
DOMAIN: UI/UX / Theme
DESCRIPTION: Implement calm, scientific dark/light theme overriding default Material colors. Establish structural directories.
DEPENDENCIES: M0-001
RELEVANT SKILLS: ui-ux-pro-max, mobile-android-design, frontend-design
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M1 - STATIC FLY
TASK-ID: M1-001
DOMAIN: 3D Visualization
DESCRIPTION: Render basic 3D fly (abstract/textbook style) via Canvas/OpenGL, implement rotate/zoom/tap.
DEPENDENCIES: M0-002
RELEVANT SKILLS: mobile-android-design, optimize, dataviz
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M2 - LIVING WORLD
TASK-ID: M2-001
DOMAIN: Simulation
DESCRIPTION: Basic odor/temp fields, 4 senses implementation, needs (hunger/exhaustion) leading to basic behaviors.
DEPENDENCIES: M1-001
RELEVANT SKILLS: autoresearch, claude-security, optimize
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M3 - VISIBLE CAUSALITY
TASK-ID: M3-001
DOMAIN: UI/UX
DESCRIPTION: Render the causal chain (Stimulus -> Sense -> Brain -> Motor -> Behavior) with click-to-expand evidence-labeled steps.
DEPENDENCIES: M2-001
RELEVANT SKILLS: dataviz, frontend-design, writing-guidelines
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M4 - LEARNING LOOP
TASK-ID: M4-001
DOMAIN: Simulation & Visualization
DESCRIPTION: Implement learning indicators (reward, connection changes before/after) linked to plasticity engine.
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M5 - EXPERIMENT WORKFLOW
TASK-ID: M5-001
DOMAIN: Core App
DESCRIPTION: Full experiment engine UI (play, pause, rewind, time-travel, deterministic save/load).
DEPENDENCIES: M4-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M6 - BRAIN REGIONS
TASK-ID: M6-001
DOMAIN: 3D Visualization / UI
DESCRIPTION: 50+ brain regions rendered with activity layers, tap-to-inspect, heating/performance warnings.
DEPENDENCIES: M2-001 (can run parallel to M4/M5)
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M7 - P1 VERTICAL SLICE COMPLETION
TASK-ID: M7-001
DOMAIN: QA & Release
DESCRIPTION: Polish and verify all P1 specs (offline support, dual languages, performance constraints, provenance UI completeness).
DEPENDENCIES: M5-001, M6-001

## M8 - GENOMICS FOUNDATION
TASK-ID: M8-001
DOMAIN: Simulation & UI
DESCRIPTION: Build the initial genome subsystem: species, chromosome, coordinates, genes, annotations, sequences, variants, and provenance modeling.
DEPENDENCIES: M7-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

TASK-ID: M8-002
DOMAIN: Simulation & UI
DESCRIPTION: Implement interactive computational mutation experiments (point mutation, insertion/deletion, phenotype mapping).
DEPENDENCIES: M8-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M9 - DEVELOPMENTAL ORGANISM MODEL
TASK-ID: M9-001
DOMAIN: Simulation
DESCRIPTION: Build a modular developmental framework interpreting the genome, modeling cell differentiation, and generating synthetic anatomy.
DEPENDENCIES: M8-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M10 - ANATOMY / ORGAN SYSTEMS
TASK-ID: M10-001
DOMAIN: 3D Visualization
DESCRIPTION: Upgrade the fly from a visualization into an inspectable simulated organism with detailed body organs.
DEPENDENCIES: M9-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M11 - ADVANCED 3D FLY
TASK-ID: M11-001
DOMAIN: 3D Visualization
DESCRIPTION: Enhance the 3D representation via dynamic shaders, detailed procedural modeling, or explicit models. Add lighting to the synthetic organs.
DEPENDENCIES: M10-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M12 - VISION / VISUAL PERCEPTION
TASK-ID: M12-001
DOMAIN: Simulation & UI
DESCRIPTION: World -> Eyes -> Visual sensor model. View linked to what the fly receives.
DEPENDENCIES: M11-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete

## M13 - CONNECTOME EXPLORER & NEURAL PERTURBATION
TASK-ID: M13-001
DOMAIN: Simulation & UI
DESCRIPTION: Implement Connectome Explorer screen. Enable optogenetic perturbation for individual neurons and view their synaptic connectivity and real-time activity rates.
DEPENDENCIES: M12-001
IMPLEMENTATION STATUS: Complete
TEST STATUS: Complete
