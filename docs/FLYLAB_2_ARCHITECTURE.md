# FlyLab 2.0 — Architektur (Stand 2026-10-19, verifiziert)

## Leitentscheidung: kein Rewrite, kein Engine-Wechsel

- **Rendering bleibt Compose-Canvas** (`Viewport3DCanvas` + `FlyMeshGeometry` + `Camera3D` + `RenderLayers`).
  Filament/SceneView/OpenGL-ES-Engine wurde geprüft und **abgelehnt**: +MBs APK, NDK-Toolchain auf Pi 400,
  Bruch aller 88 Dateien für ungewissen Gewinn. Stattdessen: Geometrie- und Animationsqualität
  innerhalb des Canvas heben (Gelenkhierarchie, LOD).
- **Compose bleibt** für HUD/Navigation/Labor. Keine Hybrid-Engine.

## Single Source of Truth (Zielbild, noch nicht erreicht)

```
Referenzdaten (ConnectomeReference, BrainDataRepository, Provenance)
        ↓
sim.SimulationEngine  ← EINZIGE Engine (deterministisch, Seed, maxHistory 2000)
  ├─ SensoryTransduction → BehaviorRuntime(Mechanistic) → MotorMapping → Fly/Environment
  ├─ NeuralDynamics + PlasticityRule (+ Neuromodulatoren)
  └─ SimulationSnapshot → Viewport3DCanvas / BrainViewer / TimelineGraphView / EEG-Signal
ExperimentRunner / Pharmakologie / Ökonomie nur als Treiber der EINEN Engine.
```

## Befund: zwei Engines live (Phase-B-Aufgabe)

| Engine | Treiber | Status |
|---|---|---|
| `sim.SimulationEngine` (BehaviorRuntime, Seed, Snapshot) | `FlyLabRootScreen`, `ExperimentRunner` | KANONISCH |
| `simulation.SimulationEngine` (SimulationConfig, PlasticityEngine) | `BrainExplorerViewModel` | ZUSAMMENFÜHREN |

`BrainExplorerViewModel` muss auf `sim.SimulationEngine` umgestellt werden; ungenutzte Duplikate
(`domain/simulation/*`, falls dann orphan) entfernen. Erst danach sind Welt- und Gehirnansicht
 garantiert derselbe Zustand.

## Schichten (ist)

1. Referenzdaten/Herkunft · 2. Konnektivität/Neuronen · 3. Engine · 4. Sensorik/Motorik ·
5. Plastizität/Training · 6. Pharmakologie (Stub-gesichert, MODELED-Badges) · 7. Umwelt/Kollision ·
8. Renderer (Canvas, LOD0–2: 150/50/20 Neuronen) · 9. UI (RootScreen + 5 Nav-Ziele) ·
10. Persistenz (PersistenceManager + Online-Stub, non-blocking).

## Kamera-Guards (verifiziert in `Camera3D.kt`)

Zoom 1.5–25, Elevation ±85°, Pan distanz-skaliert — „unkontrollierbarer Zoom" liegt nicht vor.
Offen: Test für Zoomgrenzen + Gehirn/Welt-Wechsel (Phase B).

## Ökonomie/Trading

Strikt getrenntes Modul, Spielgeld zuerst; kein Echtgeld, keine Broker. Client-seitige
Buchungen nie als Wert darstellen (vibe-security: Server-Validierung erst bei echtem Backend).
