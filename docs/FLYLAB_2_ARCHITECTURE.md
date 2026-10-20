# FlyLab 2.0 — Architektur (Stand 2026-10-20, verifiziert)

## Leitentscheidung: kein Rewrite, kein Engine-Wechsel

- **Rendering bleibt Compose-Canvas** (`Viewport3DCanvas` + `FlyMeshGeometry` + `Camera3D` + `RenderLayers`).
  Filament/SceneView/OpenGL-ES-Engine wurde geprüft und **abgelehnt**: +MBs APK, NDK-Toolchain auf Pi 400,
  Bruch aller 88 Dateien für ungewissen Gewinn. Stattdessen: Geometrie- und Animationsqualität
  innerhalb des Canvas heben (Gelenkhierarchie, LOD).
- **Compose bleibt** für HUD/Navigation/Labor. Keine Hybrid-Engine.

## Single Source of Truth (erreicht 20.10., per Vollsuite verifiziert)

```
Referenzdaten (ConnectomeReference, BrainDataRepository, Provenance)
        ↓
sim.SimulationEngine  ← EINZIGE Engine (deterministisch, Seed, maxHistory 2000)
  ├─ SensoryTransduction → BehaviorRuntime(Mechanistic) → MotorMapping → Fly/Environment
  ├─ NeuralDynamics + PlasticityRule (+ Neuromodulatoren)
  └─ SimulationSnapshot → Viewport3DCanvas / BrainViewer / TimelineGraphView / EEG-Signal
ExperimentRunner / Pharmakologie / Ökonomie nur als Treiber der EINEN Engine.
```

## Befund: Single Engine (zweite Engine 20.10. gelöscht)

| Engine | Treiber | Status |
|---|---|---|
| `sim.SimulationEngine` (BehaviorRuntime, Seed, Snapshot) | `FlyLabRootScreen`, `ExperimentRunner` | KANONISCH — einzige Engine |
| `simulation.SimulationEngine` (SimulationConfig, PlasticityEngine) | niemand (entdrahtet 19.10.) | GELÖSCHT 20.10. |
| `domain/simulation/SimulationState.kt` (SimulationConfig) | nur tote Engine | GELÖSCHT 20.10. |
| `BrainExplorerViewModel` (eigene Engine-Instanz, ungenutzt) | niemand (kein Nav-Ziel, kein Aufruf) | GELÖSCHT 20.10. |

Erledigt 19.10.: `SimulationSnapshot.toRegionalActivity()` projiziert Welt→Gehirn, Test `BrainProjectionTest`
sperrt identische Seeds → identische Aktivität. Die Gehirn-Tab (RootScreen Tab 3) rendert
`currentSnapshot.toRegionalActivity()` — derselbe Zustand wie die Weltansicht, keine zweite Simulation.
Erledigt 20.10.: Referenzprüfung (`grep` über `app/src`) bestätigte null Verweise auf das tote Cluster
(kein UI-, kein Test-Verweis); alle drei Dateien gelöscht, Vollsuite grün. Verbleibende
`domain/{plasticity,memory,timeline}`-Dateien sind ungenutzte Bibliotheksmodule, keine Engines —
außerhalb dieses Schritts, kein zweiter Zustand.

## Schichten (ist)

1. Referenzdaten/Herkunft · 2. Konnektivität/Neuronen · 3. Engine · 4. Sensorik/Motorik ·
5. Plastizität/Training · 6. Pharmakologie (Stub-gesichert, MODELED-Badges) · 7. Umwelt/Kollision ·
8. Renderer (Canvas, LOD0–2: 150/50/20 Neuronen) · 9. UI (RootScreen + 5 Nav-Ziele) ·
10. Persistenz (PersistenceManager + Online-Stub, non-blocking).

## Kamera-Guards (per Test gesperrt, 20.10.)

Zoom 1.5–25, Elevation ±85°, Pan distanz-skaliert — „unkontrollierbarer Zoom" liegt nicht vor.
`CameraAndGeometryTest` sperrt: Zoom-Clamp beidseitig, Elevation ±85°, 20× Extrem-Zoom-out →
Distanz bleibt 25 und Fliegen-Target projiziert sichtbar (1080×2400), 20× Extrem-Zoom-in → 1.5.
Offen: Gehirn/Welt-Wechsel-Test, Screenshots auf Hardware.

## Ökonomie/Trading

Strikt getrenntes Modul, Spielgeld zuerst; kein Echtgeld, keine Broker. Client-seitige
Buchungen nie als Wert darstellen (vibe-security: Server-Validierung erst bei echtem Backend).
