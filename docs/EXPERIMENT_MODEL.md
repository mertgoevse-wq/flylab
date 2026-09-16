# FlyLab – Experiment Model (Experiment-Modell)

**Quellen:** `docs/flylab-spec.md` § 12, § 15, § 27; `CLAUDE.md`; `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` § 28–29
**Status:** Kernmodul Stufe 1 (Workflow), Teilfunktionen später.

---

## 1. Zentrale Abnahme (Entscheidung des Auftraggebers)

**Anlegen → laufen lassen → speichern → später öffnen → vergleichen**

Das ist das Hauptkriterium, an dem Erfolg gemessen wird (flylab-spec § 27). Alles andere unterstützt diesen Workflow.

## 2. Die vier Kernexperimentarten (alle vier bestätigt)

1. **Umwelt verändern:** Temperatur, Licht, Feuchtigkeit, Tageszeit ändern; Reaktion beobachten.
2. **Gerüche/Stoffe:** Gerüche und Stoffe platzieren; Konzentration und Dauer einstellen; Reaktionen beobachten.
3. **Gehirnbereiche stören:** Bereiche abschalten oder dämpfen (nach dem Vorbild echter Experimente); Folgen beobachten. **Kennzeichnungspflicht:** als Simulationswerkzeug/„Simulationseingriff" führen (ANNAHME A6), nicht als biologisches Ergebnis. Abhängig vom Gehirn-Modul (Reihenfolge siehe TASK_GRAPH.md).
4. **Versuche vergleichen:** zwei oder mehr Versuche mit verschiedenen Bedingungen nebeneinander.

## 3. Experiment-Ablauf

1. **Ausgangssituation** definieren (Startzustand: Fliege(n), Umgebung, ggf. Genom, Optionen wie Lebenszyklus).
2. **Veränderung** festlegen (Reize, Stoffe, Eingriffe, Zeitpläne).
3. **Beobachtung** läuft automatisch mit (Tagebuch, echte Simulationsereignisse).
4. **Ergebnis** festhalten.
5. **Unsicherheit** dokumentieren (Beleg-Kategorie, Modellstatus, Grenzen).

## 4. Zeit und Wiederholung (Entscheidungen)

- **Voll zurückspulbar:** jederzeit zurückspulen, Schritt für Schritt ansehen, **an einem Punkt weiterlaufen lassen**.
- Pause, Wiederholung von vorn, Schritt-Modus.
- **Zeitlupe und Zeitraffer**, beide frei einstellbar.
- **Determinismus:** gleicher Startzustand + gleiche Zufallsgrundlage (Seed) + gleiche Modellversion = gleiches Ergebnis. Voraussetzung für Wiederholung, Vergleich und Weitergabe.

## 5. Weitergabe (Entscheidung)

Experimente können **als Datei weitergegeben** werden (z. B. an Lehrkräfte/Freunde), inklusive genug Information zur Nachstellung (Startzustand, Einstellungen, Seed, Modellversion).
**OFFEN** (flylab-spec § 23.8): genauer Umfang/Mindestinhalt der Datei; Nutzung auf anderen Geräten.

## 6. Versuchseintrag im Tagebuch (Pflichtfelder, aus Projektvertrag § 28)

- Frage/Ausgangslage (Hypothese, falls angegeben)
- Konfiguration (Einstellungen, Umgebungsparameter)
- Zufallsgrundlage (Seed)
- Datensatz- und Modellversion
- Reize (was, wann, wo, wie stark)
- Beobachtungen (echte Simulationsereignisse, Zeitverlauf)
- Lern-/Verbindungsänderungen (mit Ursache)
- Verhaltensänderungen
- Ergebnis
- Unsicherheit (Beleg-Kategorie, Modellgrenzen)

Dazu (Entscheidung „alle vier Bestandteile"): **automatische Aufzeichnung**, **eigene Notizen** des Nutzers, **Bildschirmfotos** aus der laufenden Simulation, **Vergleiche** früherer Versuche.

## 7. Vergleiche

- Mindestens zwei Versuche nebeneinander.
- Verglichbar: Einstellungen, Verlauf, Verhalten, Lern-/Verbindungsänderungen, Ergebnis.
- Zeitverläufe ansehen; Wiederholungen führen.

## 8. Speichergrenzen

- **ANNAHME A4:** Rückspulen innerhalb eines Versuchs unbegrenzt, begrenzt nur durch Gerätespeicher; vollständige Aufzeichnung bleibt im Tagebuch.
- **OFFEN** (flylab-spec § 23.7): konkrete Begrenzungen für Rückspul-Historie/Speicherplatz pro Gerät (geräteabhängig umzusetzen).

## 9. Beziehungen zu anderen Modulen

- **Simulation:** Experiment steuert die Simulation; Ergebnisse sind echte Simulationsereignisse (SIMULATION_MODEL.md).
- **Daten:** Seed, Modellversion, Datensatzversion werden protokolliert (DATA_MODEL.md).
- **Belege:** Jede Ergebnis-Aussage trägt eine Beleg-Kategorie (SCIENTIFIC_MODEL.md); „Gehirnbereiche stören" ist Simulationseingriff, kein biologischer Befund.
- **KI:** Auswertung/Zusammenfassung durch KI ist optional und immer „KI-generiert"-markiert (AI_MODEL.md).
- **Weitergabe:** Datei-Export nach Stufe 1 geplant (PRD.md S3); Reihenfolge im TASK_GRAPH.md.
