# FlyLab 2.0 — Release Notes (Phase A: Audit, 2026-10-19)

Branch: `feat/simulator-world-brain-view` · Tests: **75/75 grün** · Build: `app-debug.apk` (30,3 MB, 19.10.)

## Neu in diesem Schritt

- `docs/APK_FORENSICS.md` — echte apkanalyzer/aapt-Befunde (Paket, Dex, Libs, Assets).
- `docs/VIDEO_FRAME_AUDIT.md` — ehrlich als OFFEN markiert (Aufnahme fehlt auf diesem System).
- `docs/FLYLAB_2_ARCHITECTURE.md` — Single-Engine-Zielbild + Render-Entscheidung (Compose-Canvas bleibt).
- `docs/QA_REPORT.md` — verifizierte vs. offene Punkte, ohne geschönte Aussagen.

## Wichtigster Befund

Zwei Simulationsengines sind gleichzeitig verdrahtet (`sim.*` vs. `simulation.*`).
Kaninonisch: `sim.SimulationEngine`. Zusammenführung ist Aufgabe Nr. 1 der Phase B.

## Noch nicht enthalten (Phasen B–H ausstehend)

Neue Navigation, Anatomie-Upgrade, 3D-Umgebung, Gehirn-Fenster, EEG-Signal, Aufgaben,
Pharma-Ausbau, Ökonomie-Modul, Geräte-Smoke-Test, Screenshots.

---

# FlyLab 2.0 — Release Notes (Phase B: Single Engine + Kamera-Guards, 2026-10-20)

Branch: `feat/simulator-world-brain-view` · Tests: **81/81 grün** (frisch verifiziert,
`cleanTestDebugUnitTest`, 0 failures/errors) · Build: `app-debug.apk` (30.321.300 Bytes, 20.10. 11:13)

## Neu in diesem Schritt

- **Single Engine erreicht**: totes Doppel-Engine-Cluster gelöscht — `simulation/SimulationEngine.kt`,
  `domain/simulation/SimulationState.kt`, `ui/BrainExplorerViewModel.kt` (alle per `grep` über
  `app/src` null-verweis-geprüft: kein UI-, kein Test-Verweis). Einzige Engine: `sim.SimulationEngine`;
  Gehirn-Tab rendert `currentSnapshot.toRegionalActivity()` (derselbe Zustand wie Weltansicht).
- **Haltere-Anatomie**: Diptera-Merkmal bilateral gespiegelt in `FlyMeshGeometry` + `HaltereGeometryTest`
  (dabei Testlogik-Fehler gefunden und behoben: Spiegelvergleich sortierte falsch, Geometrie war korrekt).
- **Kamera-Guards per Test gesperrt**: neuer Test `camera elevation clamps and fly stays projectable
  after extreme gestures` — Elevation ±85°, 20× Extrem-Zoom-out → Distanz 25 + Target sichtbar
  (1080×2400), 20× Extrem-Zoom-in → 1.5.
- Docs: `FLYLAB_2_ARCHITECTURE.md` (Single-Engine-Status), `QA_REPORT.md` (Nachtrag 20.10.).

## Wichtigste Einschränkungen (unverändert ehrlich)

- `FlyLab-debug.apk` und `Screen_Recording_20261019_224710_FlyLab.mp4` existieren nirgends auf
  diesem System → kein APK-Diff gegen Auslieferung, kein Video-Audit (s. `VIDEO_FRAME_AUDIT.md`).
- Kein Galaxy A56 per `adb` verbunden → kein Geräte-Smoke-Test, keine Screenshots.
- Nur Debug-APK (Live Literals, 4 ABIs); kein Release/Minify-Lauf.
