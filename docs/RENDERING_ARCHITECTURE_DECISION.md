# Rendering Architecture Decision

**Date:** 2026-09-21  
**Status:** VALIDATED - Current architecture sufficient

## Current Rendering Stack

**Technology:** Jetpack Compose Canvas with custom 3D projection

**Components:**
1. **BrainViewer.kt** (158 lines)
   - Interactive brain region visualization
   - Touch gesture handling (rotation, zoom, pan via Compose)
   - Orthographic 3D-to-2D projection with rotation matrices
   - Activity overlay rendering
   - Region selection

2. **FlyMeshGeometry.kt** (284 lines)
   - Complete Drosophila melanogaster anatomical geometry
   - Ellipsoid body segments (head, thorax, abdomen)
   - Compound eyes, antennae, proboscis
   - Six articulated legs
   - Translucent wings with vein structure
   - Brain neuropil 3D markers (11 bilateral regions)
   - Scientific color palette

3. **Camera3D.kt** (109 lines)
   - Camera state management
   - View/projection matrices
   - Viewport transformations

4. **Vector3D.kt** (55 lines)
   - 3D vector math utilities
   - Cross product, dot product, normalization

5. **Viewport3DCanvas.kt** (264 lines)
   - Main interactive 3D canvas
   - Layer toggles (anatomy, brain regions, activity)
   - Touch interaction handling
   - Real-time recomposition from simulation state

**Total: ~870 lines of rendering code**

## Performance Characteristics

**Current Scale:**
- ~10 brain region circles per frame
- Fly anatomy: ~150 polygons + ~50 line segments
- Canvas drawCircle: <1ms for 10 elements
- Recomposition: Triggered by simulation activity updates (20 FPS target)

**Measured:**
- Rendering cost: Negligible at current element count
- Touch latency: Acceptable with Compose gesture detectors
- Memory: <5MB for geometry data

## Rendering Pipeline

```
Simulation State Update
→ Compose Recomposition
→ Canvas Draw Pass
  → Project 3D points to 2D (rotation matrices)
  → Draw anatomical geometry (if layer enabled)
  → Draw brain region circles
  → Draw activity overlays with alpha
→ Display
```

**Frame Budget:** 50ms @ 20 FPS (achievable on target hardware)

## GPU Migration Analysis

### When GPU Would Be Justified:
1. **Neuron count:** >1,000 individual neurons rendered
2. **3D geometry:** Complex mesh rendering with lighting/shading
3. **Particle systems:** Activity propagation visualization
4. **Frame rate:** >60 FPS requirement
5. **Multiple flies:** Concurrent multi-agent rendering

### Current Requirements:
- ✅ ~100 neurons (aggregated to ~10 regions)
- ✅ Simple 2D projection sufficient
- ✅ 20 FPS target (not 60+)
- ✅ Single fly simulation
- ✅ Activity overlays via Canvas alpha blending

**Conclusion: GPU NOT justified at current scale.**

## Architecture Strengths

**✅ Maintainable:**
- Pure Kotlin/Compose, no native OpenGL code
- Standard Canvas API, no complex shader management
- Easy to debug and modify

**✅ Performant:**
- Current element count well within Canvas capabilities
- No unnecessary GPU context management
- Minimal memory overhead

**✅ Extensible:**
- LOD system architecture already in place
- Separate rendering layer from simulation state
- Clean separation: domain → simulation → rendering → UI
- Can add GPU renderer alongside Canvas without rewriting simulation

**✅ Mobile-Appropriate:**
- No GPU resource contention
- Minimal battery impact
- Works across all Android devices (no GPU capability requirements)

## Recommended Evolution Path

**Phase 1 (Current): Canvas Renderer** ✅
- Region-level visualization (LOD 1)
- Fly anatomy geometry
- Activity overlays
- Touch interaction

**Phase 2 (Future, if needed): Hybrid Approach**
- Keep Canvas for UI overlays and 2D elements
- Add optional GPU renderer for large-scale connectome visualization
- Implement abstraction: `Renderer` interface
  - `CanvasRenderer` (default, always works)
  - `OpenGLRenderer` (opt-in, for connectome scale-up)

**Phase 3 (Future, if needed): Full GPU**
- Only when rendering >10,000 neurons
- Only when lighting/shading required
- Only when 60+ FPS needed

## Decision

**The current Canvas renderer is SUFFICIENT and MAINTAINABLE.**

**No rendering rewrite required.**

**Rationale:**
1. Current scale (10 regions) is trivial for Canvas
2. Performance baseline shows no rendering bottleneck
3. Touch gestures work smoothly with Compose
4. GPU migration would add complexity without current benefit
5. Architecture already supports future GPU renderer as extension

**Next optimization priorities:**
1. Simulation step performance (if neuron count scales)
2. Recomposition frequency tuning (throttle updates)
3. Memory allocations (object pooling)
4. NOT rendering architecture

## References

- Performance Baseline Report: docs/PERFORMANCE_BASELINE.md
- Rendering code: app/src/main/java/com/flylab/render3d/
- UI integration: app/src/main/java/com/flylab/ui/viewer/
- Viewport: app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt

---

**Status:** VALIDATED  
**Decision:** Keep current Canvas architecture  
**Next Action:** Continue Phase H (Connectome Data Architecture)
