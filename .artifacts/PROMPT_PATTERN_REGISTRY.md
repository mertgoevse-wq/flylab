# Prompt Pattern Registry

## 1. Autonomous Loop Pattern
**Source**: `flylab-autonomous-orchestrator`
**Purpose**: Run autonomous design/build/test/fix loops.
**Domain**: Overall architecture
**FlyLab Adaptation**: Ensure the loop logs to SKILL_USAGE_LOG.md and respects the scientific evidence tagging limits.

## 2. Design to Code Pattern
**Source**: Figma MCP `figma-use` and `frontend-design`
**Purpose**: Build UI precisely from visual constants.
**FlyLab Adaptation**: Mapped to "Calm Science" FlyLab aesthetic. Focuses on Compose UI without AI-generic gradients.

## 3. The Science Provenance Pattern
**Source**: FlyLab internal requirement
**Purpose**: Enforce truthfulness in biological claims.
**FlyLab Adaptation**: Every generated UI text or domain entity must insert `[EVIDENCE: <measured|published|modeled|hypothesis>]`. 

## 4. Mobile Deterministic Simulation Pattern
**Source**: Simulation/Game Dev Literature (Fixed Time-step algorithms)
**Purpose**: Keep simulation deterministic regardless of render frame rate.
**FlyLab Adaptation**: Applied strictly to `SimulationEngine.kt` to allow replay and time-travel.
