# FlyLab – Decisions (Entscheidungen, Annahmen, Widersprüche)

**Quelle:** `docs/flylab-spec.md` (§ 23 offene Punkte, § 24 Annahmen, § 25 Widersprüche)
**Zweck:** ein einziges Register aller Entscheidungen. Diese Datei hat Vorrang bei Zweifelsfragen vor Interpretationen; im Konfliktfall gilt `flylab-spec.md` als Quelle der Wahrheit.

---

## 1. Produktentscheidungen des Auftraggebers (verbindlich, aus dem Interview)

| # | Entscheidung | Detail |
|---|---|---|
| E1 | Zielgruppen-Phasierung | A: Auftraggeber (Lernen) → B: Laien → C: Lehre/Forschung (sobald Grundbausteine stehen) |
| E2 | Erster Start | Ruhige Startseite mit freier Wahl: kurze Einführung / geführtes Experiment / direkt zur Fliege |
| E3 | App-Sprachen | Deutsch + Englisch, umschaltbar, zweisprachig von Anfang an angelegt |
| E4 | Haupterlebnis | Verknüpfung ganzes Tier + Gehirn: Kausalkette mal aufs Tier, mal ins Gehirn gezoomt |
| E5 | Aussehen der Fliege | Drei Stile (stilisiert-schön / realistisch / abstrakt-lehrbuchhaft), jederzeit umschaltbar |
| E6 | Sinne ab Start | Geruch, Sehen, Berührung/Wind, Temperatur |
| E7 | Kausalkette | Gesamtkette sichtbar inklusive Details; jeder Schritt klickbar und erklärbar |
| E8 | Bedürfnisse | Ja, von Anfang an (Hunger, Erschöpfung, Erregung) |
| E9 | Gehirn-Detailtiefe | Drei wählbare Stufen; Überhitzungswarnung; geräteabhängige Empfehlung |
| E10 | Lernen | Kernfunktion (Belohnung, Vermeidung, Gewöhnung, Erinnerung) |
| E11 | Belege | Streng immer – jede Aussage überall gekennzeichnet |
| E12 | Erbgut | Fast gleich wichtig wie Gehirn; bis Basen-Ebene zoombar; **nach dem Kompakt-Kern** |
| E13 | Experimente | Vier Kernarten: Umwelt verändern; Gerüche/Stoffe; Gehirnbereiche stören; Versuche vergleichen |
| E14 | Zeit | Voll zurückspulbar; zusätzlich Zeitlupe und Zeitraffer; Pause; Wiederholung |
| E15 | Offline | Alles offline; KI optional, Dienst frei wählbar (BYOK) |
| E16 | KI-Aufgaben | Erklären, Experiment-Hilfe, Auswertung |
| E17 | Oberfläche | Querformat bevorzugt; dunkel/hell umschaltbar; ruhig, wissenschaftlich, kein Neon/Glas/Dashboard |
| E18 | Auswahl-Modi | Jederzeit wechselbar, auch mitten im Versuch |
| E19 | Tagebuch | Automatische Aufzeichnung + eigene Notizen + Bildschirmfotos + Versuchsvergleich |
| E20 | Erste Version | Kompakter Kern zuerst (Auflösung Widerspruch 1); Erbgut + mehrere Fliegen direkt danach |
| E21 | Geschlecht | Zuerst nur weiblich (passt zu Datenlage) |
| E22 | Echte Daten | Ja, FlyWire-Daten einbinden (adultes weibliches Tier); Übergangs-Übungsdatensatz erlaubt, klar markiert |
| E23 | Weitergabe | Experimente als Datei weitergebbar |
| E24 | Überlastung | Automatische Detailreduktion mit sichtbarem Hinweis (keine Rückfrage) |
| E25 | Zielgeräte | Samsung Galaxy A56 **und** OnePlus 6T |
| E26 | Denk-Sandbox | Kernbestandteil von FlyLab; architektonisch vorsehen, zeitlich nach Stufe 2 |
| E27 | Lebenszyklus | Ja (Altern, Tod, Paarung, Nachkommen) – aber als vom Nutzer wählbare Optionen |
| E28 | Mehrere Fliegen | Später, eine Handvoll (ca. 5–10), erst wenn Kausalketten-Erlebnis steht |
| E29 | Erklärstufen | Drei Stufen: ganz einfach / normal / ausführlich-wissenschaftlich |
| E30 | Abnahme | Experiment-Workflow: anlegen → laufen lassen → speichern → öffnen → vergleichen |

