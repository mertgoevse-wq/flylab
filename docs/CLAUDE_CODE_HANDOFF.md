# CLAUDE_CODE_HANDOFF – FlyLab

**Lese zuerst:** `CLAUDE.md` (verbindlicher Vertrag) → `docs/flylab-spec.md` (Quelle der Wahrheit für Produktentscheidungen) → dieses Dokument (Bau-Anweisung).
**Stand:** 16.09.2026. Interview abgeschlossen; Spezifikation abgenommen; Dokumenten-Familie abgeleitet. **Es gibt noch keine Implementierung außer dem Android-Platzhalter.**

---

## 1. Was FlyLab ist

FlyLab ist ein **wissenschaftlich ehrliches Simulationslabor für die Taufliege *Drosophila melanogaster*** auf Android. Der Nutzer beobachtet eine virtuelle Fliege, setzt Reize, schaut in ihr Nervensystem und verfolgt die Kette:

**Auslöser → Wahrnehmung → Aktivität im Nervensystem → Verarbeitung → Bewegung → Verhalten → Ergebnis → Lernen**

Er legt Experimente an, führt sie aus, speichert sie, öffnet sie später wieder und vergleicht sie. Jede biologische Aussage ist dauerhaft mit ihrer Belegart gekennzeichnet. Alles läuft offline; KI ist optional.

## 2. Vision

- **Phase A (jetzt):** Der Auftraggeber lernt FlyLab zu benutzen und die Fliege zu verstehen.
- **Phase B:** Laien ohne Vorkenntnisse verstehen die App.
- **Phase C:** Lehre und Forschung nutzen sie mit höchsten Ansprüchen an Belege und Reproduzierbarkeit.
- Erlebnis: ein **ruhiges wissenschaftliches Messinstrument** – kein Spiel, kein „AI-Dashboard", kein Neon, kein Glasteffekt.

## 3. Was bestätigt wurde (Produktentscheidungen – verbindlich)

Vollständige Liste mit Details: `docs/DECISIONS.md` (E1–E30). Die Kernpunkte:

