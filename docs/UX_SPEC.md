# FlyLab – UX Spec (Benutzeroberfläche)

**Quellen:** `docs/flylab-spec.md` § 1.5, § 9, § 16, § 17, § 18; `CLAUDE.md` (UX)
**Zweck:** beschreibt das gewünschte Erlebnis und die Oberflächenanforderungen. Visuelles Detaildesign (Farbwerte, Iconographie) entscheidet das Bau-Team im Rahmen dieser Leitplanken.

---

## 1. Grundstimmung (Entscheidung)

- **Dunkel und hell, beides umschaltbar**; Voreinstellung folgt der Systemeinstellung des Handys.
- Dunkel = „Messinstrument im Labor" – Aktivitätssignale heben sich gut ab.
- Hell = kliner Labor-Look, tagsüber gut lesbar.
- Ruhig, hochwertig, wissenschaftlich, modern.
- **Verboten:** Neon, Glasteffekt, verspielte „AI-Dashboard"-Optik. Kein Verwaltungsbrett mit Kacheln/Kennzahlen.
- Bestehendes Theme (`FlyLabTheme.kt`) nutzt Platzhalterfarben und ist an diese Leitplanken anzupassen.

## 2. Ausrichtung (Entscheidung)

- **Querformat bevorzugt** (Messinstrument-Charakter; Platz für 3D-Ansicht plus Bedienelemente daneben).
- Hochformat wird unterstützt (keine Zwangsrotation-Frust).

## 3. Startbildschirm (Entscheidung)

Ruhige Startseite mit **freier Wahl**:
1. **Kurze Einführung** (wenige Schritte, zeigt was die App kann)
2. **Geführtes Experiment** (Schritt für Schritt durch die Kausalkette)
3. **Direkt zur Fliege** (ohne Einleitung)

- Die Wahl ist jederzeit änderbar (auch später über Einstellungen).
- **ANNAHME A2:** Das geführte Eröffnungsexperiment zeigt die Kausalkette am Beispiel Geruch → Gehirn → Bewegung → Belohnung → Lernen. Welches Experiment standardmäßig zuerst angeboten wird: **OFFEN** (flylab-spec § 23.1).

## 4. Hauptbildschirm

**Immer sichtbar:**
- 3D-Hauptszene (ganzes Tier **oder** Gehirn – beide Blickebenen jederzeit wechselbar)
- Minimaler Overlay: Zeitsteuerung (Wiedergabe/Pause/Zurückspulen/Schritt/Zeitlupe/Zeitraffer), Aktivität ein/aus, Verbindungen ein/aus, Detailtiefe

**Über Menü/Panel (bei Bedarf):**
- Ausgewähltes Objekt: Name (deutsch + Fachbegriff), Funktion, Beleg-Kategorie, Aktivität, Verbindungen, Geschichte
- Tagebuch, Einstellungen, Erklärstufe, Sprache, Darstellungsstil, Lichtstimmung

**Nur auf Nachfrage:**
- Vollständige Erklärtexte, Vergleiche, Einzelzellen-Details

**Bedienung:** alles Wichtige im Querformat mit einer Hand an den unteren Rändern erreichbar; nichts Verdeckendes über der Szene.

## 5. Erklärungen (Entscheidung: drei Stufen)

1. **Ganz einfach** – Laiensprache, ohne Fachbegriffe
2. **Normal** – gut verständlich, Fachbegriffe mit Erklärung
3. **Ausführlich-wissenschaftlich** – Details, Quellenbezug, Grenzen der Aussage

Umschaltbar jederzeit. Beleg-Kennzeichnung in allen Stufen dauerhaft vorhanden.

## 6. Beleg-Kennzeichnung (Entscheidung: „streng immer")

- **Jede** biologische Aussage trägt dauerhaft eine dezente visuelle Kennzeichnung (BEOBACHTET/BELEGT/MODELLIERT/SIMULIERT/ABGELEITET/HYPOTHETISCH; „KI-generiert" bei KI-Ausgaben).
- Auf Antippen: Was ist bekannt? Was ist Modell? Wie sicher ist die Aussage?
- Kein Text behauptet pauschal „dieses Gebiet macht X" ohne Beleggrad.

## 7. Einstellbarkeit (Entscheidung: jederzeit wechselbar)

Diese Wahlen sind jederzeit erreichbar und wechselbar – auch mitten im Versuch:
- Startverhalten, Darstellungsstil der Fliege (3 Stile), Gehirn-Detailtiefe (3 Stufen), Lebenszyklus-Optionen, Erklärstufe, Sprache (DE/EN), Lichtstimmung (dunkel/hell)

**ANNAHME A3:** Wechsel der Detailtiefe ändert nur die Ansicht, nicht die laufende Simulation.

## 8. Leistungsrückmeldung (Entscheidungen)

- Bei hoher Last (v. a. Detailstufe 3): **Überhitzungswarnung**.
- **Geräte-Empfehlung:** je nach Gerätemodell eine Empfehlung, welche Detailstufe am besten geeignet ist.
- **Automatische Reduktion** bei Überlastung: senkt Detailtiefe selbstständig, zeigt sichtbar an, was reduziert wurde. **ANNAHME A5:** danach keine automatische Zurückschaltung; der Nutzer stellt selbst zurück.

## 9. 3D-Interaktion

- Drehen, zoomen, antippen (Körperteile, Gehirnbereiche)
- Ein-/Ausblenden: innere Strukturen, Nervensystem, Gehirn
- Aktive Bereiche hervorheben; Bahnen verfolgen (Richtung, Zeitablauf)
- Aktivitätsbildsprache (Farben, Pulssformen): **OFFEN** (flylab-spec § 23.11 – Gestaltungsentscheidung)

## 10. Tagebuch-Oberfläche

- Automatisch aufgezeichnete Versuche (echte Ereignisse)
- Eigene Notizen pro Versuch
- Bildschirmfotos aus der laufenden Simulation
- Vergleiche: Versuche nebeneinander, Zeitverläufe, Wiederholungen

## 11. Barrierefreiheit/Komfort (ANNAHME, technisch auszugestalten)

- Kontrastreiche Kennzeichnungen (auch für Farbsehschwäche unterscheidbar), Schriftskalierung respektieren. Nicht vom Auftraggeber gefordert, aber konsistent mit „hochwertig, wissenschaftlich".
