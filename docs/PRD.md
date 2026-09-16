# FlyLab – Product Requirements (PRD)

**Quelle der Wahrheit:** `docs/flylab-spec.md`
**Status:** Aus dem abgeschlossenen Produktinterview (9 Runden) abgeleitet. Nichts hinzuerfunden; Unbekanntes als OFFEN/ANNAHME markiert.

---

## 1. Produktvision

FlyLab ist ein **wissenschaftlich ehrliches Simulationslabor für *Drosophila melanogaster*** auf Android. Der Nutzer beobachtet eine virtuelle Fliege, setzt Reize, schaut in ihr Nervensystem, verfolgt die Kette **Auslöser → Wahrnehmung → Aktivität im Nervensystem → Verarbeitung → Bewegung → Verhalten → Ergebnis → Lernen** und führt reproduzierbare Experimente durch – alles ohne Internet, mit ständiger Kennzeichnung dessen, was belegt ist und was nur Modell.

FlyLab soll sich wie ein **ruhiges wissenschaftliches Messinstrument** anfühlen – nicht wie ein Spiel und nicht wie ein „AI-Dashboard" (kein Neon, kein Glasteffekt, keine verspielte Optik).

## 2. Nutzerziele

1. **Phase A (jetzt):** Der Auftraggeber lernt mit der App umzugehen und die Fliege zu verstehen.
2. **Phase B:** Laien ohne Vorkenntnisse können FlyLab verständlich nutzen.
3. **Phase C:** Lehre (Schule/Hochschule) und Forschende nutzen FlyLab mit höchsten Ansprüchen an Belege und Nachvollziehbarkeit.

## 3. Muss-Funktionen (Stufe 1 – Kompakter Kern)

