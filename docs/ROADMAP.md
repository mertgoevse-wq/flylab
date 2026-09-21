# FlyLab Roadmap

**Aktualisiert:** 16.09.2026 – durch die Interview-Priorisierung konkretisiert (Quelle: `docs/flylab-spec.md` § 20).
**Auflösender Grundsatz (Widerspruch 1):** „Schnell gewinnt" – kompakter Kern zuerst, Erbgut und mehrere Fliegen direkt danach in kurzen Schritten.

## Stufe 1 – Kompakter Kern (schnell lauffähig) [vorher: Phase 0–1 + Teile von 2, 3, 5, 6]

Fliege (3D, ein Darstellungsstil zuerst – ANNAHME A8) · Grundoberfläche (Startseite mit drei Wahlen, Querformat, dunkel/hell, DE/EN) · Umgebung verändern (Temperatur, Licht, Feuchtigkeit, Gerüche, Stoffe) · vier Sinne · Bedürfnisse · Verhalten · sichtbare/klickbare Kausalkette · Lernen + Gedächtnis · Experiment-Workflow (anlegen → ausführen → speichern → öffnen → vergleichen) inkl. Zurückspulen, Zeitlupe/Zeitraffer, Determinismus · Gehirn-Ansicht Stufe „Bereiche" · Beleg-Kennzeichnung „streng immer" · Tagebuch (automatisch + Notizen + Fotos + Vergleich) · alles offline.

## Stufe 2 – Direkt danach, in kurzen Schritten [vorher: Teile von Phase 2, 7, 9]

Erbgut-Ansicht (Gesamterbgut → Chromosomen → Gene → Basen; verändern; vergleichen) · mehrere Fliegen (ca. 5–10, unabhängige Zustände) · Experimente als Datei weitergeben · Gehirn-Detailstufe „Bereiche + Beispielzellen" · FlyWire-Datenintegration (echte Daten als Ziel; Übergangs-Übungsdatensatz vorher klar markiert).

## Stufe 3 – Vertiefung [vorher: Teile von Phase 5, 6, 8]

Fortpflanzung/Generationen als wählbare Option · Stoff-Experimente vertieft (Konzentration, Dauer, Aufnahmeort, Erholung) · Experimentart „Gehirnbereiche stören" ausgebaut · KI-Funktionen (Erklären, Experiment-Hilfe, Auswertung; BYOK) · Gehirn-Detailstufe „so tief wie möglich".

## Stufe 4+ – Langfristig [Phasen 10–14]

Denk-Sandbox (virtuelle, abgeschottete Computerwelt; Kernbestandteil, aber nach dem biologischen Kern) · männliche Tiere/Balz (parametrisiertes Modell) · Großgruppen/Schwarm (20+; Leistung vor Detailtiefe) · Evolution über viele Generationen · erweiterte Sinnwelt (Reihenfolge OFFEN) · Lehre/Forschung-Funktionen · weitere virtuelle Tiere/allgemeine Systeme (konzeptionell getrennt) · Android-Peripherie · iOS.

## Historische Phasenstruktur (bleibt als Fach-Gliederung gültig)

- Phase 0: Projektfundament und Bau-System ✔ (teilweise vorhanden: Android-Grundgerüst)
- Phase 1: Interaktive 3D-Fliege + Kamera + Anatomie-Gerüst → Stufe 1
- Phase 2: Gehirn-Viewer + Verbindungsteilstück + Bereichszuordnung → Stufe 1 (Bereiche) / Stufe 2 (Beispielzellen, FlyWire)
- Phase 3: Aktivitätssimulation + visuelle Ausbreitung → Stufe 1 (Bereichsebene)
- Phase 4: Sinneseingang → Nervensystem → Motorik → Verhalten → Stufe 1
- Phase 5: Belohnung/Neuromodulation/Lernen/Gedächtnis → Stufe 1 (Kern) / Stufe 3 (vertieft)
- Phase 6: Experiment-Engine und Wiederholung → Stufe 1
- Phase 7: Genetik/Genom-Viewer/Mutationen → Stufe 2
- Phase 8: Physiologie/Stoff-Perturbation → Stufe 3
- Phase 9: Fortpflanzung/Entwicklung/Population → Stufe 3 (als Option)
- Phase 10: Mehr-Fliegen-Kommunikation/soziale Umgebungen → Stufe 2 (Basis) / Stufe 4 (sozial vertieft)
- Phase 11: Externe Aufgabenumgebungen → Stufe 4
- Phase 12: Android-Geräte-Interaktion → Stufe 4
- Phase 13: Roboter-/Peripherie-Schnittstellen → Stufe 4
- Phase 14: Wissenschaftliche Validierung/Reproduzierbarkeit → kontinuierlich ab Stufe 1 (Determinismus, Tests), vertieft Stufe 4

**Hinweis zur Konsistenz:** Die Stufen sind die verbindliche Reihenfolge; die Phasen sind die Fach-Gliederung. Bei Abhängigkeitskonflikten entscheidet TASK_GRAPH.md konkret pro Aufgabe.

### M14 - Scientific Reproducibility Foundation
Vorbereitung auf P2 Scale (Multi-Fly, Swarm, Graphen).
- Einheitliches Experiment-Abstraktionsmodell (Determinismus, Seeds).
- Strikte Provenienz (Observed bis Hypothetical).
- Graph-Abstraktionen (Nodes/Edges für Neuro-Modelle).
- Zeitreihen- und Event-Logging (ExperimentJournal).
