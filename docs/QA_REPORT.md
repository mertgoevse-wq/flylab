# QA Report — FlyLab 2.0 Phase A (2026-10-19)

## Unit-Tests: 75/75 BESTANDEN (frisch verifiziert)

`./flylab/gradlew -p flylab :app:cleanTestDebugUnitTest :app:testDebugUnitTest --offline`
→ `BUILD SUCCESSFUL`, erzwungene Re-Ausführung (kein up-to-date), 0 failures, 0 errors.
Suites u. a.: SimulationEngine, NeuralDynamics, SensoryTransduction, ConnectomeReference,
CameraAndGeometry, M14Reproducibility, AcceleratorBridgeStrategy, ExperimentRunner/Serialization.

## Statische Verifikation (Code gelesen, nicht behauptet)

- Kamera: Zoom-/Elevations-Guards vorhanden (`Camera3D.kt`); keine unbegrenzten Stufen.
- Sicherheit: keine Secrets/Keys im Code; Supabase nur TODO-Stub ohne Netzwerk-Permission.
- APK: 30,3 MB, nur MainActivity, keine sensiblen Permissions (s. `APK_FORENSICS.md`).
- Provenance: Connectome = repräsentativer Schaltkreis (FlyWire-Motiv), Brain-Koordinaten = Platzhalter —
  beides im Code ehrlich gelabelt.

## NICHT verifiziert (offen, keine Behauptung)

1. **Video-Audit**: Quelldatei fehlt → `VIDEO_FRAME_AUDIT.md` dokumentiert nur die Lücke.
2. **Geräte-Smoke-Test**: `adb devices` = leere Liste (kein Galaxy A56 verbunden) → keine Installation,
   kein Start-, kein Render-Nachweis auf Hardware.
3. **Release-Build**: nur Debug-APK vorhanden; kein Release/Minify-Lauf.
4. **Phasen B–G**: Navigation-Umbau, Fliegen-Anatomie, Gehirn-Fenster, Aufgaben/Pharma/Ökonomie
   sind geplant (`FLYLAB_2_ARCHITECTURE.md`), aber noch nicht implementiert.

## Nachtrag Phase B/D (19.10., 22:07): 78/78 BESTANDEN

`BUILD SUCCESSFUL`, Re-Ausführung erzwungen. Neu: `BrainProjectionTest` (3 Tests —
Welt/Gehirn-Projektion, Normalisierung, EEG-Populationssignal). Code: `BrainExplorerViewModel`
auf kanonischer Engine, 4. Nav-Tab „Gehirn" als reine Snapshot-Projektion, `populationSignal()`
(MODELED, kein echtes EEG). Unverändert offen: Geräte-Smoke-Test, Video-Audit, Release-Build.

## Nachtrag Single-Engine + Kamera-Guards (20.10.)

- Vollsuite **81/81 grün** (frisch verifiziert 20.10., erzwungene Re-Ausführung) nach: Haltere-Test-Fix (Spiegelvergleich sortierte falsch — Geometrie war
  korrekt, Testlogik korrigiert), Löschung des toten Doppel-Engine-Clusters
  (`simulation/SimulationEngine.kt`, `domain/simulation/SimulationState.kt`,
  `ui/BrainExplorerViewModel.kt` — alle null-verweis-geprüft), neuer Kamera-Extremgesten-Test.
- Kamera-Guards (Zoom 1.5–25, Elevation ±85°, Sichtbarkeit bei Max-Zoom) jetzt per Test gesperrt.
- Haltere-Anatomie (Diptera-Merkmal, bilateral gespiegelt) per `HaltereGeometryTest` gesperrt.
- Unverändert offen: Geräte-Smoke-Test, Video-Audit, Release-Build.

## Nächste QA-Schritte

Neues `app-debug.apk` bauen → per `adb install` auf A56 → Screenshots der Welt-/Gehirn-Ansicht →
Video-Datei nachliefern → Audit nachholen.