- **Kompakter Kern zuerst** („Schnell gewinnt" – Auflösung Widerspruch 1): Fliege + Gehirn (Bereiche) + Reaktion + **Lernen** + Experiment-Workflow + Kausalkette + Zeitsteuerung; **dann** Erbgut und mehrere Fliegen in kurzen Schritten.
- Ruhige Startseite mit freier Wahl (Einführung / geführtes Experiment / direkt zur Fliege)
- Deutsch **und** Englisch umschaltbar; dunkel **und** hell umschaltbar; **Querformat bevorzugt**
- Vier Sinne ab Start (Geruch, Sehen, Berührung/Wind, Temperatur); Bedürfnisse von Anfang an; Lernen als Kernfunktion
- Belege **streng immer**; Erklärungen in **drei Stufen**; drei Gehirn-Detailstufen mit Überhitzungswarnung, Geräte-Empfehlung und **automatischer Reduktion**
- Voll zurückspulbar; Zeitlupe **und** Zeitraffer; deterministische Wiederholung
- Erbgut später (Stufe 2), aber bis Basen-Ebene; zuerst weibliche Fliege; echte FlyWire-Daten als Ziel (Übergangs-Übungsdatensatz klar markiert)
- Alles offline; KI optional (BYOK; Aufgaben: Erklären, Experiment-Hilfe, Auswertung)
- Tagebuch: automatisch + Notizen + Bildschirmfotos + Vergleich
- Zielgeräte: Samsung Galaxy A56 **und** OnePlus 6T
- Experimente als Datei weitergebbar; Denk-Sandbox = Kernbestandteil, aber nach Stufe 2
- Zentrale Abnahme: **Experiment-Workflow** (anlegen → laufen lassen → speichern → öffnen → vergleichen)

## 4. Was offen ist (OFFEN – nicht erfinden!)

12 offene Punkte in `docs/RESEARCH_QUESTIONS.md` Teil A (aus `flylab-spec.md` § 23), u. a.: erstes geführtes Experiment; weitere Bedürfniszustände; Lebenszyklus-Umschalter; weitere Verhaltensweisen; spätere Sinne; Gen-Wirkungs-Details; Rückspul-Grenzen; Weitergabedatei-Inhalt; KI-Dienste; platzierbare Objekte; Aktivitätsbildsprache; Stil-Namen.
**Regel:** Offenes bleibt offen oder wird als ANNAHME überbrückt (markiert, widerrufbar). Nicht als Entscheidung ausgeben.

## 5. Was angenommen wurde (ANNAHME – Team-Annahmen, widerrufbar)

`docs/DECISIONS.md` § 2 (A1–A8), u. a.: Deutsch als Voreinstellung (A1); Eröffnungsexperiment = Geruch→Gehirn→Bewegung→Belohnung→Lernen (A2); Detailtiefe ändert nur Ansicht, nicht Simulation (A3); Rückspulen unbegrenzt bis auf Gerätespeicher (A4); keine automatische Zurückschaltung nach Reduktion (A5); „Gehirnbereiche stören" = Simulationseingriff (A6); Basen-Ebene aus weiblichem Referenzdatensatz (A7); erster Darstellungsstil „Abstrakt-lehrbuchhaft" (A8).

## 6. Was zuerst gebaut werden muss

Konkreter Plan mit Abhängigkeiten: `docs/TASK_GRAPH.md`. Meilensteine: `docs/MILESTONES.md`. In Kürze:

- **M0 (klein!):** T-001 Build stabilisieren → T-002 Architektur-Trennung (Simulation/Daten/Darstellung/Bedienung/Speichern) → T-003 Theme auf ruhige Wissenschaftsoptik (dunkel/hell)
- **M1:** T-010 3D-Fundament → T-011 Fliege („Abstrakt-lehrbuchhaft", A8) → T-012 Startseite (3 Wahlen) + DE/EN
- **M2:** T-020 Sim-Kern (deterministisch, Ereignisstrom) → T-021 Umgebungsfelder → T-022 vier Sinne → T-023 Bedürfnisse → T-024 Verhalten
- **M3:** T-030 Kausalkette sichtbar/klickbar → T-031 Erklärsystem (3 Stufen + Belege) → T-032 Gehirn „Bereiche"
- **M4:** T-040 Lern-Engine → T-041 Vorher/Nachher-Darstellung
- **M5:** T-050 Experiment-Engine → T-051 Zurückspulen/Zeitlupe/Zeitraffer → T-052 Speichern/Öffnen/Vergleichen (**zentrale Abnahme**)
- **M6/M7:** T-060 Leistungsmanagement → T-061 Tagebuch → T-062 Stufe-1-Endprüfung

**Der kleinste schnell-laufende Pfad ist bewusst klein:** Nach M0+M1 existiert eine stabil gebaute, ruhig gestaltete, zweisprachige App mit interaktiver 3D-Fliege.

## 7. Was ausdrücklich NOCH NICHT gebaut werden soll

- ❌ Erbgut-Ansicht (Stufe 2, T-070) – nicht vor M7
- ❌ Mehrere Fliegen (Stufe 2, T-072) – nicht vor M7
- ❌ FlyWire-Vollintegration (T-071) – Bereichs-Ebene läuft vorher mit klar markierten Übungsdaten
- ❌ KI-Funktionen (T-083) – nicht vor M7; Kern läuft ohne
- ❌ Denk-Sandbox (T-090) – nach Stufe 2; nur architektonisch vorbereiten
- ❌ Fortpflanzung/männliche Tiere/Schwarm/Evolution-vertieft (T-080 ff.) – Stufe 3/4
- ❌ Cloud-Synchronisation – **keine** bestätigte Anforderung; nicht einbauen
- ❌ Keine neuen Abhängigkeiten ohne Begründung (Projektvertrag); keine Schlüssel im Quelltext

## 8. Wissenschaftliche Grenzen (niemals überschreiten)

- Keine Hypothese als Beleg darstellen. Beleg-Kategorien (BEOBACHTET/BELEGT/MODELLIERT/SIMULIERT/ABGELEITET/HYPOTHETISCH, siehe `docs/SCIENTIFIC_MODEL.md`) gelten **immer und überall** und werden nie stillschweigend geändert.
- Kein vollständiges männliches Connectom; männliche Simulation bleibt parametrisiert (MODELLIERT).
- Keine menschliche Biologie (z. B. Endorphine) auf die Fliege übertragen; belegte *Drosophila*-Botenstoffe (Dopamin u. a.) im erweiterbaren Botenstoff-System.
- Keine erfundenen Gen-Wirkungen; keine medizinischen Aussagen.
- Referenzdaten (Gehirn/Genom) werden nie verändert – Simulationsschichten darüber.
- Übergangs-„Übungsdaten" sind immer sichtbar als solche markiert.
- KI-Ausgaben: immer „KI-generiert", nie stillschweigend Fakten.
- Details/Grenzen: `docs/KNOWN_LIMITATIONS.md`.

## 9. Gewünschte Benutzererfahrung

Ruhige Startseite → freie Wahl → 3D-Fliege im Querformat, dunkel/hell, DE/EN → Reiz setzen → Kausalkette live beobachten (klickbar, 3 Erklärstufen, Belege) → ins Gehirn wechseln (Bereiche, Aktivität, Bahnen) → Lernen zusehen (Vorher/Nachher mit Ursache) → Experiment speichern, öffnen, vergleichen → alles im Versuchstagebuch. Bei Last: Warnung, Empfehlung, automatische Reduktion mit Hinweis. Details: `docs/UX_SPEC.md`, `docs/NARRATIVE_PRODUCT_SPEC.md`.

## 10. Abnahmekriterien

- **Zentrale Abnahme:** Experiment-Workflow vollständig (siehe § 3).
- Stufe-1-Checkliste: `docs/PRD.md` § 3 (M1–M23), Nachweis-Plan: `docs/VALIDATION.md`.
- Kritische Tests (Projektvertrag § 37): **Determinismus** (gleicher Seed + Modell + Experiment = identisches Ergebnis), Zurückspulen/Weiterlaufen, Persistenz nach App-Tötung, Beleg-Vollständigkeit, Offline, Sandbox-Isolation (später), Leistung auf Zielgeräten.
- Stufe 2: Erbgut bis Basen-Ebene; 5–10 Fliegen; Datei-Round-trip.

## 11. Aktuelle Prioritäten

1. **P0:** M0→M5 (kompakter Kern inkl. zentraler Abnahme) + M6-Leistungsmanagement
2. **P1:** Tagebuch, Stufe-1-Endprüfung
3. **P2:** Erbgut, mehrere Fliegen, FlyWire, Datei-Weitergabe, Detailstufe „Beispielzellen"
4. **P3:** Fortpflanzung, Stoffe vertieft, KI, Detailstufe 3
5. **P4:** Denk-Sandbox, männliche Tiere, Schwarm, Evolution, Lehre/Forschung, Peripherie, iOS

## 12. Nächste konkrete Aufgaben (starte hier, autonom)

1. **T-001:** Android-Projekt stabilisieren (`./gradlew assembleDebug` + `test` grün; App startet)
2. **T-002:** Architektur-Trennung anlegen; Simulationsmodul UI-frei und testbar
3. **T-003:** Theme auf Wissenschaftsoptik (dunkel/hell, Systemvoreinstellung, keine Platzhalterfarben)
4. **T-012:** Startseite mit drei Wahlen + DE/EN-Ressourcen
5. **T-010/T-011:** 3D-Fundament + Fliege (A8-Stil)
6. Danach M2–M5 gemäß TASK_GRAPH.md, jeder Schritt: bauen → testen → dokumentieren → weiter.

**Arbeitsweise (aus dem Projektvertrag, verbindlich):** kleine überprüfbare Schritte; nach jedem größeren Schritt Build/Tests; Fehler autonom untersuchen/reparieren/erneut testen; Projektzustand in `docs/PROJECT_STATE.md` aktualisieren; keine Fragen an den Nutzer bei technischen Entscheidungen – entscheiden, als ANNAHME dokumentieren, weiterbauen. Ziel ist keine Entwurfssammlung, sondern ein tatsächlich funktionierendes FlyLab.

## 13. Dokumenten-Landkarte

| Zweck | Datei |
|---|---|
| Quelle der Wahrheit (Produkt) | `docs/flylab-spec.md` |
| Verbindlicher Vertrag | `CLAUDE.md` |
| Produktanforderungen | `docs/PRD.md` |
| Erzählende Beschreibung | `docs/NARRATIVE_PRODUCT_SPEC.md` |
| Projektzustand | `docs/PROJECT_STATE.md` |
| Belegsystem | `docs/SCIENTIFIC_MODEL.md` |
| Simulation | `docs/SIMULATION_MODEL.md` |
| Erbgut | `docs/GENETICS_MODEL.md` |
| Experimente | `docs/EXPERIMENT_MODEL.md` |
| Daten/Provenienz | `docs/DATA_MODEL.md` |
| KI | `docs/AI_MODEL.md` |
| Oberfläche | `docs/UX_SPEC.md` |
| Entscheidungen/Annahmen/Widersprüche | `docs/DECISIONS.md` |
| Reihenfolge | `docs/ROADMAP.md` |
| Meilensteine | `docs/MILESTONES.md` |
| Aufgabenplan | `docs/TASK_GRAPH.md` |
| Validierung/Abnahme | `docs/VALIDATION.md` |
| Grenzen | `docs/KNOWN_LIMITATIONS.md` |
| Offene Punkte | `docs/RESEARCH_QUESTIONS.md` |
| Diese Übergabe | `docs/CLAUDE_CODE_HANDOFF.md` |
