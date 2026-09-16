# FlyLab – Data Model (Datenmodell und Provenienz)

**Quellen:** `docs/flylab-spec.md`; `CLAUDE.md`; `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` § 4–6, § 36; `docs/ARCHITECTURE.md`
**Zweck:** beschreibt die logischen Datenobjekte und die Provenienz-Pflichten. Technische Speicherformate entscheidet das Bau-Team.

---

## 1. Domänenobjekte (Kern)

### Fliege (Fly)
- Identität, Geschlecht (initial: weiblich), Alter, Genotyp, Phänotyp
- Anatomie, Physiologie, Nervensystem, Gehirn
- Erinnerung, Lernstand, Motivation, Verhaltenszustand
- Umgebungszustand (wo im Raum, was wahrgenommen)

### Gehirn (Brain)
- Hierarchie: Gehirn → Gehirnbereich → Teilbereich → Zelltyp → Neuron → Kontaktstelle (Synapse)
- Je Objekt: ID, Name (deutsch + Fachbezeichnung), Beschreibung, Funktion, Beleg-Level, Quelle, Version, Koordinaten, Beziehungen
- Aktivität, Botenstoffe/Modulation

### Verbindung (Connection/Synapse)
- Quelle, Ziel, Typ, Gewicht, Verzögerung
- Baseline-Gewicht (Referenzdaten), aktuelles Gewicht (Simulationsschicht), Änderung, Plastizität, Beleg

### Sinnesreiz (Stimulus)
- Art (Geruch/Sehen/Berührung-Wind/Temperatur; später mehr), räumliche Verteilung, Intensität/Konzentration, Dauer, Zeitverlauf

### Umgebung (Environment)
- Temperatur, Licht, Feuchtigkeit, Gerüche, Stoffe, Nahrung, Wasser, Hindernisse, räumliche Bereiche, Tageszeit — als stetige Werte

### Experiment (Experiment)
- Organismus/Genotyp, Umgebung, Aufgabe, Parameter, Zeitstempel, Seed
- Datensatzversion, Modellversion
- Reize, Beobachtungen (Ereignisstrom), Lern-/Verbindungsänderungen, Verhaltensänderungen, Ergebnis, Unsicherheit
- Optionen (z. B. Lebenszyklus-Umschalter, wenn aktiv)

### Tagebuch-Eintrag (Journal Entry)
- Pflichtfelder siehe `docs/EXPERIMENT_MODEL.md` § 6, plus: eigene Notizen, Bildschirmfotos, Vergleichsverweise

### Erklärtext (Explanation)
- Objektbezug, drei Stufen (einfach/normal/wissenschaftlich), Beleg-Kategorie, ggf. Quellenverweis, ggf. „KI-generiert"-Flag

## 2. Beleg-Kategorie als Pflichtfeld

Jedes Objekt mit biologischem Inhalt trägt **immer** eine Beleg-Kategorie (BEOBACHTET/BELEGT/MODELLIERT/SIMULIERT/ABGELEITET/HYPOTHETISCH; siehe SCIENTIFIC_MODEL.md). Fehlende Kennzeichnung gilt als Fehler.

## 3. Referenzdaten vs. Simulationsschichten (verbindlich)

- Referenzdaten (FlyWire-Gehirn; Genom-Referenz) werden **nie verändert**.
- Simulation schreibt nur in Overlays/Schichten (z. B. aktuelles Verbindungsgewicht ≠ Baseline-Gewicht).
- Jeder Datensatz trägt: ID, Version, Quelle, Lizenz, Prüfsumme, Abrufdatum, Artenschlüssel, Geschlecht, Entwicklungsstadium, Belege (Projektvertrag § 36).
- Übergangs-„Übungsdaten" (falls FlyWire noch nicht integriert) sind klar als SIMULIERT/„Übungsdaten" zu markieren (flylab-spec § 5.3).

## 4. Persistenz (ANNAHME, technisch auszugestalten)

- Alles läuft **offline** (Entscheidung). Daten bleiben auf dem Gerät.
- Gespeichert werden mindestens: Experimente (mit Ereignisstrom für Zurückspulen/Vergleich), Tagebuch (inkl. Notizen, Bildschirmfotos), Einstellungen, App-Sprache/Stimmung.
- **ANNAHME A4:** Zurückspul-Historie innerhalb eines Versuchs unbegrenzt, begrenzt durch Gerätespeicher; Speicherplatz-Anzeige vorgesehen (flylab-spec § 12.4).
- **OFFEN** (flylab-spec § 23.7): konkrete Begrenzung pro Gerät.
- **Datei-Weitergabe** von Experimenten (Entscheidung): Dateiformat inklusive Startzustand, Einstellungen, Seed, Modellversion; Mindestinhalt **OFFEN** (flylab-spec § 23.8).
- **Synchronisation:** nicht Bestandteil der bestätigten Anforderungen („Alles offline"); CloudSync wäre späteres Thema – **OFFEN/SPÄTER**, keine Entscheidung getroffen.

## 5. KI-Daten

- KI benötigt: Nutzer-Schlüssel (BYOK), nie im Programmtext (Projektvertrag).
- KI-Ausgaben werden als eigene Objekte mit „KI-generiert"-Flag gespeichert; werden nie mit Beleg-Daten verschmolzen (AI_MODEL.md).

## 6. Versionierung

- Datensatz-Version, Modellversion und Seed sind Teil jedes Experiment-Eintrags (Determinismus, Nachstellung).
- Änderungen an Erklärtexten/Beleg-Kategorien sind dokumentierte Entscheidungen (DECISIONS.md), keine stillschweigenden Umbenennungen.