## 2. Technische Annahmen (ANNAHME – dem Auftraggeber vorgeschlagen, nicht von ihm entschieden)

| ID | Annahme | Quelle |
|---|---|---|
| A1 | Deutsch ist Voreinstellung der App-Sprache; Fachbegriffe erscheinen zusätzlich in Fachsprache | flylab-spec § 24.1 |
| A2 | Das geführte Eröffnungsexperiment zeigt die Kausalkette am Beispiel Geruch → Gehirn → Bewegung → Belohnung → Lernen | flylab-spec § 24.2 |
| A3 | Detailtiefe ist eine Ansichtseigenschaft: Wechsel mitten im Versuch ändert nur die Anzeige, nicht die laufende Simulation | flylab-spec § 24.3, § 17 |
| A4 | Rückspulen innerhalb eines Versuchs unbegrenzt, begrenzt durch Gerätespeicher; vollständige Aufzeichnung bleibt im Tagebuch | flylab-spec § 24.4 |
| A5 | Nach automatischer Reduktion keine automatische Zurückschaltung; der Nutzer stellt selbst zurück | flylab-spec § 24.5 |
| A6 | „Gehirnbereiche stören" wird als Simulationswerkzeug mit Kennzeichnung „Simulationseingriff" geführt, nicht als biologisches Ergebnis | flylab-spec § 24.6 |
| A7 | Basen-Ebene des Erbguts: zunächst Referenzdatensatz des weiblichen Tiers; individuelle Abweichungen durch Eingriffe/Vererbung, als MODELLIERT gekennzeichnet | flylab-spec § 24.7 |
| A8 | Erster Darstellungsstil bei der Umsetzung: „Abstrakt-lehrbuchhaft" (schnell und wissenschaftlich solide); die anderen folgen | flylab-spec § 28 (Team-Annahme) |

## 3. Widersprüche und ihre Auflösung (dokumentiert)

| Widerspruch | Auflösung |
|---|---|
| „Schnell etwas Lauffähiges" (Runde 4) vs. „Ganz groß von Anfang an" (Runde 8) | **Schnell gewinnt** (Runde 9): kompakter Kern zuerst; Erbgut-Ansicht und mehrere Fliegen direkt danach in kurzen Schritten |
| „Alles offline" vs. „KI aktiv einbinden" | Kein echter Widerspruch nach Klärung: Grundfunktionen komplett offline; KI optional, Dienst frei wählbar |
| „Streng immer Belege" vs. ruhige Optik | Kennzeichnung dauerhaft, aber dezente visuelle Markierung; vollständige Erklärungen auf Nachfrage/Antippen |

## 4. Offene Punkte (OFFEN – nicht erfinden!)

Unverändert aus `flylab-spec.md` § 23 (12 Punkte); verwaltet in `docs/RESEARCH_QUESTIONS.md`:

1. Welches geführte Experiment zuerst (Vorschlag: „Ein Geruch – und wie die Fliege ihn lernt")
2. Genaue Bedürfniszustände über Hunger/Erschöpfung/Erregung hinaus
3. Genaue Lebenszyklus-Umschalter (Altern/Tod/Paarung/Generationen-Zahl)
4. Zusätzliche Verhaltensweisen für Version 1 (Putzen, Springen …)
5. Reihenfolge späterer Sinne (Feuchtigkeit, Geräusch, Geschmack, Schwerkraft)
6. Gen-Wirkung auf Körper/Verhalten/Gehirn; welche Gene zuerst
7. Begrenzungen für Rückspul-Historie/Speicherplatz pro Gerät
8. Umfang/Mindestinhalt der Experiment-Weitergabedatei
9. Konkrete KI-Dienste neben Google/Gemini; Einbindungsreihenfolge
10. Hindernisse, Wasserquellen, andere Tiere als frei platzierbare Objekte: welche in welcher Stufe
11. Bildsprache der Aktivitätsdarstellung (Farben, Pulsformen)
12. Umbenennung/Feinschnitt der drei Darstellungsstile nach ersten Entwürfen

## 5. Regeln für künftige Entscheidungen

- Produktentscheidungen trifft der Auftraggeber. Das Bau-Team darf technische Entscheidungen treffen und muss Annahmen als ANNAHME markieren.
- Unbekanntes bleibt OFFEN; nichts wird erfunden, nur damit Dokumente vollständig wirken.
- Beleg-Kategorien werden nie stillschweigend geändert (SCIENTIFIC_MODEL.md § 1).
