# FlyLab Architecture

## Overview

FlyLab is a scientifically honest Android application for exploring the Drosophila melanogaster brain, connectome, neural activity, plasticity, and learning.

## Core Architectural Principles

### 1. Scientific Honesty

Every biological component is tagged with its evidence level:
- **MEASURED**: Direct experimental measurements
- **PUBLISHED**: Peer-reviewed scientific data
- **DERIVED**: Computed from validated data
- **MODELED**: Computational simulation
- **HYPOTHESIS**: Unvalidated speculation
- **UNKNOWN**: Unknown or unverified

### 2. Separation of Concerns

```
Domain Layer (Pure Kotlin, no Android dependencies)
    ↓
Data Layer (Repositories, data sources)
    ↓
Simulation Layer (Neural dynamics, plasticity engine)
    ↓
Presentation Layer (ViewModels, Compose UI)
```

### 3. Clean Architecture

- **Domain models** have no dependencies
- **Use cases** orchestrate domain logic
- **Repositories** abstract data sources
- **ViewModels** manage UI state
- **Composables** render UI

## Domain Model

### Brain Hierarchy

```
Brain
  └── BrainRegion
      └── Subregion
          └── CellType
              └── Neuron
```

### Connectome

```
Connectome
  ├── Neurons (Map<String, Neuron>)
  ├── Synapses (Map<String, Synapse>)
  └── Connections (Map<Pair<String, String>, Connection>)
```

Each `Synapse` maintains:
- `baselineWeight` (immutable reference data)
- `currentWeight` (mutable simulation state)

### Neural Activity Levels of Detail

**LOD 0**: Behavior only (no neural detail)
**LOD 1**: Regional activity (brain regions as single units)
**LOD 2**: Cell population activity (averaged per population)
**LOD 3**: Individual neuron activity (selected neurons)
**LOD 4**: Synaptic detail (selected synapses)

Mobile performance requires LOD 1-2 for real-time visualization.

## Plasticity Engine

The `PlasticityEngine` implements computational learning rules:

```kotlin
Δw = learningRate × preActivity × postActivity × rewardSignal
```

**Evidence Level**: MODELED

This is a computational model, not directly measured biology.

## Timeline & Replay

`Timeline` records all simulation events:
- StimulusEvent
- SensorActivationEvent
- NeuralActivationEvent
- MotorOutputEvent
- BehaviorEvent
- RewardEvent
- PlasticityEvent
- MemoryUpdateEvent

**Deterministic replay**: Same seed + same config → same outcome

## Memory System

Two-tier memory:
- Short-term: rapid storage, faster decay
- Long-term: consolidated, persistent

Memory consolidation increases trace strength.

## Data Repository

`BrainDataRepository` provides:
1. Reference brain structure (adult female *Drosophila*)
2. Major brain regions with scientific references
3. Connectome metadata

**Real FlyWire data** (~139k neurons, ~54.5M synapses) would be loaded from external datasets.

Current implementation: minimal reference structure with clear evidence labels.

## Simulation Engine

`SimulationEngine` executes time-stepped simulation:

1. Compute regional activity (LOD 1)
2. Apply plasticity rules
3. Update memory traces
4. Record timeline events
5. Advance simulation time

## UI Architecture

### Compose-based reactive UI

```
MainActivity
  └── FlyLabApp
      └── BrainExplorerScreen
          ├── BrainViewer (3D visualization)
          ├── RegionList (region selection)
          └── RegionInfoCard (detail panel)
```

### BrainViewer Features

- **Rotation**: Drag to rotate brain
- **Zoom**: Pinch to zoom in/out
- **Pan**: Two-finger pan
- **Region selection**: Tap to select
- **Activity overlay**: Real-time activity visualization
- **Visual channels**:
  - Node brightness (activity level)
  - Pulse effect (high activity)
  - Color coding (selected/highlighted/active)

## Performance Considerations

### Mobile GPU Optimization

- Level-of-detail rendering
- Progressive loading
- Bounded simulation ticks
- Frustum culling (future)
- Instancing (future)

### Memory Management

- Load regions on demand
- Release unused neural data
- Stream large datasets
- Limit simultaneous active neurons

## Testing

Comprehensive unit tests cover:
- Brain model creation
- Region hierarchy
- Neuron and synapse models
- Plasticity computation
- Timeline events
- Memory consolidation
- Evidence level classification
- Coordinate math

## Build Configuration

- **minSdk**: 21 (Android 5.0)
- **targetSdk**: 34 (Android 14)
- **Kotlin**: 1.9.22
- **Compose**: 1.5.4
- **Material 3**: 1.2.1

## Scientific References

Major brain regions implemented with citations:
- **Mushroom Body**: Aso et al. 2014, Eichler et al. 2017
- **Antennal Lobe**: Grabe & Sachse 2018
- **Central Complex**: Hulse & Jayaraman 2020
- **Optic Lobe**: Borst 2014
- **Ventral Nerve Cord**: Shepherd et al. 2016

## Future Enhancements

### P1 Priority
- Environment simulation
- Sensor models (olfactory, visual, mechanosensory)
- Behavior engine
- Learning experiments
- Memory experiments

### P2 Priority
- Virtual OS sandbox
- Autonomous FlyAgent
- AI provider integration
- Google Gemini integration

### P3 Priority
- Multi-agent simulation
- Knowledge ingestion
- Genetics viewer
- Pharmacology experiments

## Limitations

1. **Connectome**: Full FlyWire dataset not loaded (requires external data)
2. **Neural simulation**: Simplified regional model (LOD 1)
3. **3D visualization**: Orthographic projection (not full 3D rendering)
4. **Physics**: No body physics simulation yet
5. **AI integration**: Not yet implemented

## Data Provenance

All biological data is labeled with:
- Source
- Evidence level
- Version
- Reference URL
- License (where applicable)

**Never claim a computational model is experimentally validated biology.**
