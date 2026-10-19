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
