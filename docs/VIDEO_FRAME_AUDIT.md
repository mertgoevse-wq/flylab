# Video Frame Audit — Screen Recording (nicht verfügbar)

Stand: 2026-10-19 · Status: **NICHT DURCHFÜHRBAR — Quelldatei fehlt**

## Befund

Die Datei `Screen_Recording_20261019_224710_FlyLab.mp4` existiert **nirgends** auf diesem System
(gesucht in `/home/mert`, `/tmp`; `ffprobe` wäre vorhanden und einsatzbereit).
Daher konnte **kein einziger Frame** extrahiert oder geprüft werden — keine Stichprobe, keine Kontaktbögen.

## Was das bedeutet (ehrlich)

- Alle im Masterprompt unter 2.2 geforderten Aussagen (Übergänge, Zoomsprünge, abgeschnittene
  Geometrie, UI-Überläufe, Zeitstempel→Ursache→Korrektur) wären ohne Datei **erfunden**.
  Es wird hier bewusst **nichts** behauptet.
- Ersatzweise verifiziert (Code-Ebene, keine Video-Ebene):
  - `FlyLabRootScreen.kt` (331 Zeilen): scrollbare Root-Column + `Scaffold` mit Bottom-`NavigationBar`
    (Welt/Gehirn/Labor/Daten/Aufgaben-Icons vorhanden) — die im Prompt beschriebene
    „Dashboard-Überladung" ist strukturell noch vorhanden, aber einklappbar organisiert.
  - `Viewport3DCanvas.kt` (308 Zeilen): Canvas-Projektion mit `detectTransformGestures`
    (Rotate/Zoom) + `detectTapGestures`, RenderLayer-Chips — Kamera-Gesten existieren,
    Zoom-Begrenzung muss noch per Test geprüft werden (Phase B).
  - Letzter Fix `c06a69a` adressierte exakt ein Video-Symptom (Viewport unsichtbar ohne Scroll);
    ohne Aufnahme ist die visuelle Wirkung **unbestätigt**.

## Nachholplan (sobald die Datei vorliegt)

1. `ffprobe` → Dauer/Auflösung/Framerate protokollieren.
2. `ffmpeg fps=1` Frames + Kontaktbögen nach `docs/video-audit/` (Pakete à ~30 Frames).
3. Pro Auffälligkeit: Zeitstempel, Frame-Bereich, Beschreibung, wahrscheinliche Ursache,
   Datei:Zeile-Korrektur — Tabelle unten ergänzen.

## Auffälligkeiten-Tabelle

| Zeitstempel | Frames | Beschreibung | Ursache (vermutet) | Korrektur | Status |
|---|---|---|---|---|---|
| — | — | (keine Datei vorhanden) | — | Datei bereitstellen, dann auditieren | OFFEN |

## Anforderung an den Benutzer

Bitte `Screen_Recording_20261019_224710_FlyLab.mp4` in `flylab/docs/video-audit/` ablegen
(z. B. per USB/`adb push` vom Galaxy A56) — danach wird dieses Audit vollständig nachgeholt.
Alternativ genügt eine neue Aufnahme der aktuellen `app-debug.apk` vom Gerät.