| # | Anforderung | Details |
|---|---|---|
| M1 | Stabile Android-App | Läuft auf Samsung Galaxy A56 **und** OnePlus 6T; Build reproduzierbar |
| M2 | Startseite mit freier Wahl | Ruhiger Startbildschirm: kurze Einführung / geführtes Experiment / direkt zur Fliege |
| M3 | Zweiersprachigkeit | Deutsch + Englisch umschaltbar, zweisprachig von Anfang an angelegt |
| M4 | Optik | Dunkel + hell umschaltbar (Voreinstellung = Systemeinstellung); ruhig, wissenschaftlich, modern; kein Neon/Glas/Dashboard |
| M5 | Orientierung | Querformat bevorzugt, Hochformat unterstützt |
| M6 | 3D-Fliege | Ein Darstellungsstil zuerst (ANNAHME A8: „Abstrakt-lehrbuchhaft"); drehen/zoomen/antippen; drei Stile insgesamt vorgesehen, jederzeit umschaltbar |
| M7 | Umgebung | Temperatur, Licht, Feuchtigkeit, Gerüche, Stoffe als stetige (nicht binäre) Größen, vom Nutzer veränderbar |
| M8 | Vier Sinne | Geruch, Sehen, Berührung/Wind, Temperatur – räumlich und stärkeabhängig |
| M9 | Bedürfnisse | Hunger/Sättigung, Erschöpfung, Erregungszustand – von Anfang an, beeinflussen Verhalten |
| M10 | Verhalten | Laufen, Orientieren, Nähern, Meiden/Fliehen, Ruhen, Nahrung suchen/aufnehmen, Erkunden |
| M11 | Kausalkette | Gesamtkette sichtbar; jeder Schritt anklickbar; Detailansicht je Schritt; beide Blickrichtungen (Tier/Gehirn); aus echten Simulationsereignissen erzeugt |
| M12 | Lernen | Belohnung, Vermeidung, Gewöhnung, Erinnerung; beobachtbar (Verbindungsänderung mit Vorher/Nachher + Begründung; als SIMULIERT/MODELLIERT markiert) |
| M13 | Experiment-Workflow | Anlegen → laufen lassen → speichern → später öffnen → vergleichen (mind. 2 Versuche nebeneinander) |
| M14 | Zeitsteuerung | Voll zurückspulbar bis zu einem Punkt + Weiterlaufen; Pause; Wiederholung; Zeitlupe; Zeitraffer |
| M15 | Nachvollziehbarkeit | Gleicher Startzustand + gleiche Zufallsgrundlage + gleiche Modellversion = gleiches Ergebnis |
| M16 | Beleg-Kennzeichnung | „Streng immer": jede biologische Aussage trägt MEASURED/PUBLISHED/DERIVED/MODELED(SIMULIERT)/HYPOTHESIS; Details auf Antippen |
| M17 | Erklärstufen | Drei umschaltbare Stufen: ganz einfach / normal / ausführlich-wissenschaftlich |
| M18 | Gehirn-Ansicht (Bereiche) | Ca. 50 benannte Bereiche mit Aktivität; Übergangsweise darf klar gekennzeichneter Übungsdatensatz stehen, bis FlyWire integriert ist |
| M19 | Leistungsmanagement | Drei Gehirn-Detailstufen wählbar; Überhitzungswarnung; Geräte-Empfehlung; **automatische Reduktion** bei Überlastung mit sichtbarem Hinweis |
| M20 | Tagebuch | Automatische Aufzeichnung aus echten Simulationsereignissen + eigene Notizen + Bildschirmfotos + Versuchsvergleich |
| M21 | Offline | Alles ohne Internet funktionsfähig (Simulation, Daten, Experimente, Tagebuch) |
| M22 | Versuchstagebuch-Eintrag | Frage/Ausgangslage, Einstellungen, Zufallsgrundlage, Datensatz-/Modellversion, Reize, Beobachtungen, Lern-/Verbindungsänderungen, Verhaltensänderungen, Ergebnis, Unsicherheit |
| M23 | Jederzeit wechselbar | Startverhalten, Darstellungsstil, Detailtiefe, Lebenszyklus-Optionen, Erklärstufe, Sprache, Lichtstimmung – auch mitten im Versuch |

## 4. Muss-Funktionen (Stufe 2 – direkt danach, in kurzen Schritten)

| # | Anforderung |
|---|---|
| S1 | Erbgut-Ansicht: Gesamterbgut → Abschnitte/Chromosomen → Gene → Basen; blättern, wählen, anschauen, verändern, zwei Erbgüter vergleichen; unbekannte Wirkungen bleiben offen gekennzeichnet |
| S2 | Mehrere Fliegen (ca. 5–10) gleichzeitig, unabhängige Zustände pro Tier (Erbgut, Geschlecht, Körperzustand, Nervenzustand, Erinnerung, Lernstand, Verhaltenszustand) |
| S3 | Experimente als Datei weitergeben (inkl. genug Information zur Nachstellung) |

## 5. Später (bestätigt, nicht Stufe 1/2)

- Gehirn-Detailstufen 2 („Bereiche + Beispielzellen") und 3 („so tief wie möglich")
- Fortpflanzung/Generationen als wählbare Simulationsoption; männliche Tiere (parametrisiertes Modell)
- Stoff-Experimente vertieft (Konzentration, Dauer, Aufnahmeort; Erholung)
- KI-Funktionen: Erklären (3 Stufen), Experiment-Hilfe (Vorschläge, Zusammenfassung), Auswertung (Muster finden) – Dienst frei wählbar (BYOK), immer „KI-generiert"-markiert, nie Voraussetzung
- Denk-Sandbox (virtuelle, abgeschottete Computerwelt; Kernbestandteil, Umsetzung nach Stufe 2)
- Experimentart „Gehirnbereiche stören" (Bereiche abschalten/dämpfen; als Simulationseingriff gekennzeichnet)

*Hinweis: „Gehirnbereiche stören" ist als eine der vier Kernexperimentarten bestätigt; die Umsetzung wird nicht vor dem Gehirn-Modul eingeplant (Abhängigkeit). Siehe TASK_GRAPH.md.*

## 6. Langfristige Ideen (Fernziele)

Lehre/Forschung-Funktionen; Schwarm (20+ Fliegen); Evolution über viele Generationen; erweiterte Sinnwelt; weitere virtuelle Tiere/allgemeine simulierte Systeme (konzeptionell getrennt); iOS; Android-Peripherie.

## 7. Explizit NICHT (Grenzen des Produkts)

- Kein Spiel mit erfundenen biologischen Fakten
- Keine menschliche Biologie (z. B. keine Endorphin-Belohnung); keine medizinischen Aussagen
- Kein vollständiges männliches Connectom (Datenlage nur weiblich)
- Keine Erfundung unbekannter Gen-Wirkungen
- KI wird niemals stillschweigend zur wissenschaftlichen Faktenquelle
- Kein Neon, kein Glasteffekt, keine „AI-Dashboard"-Optik
- KI nicht Voraussetzung irgendeiner Kernfunktion

## 8. Abnahmekriterien

**Hauptkriterium:** Experiment-Workflow vollständig: anlegen → laufen lassen (mit Pause, Zeitlupe, Zeitraffer) → speichern → später öffnen → vergleichen (mind. 2 Versuche nebeneinander).

**Stufe 1 zusätzlich:**
- Kausalkette sichtbar, klickbar, in drei Erklärstufen, mit dauerhafter Beleg-Kennzeichnung
- Lernen beobachtbar (Verhalten ändert sich; Verbindungen zeigen Vorher/Nachher)
- Rückspulen bis zu einem Punkt und Weiterlaufen ab dort
- Offline; deutsch/englisch; dunkel/hell; Querformat vorrangig
- Galaxy A56 **und** OnePlus 6T nutzbar; automatische Detailreduktion greift
- OnePlus-6T-Klasse: keine Dauerüberhitzungswarnung bei normaler Nutzung der Standard-Detailtiefe

**Stufe 2:** Erbgut-Ansicht bis Basen-Ebene nutzbar; ca. 5–10 Fliegen gleichzeitig.

## 9. Beibehaltene offene Punkte und Annahmen

- **12 OFFEN-Punkte:** unverändert aus `flylab-spec.md` § 23 (dort maßgeblich; Spiegelung in `docs/RESEARCH_QUESTIONS.md`)
- **8 ANNAHMEN (A1–A7, A8):** unverändert aus `flylab-spec.md` § 24 (dort maßgeblich; Spiegelung in `docs/DECISIONS.md`)
- **3 aufgelöste Widersprüche:** dokumentiert in `flylab-spec.md` § 25 und `docs/DECISIONS.md`

## 10. Metriken (vorgeschlagen, ANNAHME – nicht vom Auftraggeber entschieden)

- A-Start bis interaktive Fliege < 3 s auf Galaxy A56 (ANNAHME, technisch zu verifizieren)
- Simulation läuft flüssig auf OnePlus 6T bei Standard-Detailtiefe (Framerate-Ziel: technisch zu bestimmen, ANNAHME ≥ 30 fps)
- 100 % der biologischen UI-Texte tragen Beleg-Kennzeichnung (prüfbar via Tests/Review)
- Experiment-Workflow von „Neues Experiment" bis „Vergleich" ohne Absturz (Abnahmetest)
