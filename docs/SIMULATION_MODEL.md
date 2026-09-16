# FlyLab – Simulation Model (Simulationsmodell)

**Quellen:** `docs/flylab-spec.md` § 4, § 6, § 7, § 12; `CLAUDE.md` (Simulation architecture); `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` § 3–14
**Hinweis:** Dieses Dokument beschreibt WAS simuliert wird und welche Modellebenen gelten. Die technische Umsetzung (WIE) entscheidet das Bau-Team.

---

## 1. Verbindliche Grundprinzipien

1. **Systemtrennung:** Darstellung / Simulation / Daten / Versuchssteuerung / Bedienoberfläche / Speichern bleiben getrennt (Projektvertrag). Die Simulation läuft ohne die 3D-Darstellung.
2. **Zeitunabhängig:** Simulation ist frame-rate-unabhängig; kann im Hintergrund laufen (Projektvertrag).
3. **Deterministische Wiederholung:** Gleicher Startzustand + gleiche Zufallsgrundlage (Seed) + gleiche Modellversion = gleiches Ergebnis (flylab-spec § 12.3, Projektvertrag).
4. **Stetige Größen:** Umweltgrößen sind kontinuierliche Werte, keine Booleans (Projektvertrag).
5. **Echtereignisse:** Alle Protokoll-/Kausalketten-Einträge sind echte Simulationsereignisse, nie generiert (flylab-spec § 6, § 15).
6. **Beleg-Kennzeichnung:** Jedes Modell trägt eine Kategorie aus SCIENTIFIC_MODEL.md.
7. **Kausalmodell:** Umweltreiz → Sensor → Nerveneingang → Gehirnbereich → Schaltung → Aktivität → motorische Ausgabe → Körper → Verhalten → Folge → Lernen → Plastizität → aktualisiertes Netz (Projektvertrag § 3; jede Stufe ist ein Domänenobjekt).

## 2. Modellebenen der Nervensimulation (Projektvertrag)

