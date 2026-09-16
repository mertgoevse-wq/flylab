# FlyLab – Projektzustand

**Stand:** 16.09.2026
**Maßgebliche Quelle für Produktentscheidungen:** `docs/flylab-spec.md` (aus 9 Interviewrunden)
**Verbindlicher Vertrag:** `CLAUDE.md` (Entwicklungsvertrag)

---

## BEREITS VORHANDEN

### Dokumente
- `CLAUDE.md` – Entwicklungsvertrag (Mission, wissenschaftliche Integrität, Leistungs- und Architekturanforderungen, vertikaler Streifen, UX-Grundsätze)
- `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` – technische Leitplanken für autonome Bau-Läufe (46 Abschnitte: Gehirnarchitektur, Plastizität, Sandbox, KI, QA, Task-Prioritäten)
- `freebuff-flylab-interview.md` – Interview-Modus (Interviewphase ist abgeschlossen)
- `README.md` – Projektkurzbeschreibung
- `docs/ARCHITECTURE.md` – Organismus-/Gehirn-/Aktivitäts-/Versuchs-Domänenmodell (konzeptionell)
- `docs/ROADMAP.md` – Phasen 0–14 (strukturgrundlage, wird durch Priorisierung konkretisiert)
- `docs/flylab-spec.md` – **Produktspezifikation aus dem Interview; einzige Quelle der Wahrheit für Produktentscheidungen**

### Code
- Android-Projekt mit Kotlin/Jetpack Compose (Kompilierungsstand: `compileSdk 34`, `minSdk 21`, `targetSdk 34`, Java 17, Compose-Compiler 1.5.8)
- `app/src/main/java/com/flylab/MainActivity.kt` – Platzhalter-Startbildschirm („FlyLab Android Shell", Knopf ohne Funktion)
- `app/src/main/java/com/flylab/ui/theme/FlyLabTheme.kt` – Standard-hell/dunkel-Designschema (Platzhalterfarben 0xFF6200EE etc., **entspricht noch nicht** der gewünschten ruhigen Wissenschaftsoptik)
- Gradle-Bau (8.5/9.6.0 Caches vorhanden, Wrapper vorhanden)
- `docs/VALIDATION.md` existierte als Test-Skelett (1 Testdatei erkannt) – **Inhalt zu prüfen** (OFFEN)

### Feststehende Produktentscheidungen (aus Interview, siehe flylab-spec.md)
- Ruhige Startseite mit freier Wahl (Einführung / geführtes Experiment / direkt zur Fliege)
- Deutsch + Englisch umschaltbar, zweisprachig von Anfang an angelegt
- Dunkel/hell umschaltbar; Voreinstellung folgt Systemeinstellung
- Querformat bevorzugt, Hochformat unterstützt
- Vier Sinne ab Start: Geruch, Sehen, Berührung/Wind, Temperatur
- Bedürfnisse von Anfang an (Hunger, Erschöpfung, Erregung)
- Lernen als Kernfunktion (Belohnung, Vermeidung, Gewöhnung, Erinnerung)
- Drei Gehirn-Detailstufen + Überhitzungswarnung + Geräte-Empfehlung + automatische Reduktion
- Erbgut-Ansicht fast gleich wichtig wie Gehirn, bis Basen-Ebene zoombar – **nach dem Kompakt-Kern**
- Zuerst nur weibliche Fliege
- Echte FlyWire-Daten als Ziel (adultes weibliches Tier)
- Voll zurückspulbar + Zeitlupe + Zeitraffer
- Experiment-Workflow = zentrale Abnahme: anlegen → laufen lassen → speichern → öffnen → vergleichen
- Alles offline; KI optional, Dienst frei wählbar (BYOK)
- Denk-Sandbox = Kernbestandteil, zeitlich später
- Zielgeräte: Samsung Galaxy A56 **und** OnePlus 6T
- Beleg-Kennzeichnung „streng immer"; Erklärungen in drei Stufen

---

## IN ARBEIT

- **Nichts.** Es gab noch keinen Bau-Lauf; alle Dokumente in diesem Batch sind neu erstellt.
- Das Interview ist abgeschlossen; die Übergabedokumente (dieser Batch) sind die Arbeitsgrundlage für den ersten Bau-Lauf.

---

## OFFEN (nächste konkrete Arbeitsschritte)

Siehe `docs/TASK_GRAPH.md` (Konkretisierung) und `docs/CLAUDE_CODE_HANDOFF.md` (Reihenfolge). Zusammengefasst:
1. Bestehendes Android-Projekt stabilisieren (Build, Testlauf, Theme an gewünschte Optik anpassen)
2. Grundoberfläche (Startseite mit drei Wahlen, Querformat, dunkel/hell)
3. Erste Fliege (3D, ein Darstellungsstil zuerst – ANNAHME A8: „Abstrakt-lehrbuchhaft")
4. Umgebung + vier Sinne
5. Bedürfnisse + Verhalten
6. Kausalkette sichtbar/klickbar
7. Lernen + Gedächtnis
8. Experiment-Workflow (anlegen/ausführen/speichern/öffnen/vergleichen) inkl. Zurückspulen, Zeitlupe/Zeitraffer
9. Gehirn-Ansicht Stufe „Bereiche" mit Beleg-Kennzeichnung
10. Beleg-Kennzeichnung überall durchsetzen

---

## SPÄTER (bestätigt, nicht in Stufe 1)

- Erbgut-Ansicht (bis Basen-Ebene) – **direkt nach Stufe 1** (Stufe 2)
- Mehrere Fliegen (ca. 5–10) – direkt nach Stufe 1 (Stufe 2)
- Fortpflanzung/Generationen als wählbare Simulationsoption
- Gehirn-Detailstufe „Bereiche + Beispielzellen" und „so tief wie möglich"
- Erweiterte Stoff-Experimente, Auswertung durch KI
- Experimente als Datei weitergeben
- Männliche Tiere, Balz/Paarung (parametrisiertes Modell)
- Denk-Sandbox (virtuelle Computerwelt) – Umsetzung nach Stufe 2, architektonisch vorher vorbereiten

---

## LANGFRISTIG (Fernziele, bestätigt)

- Lehre (Schule/Hochschule) und Forschende als Zielgruppe
- Großgruppen/Schwarm (20+ Fliegen), Leistung vor Detailtiefe
- Evolution über viele Generationen mit Populationsvergleichen
- Erweiterte Sinnwelt (Feuchtigkeit, Geräusche, Geschmack, Schwerkraft) – Reihenfolge OFFEN
- Zusätzliche virtuelle Tiere/allgemeine simulierte Systeme – konzeptionell getrennt von der biologischen Simulation
- iOS und Android-Zubehör/Peripherie
- KI-Dienste neben Google/Gemini (Reihenfolge OFFEN)
