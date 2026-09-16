# FlyLab – Genetics Model (Erbgut-Modell)

**Quellen:** `docs/flylab-spec.md` § 10, § 11, § 24 (A7); `CLAUDE.md` (Genetics)
**Status:** Stufe-2-Modul (nach dem Kompakt-Kern). Dieses Dokument beschreibt Anforderungen, nicht die technische Umsetzung.

---

## 1. Produktentscheidungen (aus dem Interview, verbindlich)

- Erbgut ist **fast gleich wichtig** wie Gehirn und gehört von Anfang an zur Produktgeschichte – die Ansicht kommt aber **nach dem Kompakt-Kern** (Auflösung Widerspruch 1: „Schnell gewinnt").
- Von Anfang an **bis auf die Ebene einzelner Bausteine (Basen)** zoombar: Gesamterbgut → Abschnitte/Chromosomen → Gene → Basen.
- Funktionen: blättern, Bereiche wählen, Gene anschauen, Basen ansehen, **Veränderungen vornehmen** (Bearbeitungen/Mutationen), zwei Erbgüter vergleichen, Auswirkungen untersuchen.
- Kernfragen der Ansicht: Was ist im Erbgut vorhanden? Was unterscheidet zwei Fliegen? Was könnte diese Unterschiede verursachen? **Was davon ist sicher bekannt – was ist nur ein Modell?**

## 2. Datenbasis

- **ANNAHME A7:** Für die Basen-Ebene wird zunächst der bekannte Referenzdatensatz des weiblichen Tiers genutzt; individuelle Abweichungen entstehen durch Simulations-Eingriffe/Vererbung und werden als MODELLIERT gekennzeichnet.
- Echte Referenzdaten (Ziel): öffentlich zugänglicher Genom-Referenzdatensatz für *D. melanogaster*. Version, Quelle, Lizenz, Abrufdatum sind zu dokumentieren (siehe DATA_MODEL.md, Provenienz-Pflicht).
- Referenzdaten werden **nie verändert**; Bearbeitungen/Mutationen liegen als Simulations-Schicht darüber (Projektvertrag: Never mutate reference data).

## 3. Genomviewer-Anforderungen (aus flylab-spec § 10 / Projektvertrag)

- Chromosomen (Abschnitte) → Gene → Sequenzen → Basen-Ebene
- Blättern und Auswahl von Bereichen
- Gen-Details: Name, Funktion (mit Beleg-Kategorie), Lage
- Basen-Ansicht (einzelne Bausteine)
- **Veränderungen:** Bearbeitungen/Mutationen anlegen (als Simulations-Eingriff markiert)
- **Vergleich:** zwei Erbgüter gegenüberstellen (z. B. Referenz vs. bearbeitet; Eltern vs. Nachkommen)
- **Auswirkungen untersuchen:** Verweis auf beobachtete Effekte in Versuchen (nur BEOBACHTET in der Simulation; niemals erfundene Realwirkung)
- Experiment-Historie zu Genom-Zuständen

## 4. Wissenschaftliche Integrität

- Niemals einer willkürlichen Mutation eine bekannte biologische Wirkung zuschreiben, wenn sie nicht belegt ist (Projektvertrag).
- Unbekannte Wirkungen bleiben **OFFEN/HYPOTHETISCH** gekennzeichnet.
- Gen-Wirkung auf Körper/Verhalten/Gehirn: Umsetzungsdetails **OFFEN** (flylab-spec § 23.6) – welche Gene zuerst dargestellt werden, ist nicht entschieden.
- Genotyp/Phänotyp-Zuordnung nur mit Beleg-Kategorie; Standard ist MODELLIERT, BELEGT nur bei einzeln belegten Genen.

## 5. Vererbung (mit Fortpflanzung, später)

- Nachkommen erben Kombinationen aus Eltern-Erbgut (vereinfachtes Modell, MODELLIERT).
- Zufällige Veränderungen (Mutationen) als Modellgröße.
- Mehrere Generationen als **wählbare Simulationsoption** (flylab-spec § 3.4); genaue Umschalter (Altern an/aus, Tod an/aus, Paarung an/aus, Generationszahl) **OFFEN** (flylab-spec § 23.3).
- Unterschiede zwischen Eltern und Nachkommen und zwischen Populationen darstellbar; Evolution immer als **vereinfachtes Modell** gekennzeichnet (flylab-spec § 11).

## 6. Geschlecht

- Zuerst nur weibliche Tiere (Entscheidung; passt zur Datenlage).
- Männliche Tiere später, klar als parametrisiertes Modell markiert (keine vollständigen männlichen Strukturdaten erfinden).

## 7. Schnittstellen zu anderen Modulen

- **Gehirn:** Gen-Zustände können modellierte Parameter des Nervensystems beeinflussen (nur mit Beleg-Kennzeichnung; Details OFFEN § 23.6).
- **Körper/Phänotyp:** sichtbare Unterschiede nur wenn modelliert/belegt gekennzeichnet.
- **Experimente:** Genom-Zustand ist Teil der Experiment-Einstellungen (für Nachstellung und Vergleich).
- **Tagebuch:** Genom-Änderungen werden mit Ursache und Zeitstempel protokolliert.