| Ebene | Inhalt | Verwendung |
|---|---|---|
| L0 | Nur Verhalten | Performance-Modus, schwache Geräte |
| L1 | Aktivität pro Gehirnbereich | Standard (Stufe 1 des Produkts) |
| L2 | Zellpopulationen | Detailstufe 2 („Bereiche + Beispielzellen") |
| L3 | Ausgewählte Neuronen | Detailstufe 3 („so tief wie möglich") |
| L4 | Ausgewählte Kontaktstellen | Detailstufe 3, fokussierte Bahnen |

- Der Nutzer wählt die Detailstufe; die App kann automatisch reduzieren ( flylab-spec § 5.2, § 18) – **die Simulation läuft in voller Genauigkeit weiter, nur die Ansicht ändert sich** (ANNAHME A3).
- Keine biophysikalisch detaillierten Zellmodelle (z. B. Ionenkanal-Simulation) in Stufe 1; abstrakte Populations-/Zustandsdynamik (Projektvertrag ARCHITECTURE.md).

## 3. Sinne (vier, ab Stufe 1)

| Sinn | Reizgröße | Räumlich | Wirkungspfad (grob, BELEGT grotte/MODELLIERT fein) |
|---|---|---|---|
| Geruch | Konzentrationstoff-Feld | ja | Geruchsbahn → Geruchszentrum → Anziehung/Vermeidung/Lernen |
| Sehen | Helligkeit, Bewegung, Schatten | ja | Sehzentren → Orientierung, Schreckreaktion, Tag/Nacht |
| Berührung/Wind | Kontakt/Luftzug-Intensität | ja (am Körper) | Flucht, Umorientierung, Stillhalten |
| Temperatur | Temperaturfeld | ja | Aufsuchen/Mieden von Zonen, Stress bei Extremen |

- Reize wirken konzentrations-/intensitätsabhängig (kein An/Aus).
- Weitere Sinne (Feuchtigkeit, Geräusch, Geschmack, Schwerkraft): SPÄTER, Reihenfolge OFFEN (flylab-spec § 23.5).

## 4. Bedürfnisse (ab Stufe 1)

- Minimum: Hunger/Sättigung, Erschöpfung/Ruhebedürfnis, Erregungszustand (z. B. nach Beinahe-Kollisionen).
- Wirken auf Verhaltensauswahl.
- Weitere Zustände: OFFEN (flylab-spec § 23.2).

## 5. Verhalten (ab Stufe 1)

Grundrepertoire (Projektvertrag + Interview): `ruhen`, `erkunden`, `orientieren`, `nähern`, `meiden/fliehen`, `essen`, `drehen/laufen`.
- Auswahl durch Simulationszustand (Sinne + Bedürfnisse + Erinnerung), nicht zufällig.
- Weitere Verhaltensweisen (Putzen, Springen …): OFFEN (flylab-spec § 23.4). Balz erst mit männlichen Tieren.

## 6. Lernen und Gedächtnis (ab Stufe 1, Kernfunktion)

- **Belohnung** (Nahrung): stärkt Neigung zum vorherigen Verhalten.
- **Vermeidung** (schlechter Geruch, Erschrecken): schwächt Neigung.
- **Gewöhnung:** wiederholte harmlose Reize → schwächere Reaktion.
- **Erinnerung:** wirkt auf späteres Verhalten; hat Stärke (kann nachlassen) und Zeitpunkt.
- **Kurzzeit-/Langzeit-Gedächtnis getrennt** (Projektvertrag § 12).
- Umsetzung als explizite, konfigurierbare Lernregel (z. B. Δw ∝ Aktivität_vor × Aktivität_nach × Belohnungssignal – Beispiel aus Projektvertrag § 9; **die exakte Formel ist eine technische Entscheidung** des Bau-Teams und ist als SIMULIERT/MODELLIERT zu markieren).
- Jede Änderung speichert: Baseline-Gewicht, aktuelles Gewicht, Änderung, Zeitstempel, Ursache, Lernereignis (Projektvertrag § 9).
- Referenzdaten (Baseline-Verbindungen) werden nie überschrieben; Simulationsänderung liegt als Schicht darüber (Projektvertrag § 6: REFERENCE CONNECTOME + SIMULATION OVERLAY).

## 7. Zeitsteuerung (Abnahme-Kriterium)

- Wiedergabe, Pause, **voll zurückspulbar** (bis zu einem Punkt + Weiterlaufen ab dort), Schritt-Modus, Wiederholung von vorn.
- **Zeitlupe und Zeitraffer**, frei einstellbar.
- **Determinismus:** Voraussetzung für Wiederholung und Vergleich (§ 1.3).

## 8. Umgebung (ab Stufe 1)

Vom Nutzer veränderbar: Temperatur (örtlich/global), Licht (Helligkeit, Tag/Nacht-Zyklus, Tageszeit), Feuchtigkeit, Gerüche (Art/Ort/Konzentration), chemische Stoffe (Art/Ort/Konzentration/Dauer).
Existierende Elemente: Nahrung, Wasser, Hindernisse, räumliche Bereiche, zeitliche Veränderungen.
OFFEN: Hindernis-Editor, Wetter, Geräuschquellen als Nutzerwerkzeuge (flylab-spec § 8); welche Objekte frei platzierbar sind in welcher Stufe (flylab-spec § 23.10).

## 9. Stoffe (vertieft später)

Art, Konzentration/Stärke, Dauer, Aufnahmeort; beobachtbar: Wirkung auf Verhalten, Nervensystem, Erholung; Unterschiede zwischen Tieren (später, mit mehreren Tieren). Keine medizinischen Aussagen; fehlende Datenlage → MODELLIERT/HYPOTHETISCH/offene Frage (flylab-spec § 13).

## 10. Neuromodulation

Erweiterbares Botenstoff-System (Projektvertrag): Botenstoff mit Name, Zustand/Konzentration, Quellbereich, Zielbereiche, Freisetzungsereignis, Abklingmodell, Belohnungsbezug. Initial mit belegten *Drosophila*-Botenstoffen (Dopamin u. a.). Keine menschliche Belohnungsbiologie.

## 11. Mehrere Fliegen (Stufe 2)

- Ca. 5–10 gleichzeitig (flylab-spec § 11).
- Unabhängig pro Tier: Erbgut, Geschlecht, Körperzustand, Nervenzustand, Erinnerung, Lernstand, Verhaltenszustand (Projektvertrag).
- Kommunikation nur über ausdrücklich modellierte Kanäle.
- Fortpflanzung/Generationen als **wählbare Simulationsoption** (flylab-spec § 3.4); genaue Umschalter OFFEN (§ 23.3).

## 12. Denk-Sandbox (später, architektonisch vorbereiten)

- Virtuelle, abgeschottete Computerwelt (virtuelles Dateisystem + eingeschränkter Befehlsdolmetscher statt echtem Zugriff; Projektvertrag § 18–19).
- **Nie** echter Zugriff auf Gerätedateien, private Bereiche, Zugangsdaten.
- Ausdrücklich künstlich/SIMULIERT, getrennt von der biologischen Simulation.
- Umsetzung nach Stufe 2 (flylab-spec § 20.2, § 21).

## 13. Leistungsregeln (Projektvertrag, unverändert)

GPU-Nutzung, Detailstufen, Instanzwiedergabe, asynchrones Laden, Hintergrund-Simulation, speicherbewusstes Laden des Verbindungsbildes, progressive Darstellung; niemals alle ~139k Neuronen gleichzeitig voll darstellen. Zielgeräte: Galaxy A56 und OnePlus 6T; automatische Reduktion bei Überlastung mit sichtbarem Hinweis; Überhitzungswarnung; Geräte-Empfehlung für Detailstufe.
