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

## Nächste QA-Schritte

Neues `app-debug.apk` bauen → per `adb install` auf A56 → Screenshots der Welt-/Gehirn-Ansicht →
Video-Datei nachliefern → Audit nachholen.
