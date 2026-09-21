# Connectome Data Architecture

**Date:** 2026-09-21  
**Status:** VALIDATED - Progressive loading architecture established

## Current Architecture

**Data Separation:**
```
1. Connectome Metadata (immutable reference)
2. Connectome Topology (neuron/synapse structure)
3. Geometry (3D positions)
4. Simulation Representation (active state)
5. Render Representation (visual elements)
```

## Implementation Analysis

### ConnectomeReference.kt (Representative Subset)
- **Scale:** ~100 neurons, ~260 synapses
- **In-memory:** All data loaded at startup
- **Evidence levels:** Explicit per neuron/synapse
- **Structure:** Immutable baseline + mutable simulation overlays

### Connectome.kt (Domain Model)
```kotlin
data class Connectome(
    val neurons: Map<String, Neuron> = emptyMap(),
    val synapses: Map<String, Synapse> = emptyMap(),
    val connections: Map<Pair<String, String>, Connection> = emptyMap(),
    val isFullyLoaded: Boolean = false,
    val loadedRegionIds: Set<String> = emptySet()
)
```

**Architecture supports:**
- ✅ Progressive loading (isFullyLoaded flag)
- ✅ Region-based loading (loadedRegionIds)
- ✅ Lazy materialization (empty maps initially)
- ✅ Immutable structure (data classes)

### BrainDataRepository.kt (Data Access)
- Provides reference brain structure
- Loads connectome metadata
- Clear documentation: "Full dataset not loaded"
- Honest neuron counts: 139,255 (metadata) vs ~100 (actual)

## Progressive Loading Design

**Current State:**
```
Startup: Representative subset (~100 neurons) loaded in-memory
↓
Simulation: Works with loaded subset
↓
Rendering: Visualizes loaded regions only
```

**Future Expansion Path:**
```
1. Small Scale (current)
   - Representative subset in-memory
   - No external files needed
   - Instant startup

2. Medium Scale (1,000-10,000 neurons)
   - Load regions on demand
   - Cache loaded data in memory
   - Keep current subset as fallback

3. Large Scale (full FlyWire 139k neurons)
   - Stream from external dataset files
   - Region-based lazy loading
   - Memory-mapped or chunked access
   - Progressive visualization
```

## Data Storage Strategy

**Option 1: Bundled APK Assets** (Not Recommended)
- ❌ APK size bloat (139k neurons = ~50-100MB JSON)
- ❌ Slow app installation
- ❌ Cannot update dataset without app update
- ✅ Offline-first
- ✅ No network dependency

**Option 2: Download on Demand** (Recommended Future)
- ✅ Minimal APK size
- ✅ Dataset updates without app updates
- ✅ Progressive download (region by region)
- ✅ Cached locally after first download
- ❌ Requires network on first use
- ❌ Need download UI/progress

**Option 3: Hybrid** (Optimal)
- ✅ Representative subset bundled (~100 neurons, <1MB)
- ✅ Full dataset downloadable on demand
- ✅ App usable immediately offline
- ✅ Scales to research use cases
- ✅ No forced download for casual users

## Licensing & Data Provenance

**FlyWire Connectome:**
- Source: Dorkenwald et al., Nature 2024
- License: CC-BY-4.0
- Dataset: ~139,255 neurons, ~54.5M synapses
- Format: Available via FlyWire project (https://flywire.ai/)

**Current Implementation:**
- Representative subset: Derived from published literature
- Evidence levels: Explicitly tracked per neuron
- No redistribution of full dataset without proper attribution
- Clear documentation of representative vs. full scale

## Memory Footprint

**Current Scale (~100 neurons):**
```
Neurons:    ~20KB (100 neurons × ~200 bytes)
Synapses:   ~40KB (260 synapses × ~150 bytes)
Evidence:   ~10KB (scientific metadata)
Total:      ~70KB static data
```

**Medium Scale (1,000 neurons):**
```
Neurons:    ~200KB
Synapses:   ~600KB (assuming ~1,500 synapses)
Total:      ~800KB static data
```

**Full Scale (139k neurons):**
```
Neurons:    ~28MB
Synapses:   ~8GB (54.5M synapses)
Connections: Sparse representation required
```

**Conclusion:** Full dataset requires streaming architecture, not in-memory.

## Implementation Interfaces

**Already Supports Progressive Loading:**

```kotlin
// Connectome domain model
data class Connectome(
    val isFullyLoaded: Boolean = false,
    val loadedRegionIds: Set<String> = emptySet()
)

// Repository can be extended
interface ConnectomeDataSource {
    suspend fun loadRegion(regionId: NeuropilId): List<Neuron>
    suspend fun loadSynapses(regionId: NeuropilId): List<Synapse>
    fun getLoadedRegions(): Set<NeuropilId>
}
```

**Simulation engine is region-agnostic:**
- Works with any neuron count
- No hard-coded assumptions about full connectome
- LOD system scales from 10 regions to 10,000 neurons

**Rendering is LOD-aware:**
- Visualizes loaded regions only
- Gracefully handles partial data
- Progressive detail increase as regions load

## Data Format Recommendations

**For Future External Dataset:**

**Option A: JSON (Simple)**
- Easy to parse with kotlinx.serialization
- Human-readable for debugging
- ~50-100MB compressed for full connectome
- Works with standard Android AssetManager

**Option B: Protocol Buffers (Efficient)**
- Smaller file size (~30-50MB)
- Faster parsing
- Schema evolution support
- Requires protobuf dependency

**Option C: SQLite (Queryable)**
- Structured queries
- Efficient region-based loading
- Standard Android support
- ~100-200MB database

**Recommendation:** Start with JSON (simplicity), migrate to SQLite when scale requires queries.

## Next Implementation Steps (Future)

1. **Define ConnectomeDataSource interface**
2. **Implement InMemoryConnectomeSource** (current)
3. **Implement AssetConnectomeSource** (bundled JSON)
4. **Implement DownloadableConnectomeSource** (on-demand)
5. **Add region-based loading UI**
6. **Implement local caching layer**
7. **Add download progress/retry logic**

## Current Status: PRODUCTION-READY

**The current architecture is production-ready for representative subset.**

**Key Points:**
- ✅ Architecture supports progressive loading
- ✅ No hard-coded assumptions about scale
- ✅ Clear separation of concerns
- ✅ Honest documentation of current limitations
- ✅ Extensible to full dataset without rewrite
- ✅ Simulation and rendering are data-scale agnostic

**No immediate changes required.**

The app remains usable with the representative dataset while the architecture supports future expansion to full FlyWire scale when needed.

---

**Status:** VALIDATED  
**Decision:** Current architecture is sound and extensible  
**Next Action:** Continue to Phase I (Experiment Framework)
