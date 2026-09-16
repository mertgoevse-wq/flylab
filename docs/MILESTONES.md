# FlyLab – Milestones

**Quelle:** `docs/flylab-spec.md` § 20, § 27; Reihenfolge nach „Schnell gewinnt" (Widerspruchsauflösung 1).
Jeder Meilenstein hat klare Abnahmekriterien und ist klein genug, um in kurzen Bau-Schritten erreichbar zu sein.

---

## M0 – Stabilisiertes Fundament *(klein, schnell)*

**Ziel:** Das bestehende Android-Projekt baut und startet zuverlässig; Architektur-Trennung angelegt.
**Umfang:** Build läuft; App startet; Theme an ruhige Wissenschaftsoptik angepasst (dunkel/hell-Fähigkeit); Modultrennung (Simulation/Daten/Darstellung/Bedienung/Speichern) als Verzeichnisstruktur/Grenzen angelegt; leerer Experiment- und Tagebuch-Grundgerüst.
**Abnahme:** `gradle`-Build erfolgreich; App startet auf Emulator/Gerät; kein Crash; Theme folgt Systemeinstellung.
**Abhängigkeiten:** keine.

## M1 – Statische Fliege *(klein, schnell)*

**Ziel:** Erste 3D-Fliege sichtbar und interaktiv.
**Umfang:** Ein Darstellungsstil (ANNAHME A8: „Abstrakt-lehrbuchhaft"); drehen/zoomen/antippen; läuft in Querformat.
**Abnahme:** Fliege sichtbar, drehbar/zoombar; Antippen liefert ein Auswahl-Ereignis; flüssig auf OnePlus-6T-Klasse.
**Abhängigkeiten:** M0.

## M2 – Lebendige Welt

**Ziel:** Umgebung + vier Sinne + Bedürfnisse + Verhalten.
**Umfang:** Temperatur/Licht/Feuchtigkeit/Gerüche/Stoffe als stetige Felder; Geruch/Sehen/Berührung-Wind/Temperatur als Sinne; Hunger/Erschöpfung/Erregung; Grundverhalten (ruhen, erkunden, orientieren, nähern, meiden, essen).
**Abnahme:** Reiz setzen → Fliege reagiert plausibel und stärkeabhängig; Verhalten aus Simulationszustand.
**Abhängigkeiten:** M1.

## M3 – Sichtbare Kausalkette

**Ziel:** Die Kette Auslöser → … → Lernen ist sichtbar, klickbar, erklärbar.
**Umfang:** Ketten-Übersicht; jeder Schritt klickbar; Detailansicht je Schritt; beide Blickrichtungen (Tier/Gehirn); Einträge aus echten Simulationsereignissen.
**Abnahme:** Reiz → Kette zeigt alle Schritte live; jeder Schritt hat Erklärung in 3 Stufen mit Beleg-Kennzeichnung.
**Abhängigkeiten:** M2.

## M4 – Lernen beobachten

**Ziel:** Lernen als Kernfunktion sichtbar.
**Umfang:** Belohnung/Vermeidung/Gewöhnung/Erinnerung; Verbindungsänderungen mit Vorher/Nachher + Ursache; kurz-/langzeitig getrennt; explizite, konfigurierbare Lernregel (als SIMULIERT/MODELLIERT markiert).
**Abnahme:** Wiederholter Versuch zeigt geändertes Verhalten; Verbindungsänderung nachvollziehbar dargestellt.
**Abhängigkeiten:** M3.

## M5 – Experiment-Workflow *(zentrale Abnahme)*

**Ziel:** Anlegen → laufen lassen → speichern → später öffnen → vergleichen.
**Umfang:** Experiment-Engine; voll zurückspulbar + Weiterlaufen ab Punkt; Pause/Wiederholung/Schritt; Zeitlupe/Zeitraffer; Determinismus (Seed + Modellversion); Speichern/Öffnen; Vergleich von mind. 2 Versuchen.
**Abnahme:** Vollständiger Workflow ohne Absturz; gleiche Eingaben → gleiche Ergebnisse; Zurückspulen und Weiterlaufen funktioniert.
**Abhängigkeiten:** M4 (bzw. M2 für Basis-Versuche ohne Lernen).

## M6 – Gehirn-Ansicht „Bereiche"

**Ziel:** Ca. 50 benannte Bereiche mit Aktivität und Leitungen.
**Umfang:** Bereichsdaten (deutsch + Fachbegriff + Beleg-Level); Aktivitätsdarstellung; Übergangs-Übungsdatensatz klar markiert (bis FlyWire); Überhitzungswarnung + Geräte-Empfehlung + automatische Reduktion.
**Abnahme:** Bereiche drehbar/zoombar/antippbar; Aktivität sichtbar; Beleg-Kennzeichnung überall; Warnung/Empfehlung erscheinen.
**Abhängigkeiten:** M2 (Sinne/Aktivität); parallel zu M4/M5 möglich.

## M7 – Stufe-1-Fertigstellung

**Ziel:** Alle Stufe-1-Anforderungen (PRD M1–M23) erfüllt.
**Umfang:** Tagebuch (automatisch + Notizen + Fotos + Vergleich); DE/EN vollständig; dunkel/hell vollständig; Offline-Nachweis; Leistungsnachweis (A56 + OnePlus 6T); Beleg-Kennzeichnung 100 %.
**Abnahme:** Komplette PRD-Stufe-1-Checkliste erfüllt; Hauptkriterium (Experiment-Workflow) demonstriert.
**Abhängigkeiten:** M5, M6.

## M8 – Erbgut-Ansicht *(Stufe 2)*

**Ziel:** Genom-Viewer bis Basen-Ebene.
**Umfang:** Gesamterbgut → Chromosomen → Gene → Basen; blättern/anschauen/verändern/vergleichen; Referenzdaten unverändert (Overlays); Beleg-Kennzeichnung.
**Abnahme:** Bis Basen zoomen; Veränderung anlegen und als Simulationseingriff markiert; zwei Erbgüter vergleichbar.
**Abhängigkeiten:** M7.

## M9 – Mehrere Fliegen *(Stufe 2)*

**Ziel:** Ca. 5–10 Fliegen gleichzeitig.
**Umfang:** Unabhängige Zustände pro Tier; Kommunikation nur über modellierte Kanäle; Leistung gesichert.
**Abnahme:** 5–10 Fliegen laufen flüssig auf Zielgeräten; Vergleich zwischen Tieren möglich.
**Abhängigkeiten:** M7 (parallel zu M8).

## M10 – Stufe-3-Ausbau

**Ziel:** Fortpflanzung/Generationen als Option; Stoffe vertieft; „Gehirnbereiche stören" voll; KI (Erklären/Experiment-Hilfe/Auswertung, BYOK); Detailstufe 3.
**Abnahme:** jeweilige Funktionsdemos mit Beleg-Kennzeichnung; KI optional und „KI-generiert"-markiert.
**Abhängigkeiten:** M8/M9.

## M11+ – Langfristig

Denk-Sandbox (nach Stufe 2, architektonisch vorbereitet) · männliche Tiere/Balz · Schwarm (20+) · Evolution · erweiterte Sinne · Lehre/Forschung · Peripherie · iOS.
