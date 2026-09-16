# FlyLab – Task Graph (konkreter Aufgabenplan für den Bau)

**Quellen:** `docs/flylab-spec.md` § 20, § 27, § 28; `docs/MILESTONES.md`; `docs/PRD.md`
**Regeln:** Jede Aufgabe hat ID, Ziel, Beschreibung, Abhängigkeiten, Priorität, erwartetes Ergebnis, Abnahmekriterium, benötigte Fähigkeiten, Testmöglichkeit. Große Aufgaben sind in kleine Schritte geteilt. **Der erste Meilenstein ist bewusst klein und schnell lauffähig.** Die Reihenfolge wurde nur wegen tatsächlicher Abhängigkeiten angepasst.

**Prioritäten:** P0 = kritisch für Stufe 1 · P1 = Stufe 1 · P2 = Stufe 2 · P3 = Stufe 3+ · P4 = langfristig
**Status-Werte:** `offen` / `in Arbeit` / `erledigt`

---

## Meilenstein M0 – Fundament

### T-001 — Android-Projekt stabilisieren
- **Ziel:** Zuverlässiger, reproduzierbarer Build der bestehenden App.
- **Beschreibung:** Bestehendes Gradle-Projekt prüfen (`app/`, `minSdk 21`, `targetSdk 34`); Build und Unit-Test-Lauf verifizieren; Abhängigkeiten auf nötiges Minimum prüfen (Projektvertrag: keine unnötigen Abhängigkeiten); Gradle-Wrapper-Konfiguration dokumentieren.
- **Abhängigkeiten:** keine
- **Priorität:** P0
- **Erwartetes Ergebnis:** `./gradlew assembleDebug` und `./gradlew test` laufen erfolgreich.
- **Abnahmekriterium:** Build erfolgreich ohne Warnungen, die auf kaputte Konfiguration hindeuten; App installiert und startet.
- **Fähigkeiten:** Gradle, Android-Bau, Kotlin.
- **Test:** Build + bestehender Testlauf (`./gradlew test`).

### T-002 — Architektur-Trennung anlegen
- **Ziel:** Verbindliche Systemtrennung physisch im Code anlegen.
- **Beschreibung:** Verzeichnis-/Modulgrenzen für Simulation, Daten, Darstellung, Versuchssteuerung, Bedienoberfläche, Speichern (Projektvertrag). Simulation darf keine UI-Importe haben. Leere Grundgerüste für Experiment-Engine und Tagebuch.
- **Abhängigkeiten:** T-001
- **Priorität:** P0
- **Erwartetes Ergebnis:** Klare Paketstruktur; Simulationscode isoliert testbar.
- **Abnahmekriterium:** Simulationsmodul kompiliert und wird in Unit-Tests ohne Android-UI referenziert.
- **Fähigkeiten:** Kotlin, Clean Architecture/MVVM-Grundprinzipien.
- **Test:** Unit-Test für eine Simulations-Klasse ohne Android-Kontext.

### T-003 — Theme auf Wissenschaftsoptik umstellen
- **Ziel:** Ruhige, wissenschaftliche Optik (dunkel/hell) statt Platzhalterfarben.
- **Beschreibung:** `FlyLabTheme.kt` ersetzen: gedämpfte, kontrastreiche Paletten (dunkel = „Messinstrument", hell = kliner Labor-Look), Voreinstellung = Systemeinstellung, umschaltbar; **kein** Neon/Glas/Dashboard-Look.
- **Abhängigkeiten:** T-001
- **Priorität:** P0
- **Erwartetes Ergebnis:** Zwei Paletten; Umschaltung möglich.
- **Abnahmekriterium:** Keine Platzhalterfarben (0xFF6200EE etc.) mehr; dunkel/hell folgt System + manuelle Umschaltung.
- **Fähigkeiten:** Compose, Material 3, Desigsystem.
- **Test:** Screenshot-Vergleich hell/dunkel; Theme-Unit-Test (Systemeinstellung → richtige Palette).

---

## Meilenstein M1 – Statische Fliege

### T-010 — 3D-Ansichtsfundament
- **Ziel:** GPU-freundliche 3D-Szene in der App.
- **Beschreibung:** Renderer-Grundgerüst (mobile-first: Detailstufen-Fähigkeit, nur Sichtbares zeichnen; konkrete Technik wählt das Team, z. B. Filament/SceneView oder eigener Renderer – **technische Entscheidung**, keine unnötige Abhängigkeit einführen ohne Begründung).
- **Abhängigkeiten:** T-002
- **Priorität:** P0
- **Erwartetes Ergebnis:** Leere interaktive 3D-Szene in der App.
- **Abnahmekriterium:** Szene rendert flüssig auf OnePlus-6T-Klasse; Kamera drehbar/zoombar.
- **Fähigkeiten:** Android 3D/GPU, Performance.
- **Test:** Framerate-Messung auf Zielgeräteklasse (Emulator-Profil + Device-Test falls verfügbar).

### T-011 — Fliegenmodell „Abstrakt-lehrbuchhaft" (ANNAHME A8)
- **Ziel:** Erste Fliege sichtbar.
- **Beschreibung:** Ein 3D-Modell im Lehrbuch-Stil (vereinfacht, anatomisch korrekt proportioniert: Körper, Kopf, Beine, Flügel); Körperteile antippbar; Stil ist Austausch-Container für die zwei weiteren Stile (stilisiert-schön, realistisch – später).
- **Abhängigkeiten:** T-010
- **Priorität:** P0
- **Erwartetes Ergebnis:** Fliege drehbar/zoombar/antippbar.
- **Abnahmekriterium:** Antippen eines Körperteils liefert Auswahl-Ereignis mit Namen; flüssig auf Zielgeräten.
- **Fähigkeiten:** 3D-Modellerstellung/-Einbindung (glTF/GLB), Compose-Integration.
- **Test:** Instrumentierter Test: Auswahl-Ereignis pro Körperteil.

### T-012 — Grundoberfläche: Startseite + Navigationsgerüst
- **Ziel:** Ruhige Startseite mit drei Wahlen.
- **Beschreibung:** Startseite (kurze Einführung / geführtes Experiment / direkt zur Fliege); Navigation; Querformat bevorzugt (Hochformat unterstützt); **DE/EN zweisprachig angelegt** (Ressourcen `values`/`values-en`); Einstellungs-Schirm (Sprache, Stimmung) mit „jederzeit wechselbar"-Prinzip.
- **Abhängigkeiten:** T-003
- **Priorität:** P0
- **Erwartetes Ergebnis:** Drei Startwege funktionieren; Sprache/Stimmung umschaltbar.
- **Abnahmekriterium:** Alle drei Wege erreichbar; Umschaltung wirkt überall ohne Neustart (Prüfung im Vier-Augen-Test durch UI-Durchlauf).
- **Fähigkeiten:** Compose, Navigation, Lokalisierung.
- **Test:** UI-Test: Startwege; Sprachumschaltung DE↔EN.

---

## Meilenstein M2 – Lebendige Welt

### T-020 — Simulations-Kern (Zeit, Zustand, Ereignisstrom)
- **Ziel:** Frame-rate-unabhängiger, deterministischer Simulations-Kern.
- **Beschreibung:** Simulationsuhr (zeitunabhängig, Hintergrund-fähig), Zustandsverwaltung, Ereignisstrom (für Kausalkette/Tagebuch), Seed-Verwaltung; Ereignisse sind echte Simulationsausgaben.
- **Abhängigkeiten:** T-002
- **Priorität:** P0
- **Erwartetes Ergebnis:** Simulations-Kern ohne UI lauffähig; Ereignisse beobachtbar.
- **Abnahmekriterium:** Gleicher Seed + gleicher Zustand → identischer Ereignisstrom (Determinismus-Test).
- **Fähigkeiten:** Kotlin, Simulation, Testdesign.
- **Test:** Determinismus-Unit-Test (Projektvertrag § 37: kritischer Test).

### T-021 — Umgebung als stetige Felder
- **Ziel:** Temperatur, Licht, Feuchtigkeit, Gerüche, Stoffe als veränderbare Felder.
- **Beschreibung:** Fortlaufende Werte (kein An/Aus), örtlich verteilte Felder + globale Werte; Tag/Nacht-Zyklus/Tageszeit; Nahrung/Wasser als Objekte; Nutzer-Veränderung zur Laufzeit möglich.
- **Abhängigkeiten:** T-020
- **Priorität:** P0
- **Erwartetes Ergebnis:** Umgebungswerte veränderbar; Fliege „steht" in den Feldern.
- **Abnahmekriterium:** Änderung einer Größe ist im Simulationszustand messbar (z. B. Konzentrationsprofil am Fliegenort).
- **Fähigkeiten:** Simulations-/Feldmathematik (leichtgewichtig).
- **Test:** Unit-Test Feldausbreitung/Abklingverhalten.

### T-022 — Vier Sinne
- **Ziel:** Geruch, Sehen, Berührung/Wind, Temperatur.
- **Beschreibung:** Sinne lesen Umgebungsfelder am Fliegenort; Intensitäts-/Konzentrationsabhängigkeit; Ausgabe an den Ereignisstrom („welcher Sinn hat ausgelöst, wie stark").
- **Abhängigkeiten:** T-021
- **Priorität:** P0
- **Erwartetes Ergebnis:** Wahrnehmungsereignisse mit Stärke fließen in Simulation.
- **Abnahmekriterium:** Jeder der vier Sinne erzeugt bei passendem Reiz ein Wahrnehmungsereignis; Stärke skaliert mit Reizstärke.
- **Fähigkeiten:** Sensorik-Modellierung (vereinfacht).
- **Test:** Unit-Test pro Sinn (Reizstärke → Wahrnehmungsstärke).

### T-023 — Bedürfnisse
- **Ziel:** Hunger/Sättigung, Erschöpfung, Erregung.
- **Beschreibung:** Innere Zustände verändern sich durch Zeit, Nahrungsaufnahme, Anstrengung, Ereignisse (z. B. Beinahe-Kollision); wirken auf Verhaltensauswahl; genaue Feinabstufung bleibt OFFEN (flylab-spec § 23.2) – Start mit den drei genannten.
- **Abhängigkeiten:** T-020
- **Priorität:** P0
- **Erwartetes Ergebnis:** Bedürfniswerte in Simulation sichtbar.
- **Abnahmekriterium:** Hunger sinkt/rise mit Essen; Erschöpfung nach Anstrengung; Erregung nach Schreck.
- **Fähigkeiten:** Simulationsdesign.
- **Test:** Unit-Test Bedürfnisdynamik.

### T-024 — Verhaltens-Engine
- **Ziel:** Grundverhalten aus Simulationszustand.
- **Beschreibung:** Verhaltensweisen: ruhen, erkunden, orientieren, nähern, meiden/fliehen, essen, drehen/laufen; Auswahl aus Sinnen + Bedürfnissen (+ später Erinnerung); Bewegung im Raum; Fliege bewegt sich sichtbar in der 3D-Szene.
- **Abhängigkeiten:** T-022, T-023
- **Priorität:** P0
- **Erwartetes Ergebnis:** Fliege verhält sich plausibel und nachvollziehbar.
- **Abnahmekriterium:** Geruchsquelle → Annäherung (bei Hunger); Bedrohungsreiz → Meidung/Flucht; Verhalten folgt Zustand, nicht Zufall.
- **Fähigkeiten:** Verhaltenslogik/Zustandsautomat oder vergleichbar.
- **Test:** Verhaltens-Unit-Tests (Zustand → Verhalten); Visueller Smoke-Test.

---

## Meilenstein M3 – Sichtbare Kausalkette

### T-030 — Kausalketten-Darstellung
- **Ziel:** Kette sichtbar, klickbar, erklärbar.
- **Beschreibung:** Ketten-Übersicht (Auslöser → Wahrnehmung → Nervenaktivität → Verarbeitung → Bewegung → Verhalten → Ergebnis → Lernen) aus dem Ereignisstrom; jeder Schritt klickbar; Detailansicht (beteiligte Bereiche, Stärken, Leitungen); beide Blickrichtungen (Tier/Gehirn).
- **Abhängigkeiten:** T-024
- **Priorität:** P0
- **Erwartetes Ergebnis:** Live-Kette während des Versuchs.
- **Abnahmekriterium:** Jeder Schritt klickbar mit Erklärung; Kette entspricht dem echten Ereignisstrom (keine erfundenen Einträge).
- **Fähigkeiten:** Compose-UI, Ereignisstrom-Anbindung.
- **Test:** UI-Test: Ketten-Schritte klickbar; Inhalt = Ereignisstrom.

### T-031 — Erklärsystem (3 Stufen + Belege)
- **Ziel:** Drei Erklärstufen + dauerhafte Beleg-Kennzeichnung.
- **Beschreibung:** Erklärtexte als Datenobjekte (Objektbezug, 3 Stufen, Beleg-Kategorie, Quelle); dezente dauerhafte Markierung; Antippen zeigt „Was ist bekannt? Was ist Modell? Wie sicher?"; DE/EN.
- **Abhängigkeiten:** T-012 (UI), T-030
- **Priorität:** P0
- **Erwartetes Ergebnis:** Erklärbares System für alle biologischen Inhalte.
- **Abnahmekriterium:** Jede biologische UI-Aussage trägt eine Kategorie aus SCIENTIFIC_MODEL.md; 3 Stufen umschaltbar.
- **Fähigkeiten:** Compose, Datenmodell, Textarchitektur.
- **Test:** Struktur-Test: jedes erklärte Objekt hat 3 Stufen + Beleg-Kategorie (Vollständigkeitsprüfung).

### T-032 — Detailstufe „Bereiche" des Gehirns
- **Ziel:** Ca. 50 benannte Bereiche mit Aktivität und Leitungen.
- **Beschreibung:** Bereichsdaten (Name DE + Fachbegriff, Funktion, Beleg-Level, Quelle, Version); Aktivität aus Simulation (Ebene L1); Leitungen/Bahnen zwischen Bereichen; Übergangs-Übungsdatensatz **klar als „Übungsdaten" markiert** (bis FlyWire, Entscheidung E22); Drehen/zoomen/antippen/ein-/ausblenden/isolieren.
- **Abhängigkeiten:** T-010, T-024 (Aktivität), T-031 (Belege)
- **Priorität:** P0
- **Erwartetes Ergebnis:** Interaktive Gehirnansicht auf Bereichsebene.
- **Abnahmekriterium:** Bereiche sichtbar, antippbar, aktivitätsgestalt; Übungsdatensatz-Kennzeichnung sichtbar; flüssig auf Zielgeräten.
- **Fähigkeiten:** 3D-Datenvisualisierung, Datenaufbereitung.
- **Test:** Unit-Test Bereichsdaten (Vollständigkeit: jeder Bereich hat Name/Beleg/Quelle); visueller Test.

---

## Meilenstein M4 – Lernen beobachten

### T-040 — Lern-Engine (explizite Regel)
- **Ziel:** Belohnung, Vermeidung, Gewöhnung, Erinnerung.
- **Beschreibung:** Explizite, konfigurierbare Lernregel (Beispiel im Projektvertrag: Δw ∝ vor × nach × Belohnungssignal; **exakte Formel = technische Entscheidung**, als SIMULIERT/MODELLIERT markiert); Baseline-Gewicht vs. aktuelles Gewicht (Overlay-Prinzip, Referenzdaten nie verändert); jede Änderung mit Ursache + Zeitstempel; kurz-/langzeitgedächtnis getrennt.
- **Abhängigkeiten:** T-024, T-032
- **Priorität:** P0
- **Erwartetes Ergebnis:** Lernen verändert Simulationszustand nachvollziehbar.
- **Abnahmekriterium:** Belohnung nach Verhalten → erhöhte Neigung; Bestrafung → erniedrigte; Gewöhnung bei wiederholtem harmlosem Reiz; deterministisch bei gleichem Seed.
- **Fähigkeiten:** Simulations-/Lernlogik (nicht maschinelles Lernen nötig).
- **Test:** Unit-Tests pro Lernmechanismus; Determinismus-Test.

### T-041 — Lern-Darstellung (Vorher/Nachher)
- **Ziel:** Lernen sichtbar machen.
- **Beschreibung:** Verbindungsänderungen mit Vorher/Nachher-Werten + Begründung („warum geändert"); Kennzeichnung SIMULIERT/MODELLIERT; Zeitpunkte; vorläufige Timeline der Lernereignisse.
- **Abhängigkeiten:** T-040, T-032
- **Priorität:** P0
- **Erwartetes Ergebnis:** Nutzer sieht, WAS sich geändert hat und WARUM.
- **Abnahmekriterium:** Jede gezeigte Änderung hat: baseline, aktuell, Änderung, Grund, Status.
- **Fähigkeiten:** Compose, Datenvisualisierung.
- **Test:** UI-Test: Lernereignis → dargestellte Änderung stimmt mit Simulationsdaten überein.

---

## Meilenstein M5 – Experiment-Workflow (zentrale Abnahme)

### T-050 — Experiment-Engine
- **Ziel:** Anlegen und Ausführen von Experimenten.
- **Beschreibung:** Experiment-Objekt (Ausgangslage, Einstellungen, Reize, Optionen); Start/Ziel/Steuerung der Simulation; Reiz-Zeitpläne; läuft über den Simulations-Kern (T-020).
- **Abhängigkeiten:** T-024, T-040
- **Priorität:** P0
- **Erwartetes Ergebnis:** Experimente konfigurierbar und ausführbar.
- **Abnahmekriterium:** Konfiguriertes Experiment läuft deterministisch ab.
- **Fähigkeiten:** Domänenlogik.
- **Test:** Unit-Test: Experiment-Konfiguration → erwartete Ereignisse.

### T-051 — Zeitsteuerung: Zurückspulen, Zeitlupe, Zeitraffer
- **Ziel:** Voll zurückspulbar + Weiterlaufen; Tempo einstellbar.
- **Beschreibung:** Aufzeichnung des Ereignis-/Zustandsstroms; Zurückspulen bis zu einem Punkt + Weiterlaufen ab dort; Pause/Wiederholung/Schritt; Zeitlupe + Zeitraffer; **ANNAHME A4:** Historie unbegrenzt innerhalb des Versuchs (Gerätespeicher-Grenze), Speicherplatz-Anzeige.
- **Abhängigkeiten:** T-050
- **Priorität:** P0
- **Erwartetes Ergebnis:** Voller Zeit-Komfort wie spezifiziert.
- **Abnahmekriterium:** Zurückspulen → Zustand entspricht exakt dem Zeitpunkt; Weiterlaufen ab dort deterministisch korrekt; Zeitlupe/Zeitraffer wirken auf Simulation (nicht nur Animation).
- **Fähigkeiten:** Zustands-Historie/Snapshotting oder re-Simulation.
- **Test:** Unit-Test: zurückspulen → weiterlaufen = ursprünglicher Verlauf; Speicherbegrenzungs-Verhalten.

### T-052 — Speichern, Öffnen, Vergleichen
- **Ziel:** Workflow-Ende: speichern → später öffnen → vergleichen.
- **Beschreibung:** Persistenz der Experimente (offline); Öffnen mit vollem Zustand; Vergleichsansicht für mind. 2 Versuche (Einstellungen, Verlauf, Verhalten, Lernänderungen, Ergebnis); Datei-Weitergabe vorbereiten (Format: Startzustand + Einstellungen + Seed + Modellversion; tatsächlicher Export als Stufe-2-Aufgabe T-060).
- **Abhängigkeiten:** T-050, T-051
- **Priorität:** P0
- **Erwartetes Ergebnis:** Zentrale Abnahme erfüllt.
- **Abnahmekriterium:** Anlegen → laufen lassen → speichern → schließen → öffnen → vergleichen, ohne Absturz; Vergleich zeigt beide Versuche korrekt.
- **Fähigkeiten:** Persistenz (offline), UI.
- **Test:** End-to-End-Test des Workflows; Neustart-Test (App-Tötung → Öffnen).

---

## Meilenstein M6 – Gehirn-Perfektion + Leistung

### T-060 — Leistungsmanagement
- **Ziel:** Drei Detailstufen + Schutzmechanismen.
- **Beschreibung:** Detailstufen-Auswahl (Stufe 1 aktiv, 2/3 später); Überhitzungswarnung; Geräte-Empfehlung (welche Stufe für welches Gerät geeignet); **automatische Reduktion** bei Überlastung mit sichtbarem Hinweis (ANNAHME A5: keine automatische Zurückschaltung).
- **Abhängigkeiten:** T-032
- **Priorität:** P0
- **Erwartetes Ergebnis:** Schutzmechanismen aktiv.
- **Abnahmekriterium:** Künstliche Last → Warnung; automatische Reduktion greift und wird angezeigt; auf OnePlus-6T-Klasse keine Dauerüberhitzungswarnung bei Standard-Detailtiefe.
- **Fähigkeiten:** Android-Leistung (Thermal-API o. ä. – technische Entscheidung).
- **Test:** Last-Simulation; Framerate/Thermal-Messung auf Zielprofilen.

### T-061 — Tagebuch
- **Ziel:** Automatische Aufzeichnung + Notizen + Fotos + Vergleich.
- **Beschreibung:** Automatischer Eintrag pro Versuch (Pflichtfelder siehe EXPERIMENT_MODEL.md § 6, aus echten Simulationsereignissen); eigene Notizen; Bildschirmfotos aus der laufenden Simulation; Vergleichsverweise.
- **Abhängigkeiten:** T-052
- **Priorität:** P1
- **Erwartetes Ergebnis:** Vollständiges Tagebuch.
- **Abnahmekriterium:** Jeder Versuch erzeugt automatisch einen vollständigen Eintrag; Notizen/Fotos bleiben nach Neustart erhalten.
- **Fähigkeiten:** Persistenz, Compose.
- **Test:** End-to-End: Versuch → Eintrag → Notiz → Foto → Neustart → alles da.

### T-062 — Stufe-1-Endprüfung
- **Ziel:** Alle PRD-Stufe-1-Anforderungen verifiziert.
- **Beschreibung:** Checkliste M1–M23 aus PRD.md abarbeiten; Offline-Nachweis (Flugmodus); Leistungsnachweis (A56 + OnePlus-6T-Klasse); Beleg-Kennzeichnung 100 %.
- **Abhängigkeiten:** alle Stufe-1-Tasks
- **Priorität:** P0
- **Erwartetes Ergebnis:** Stufe 1 abnahmefertig.
- **Abnahmekriterium:** PRD-§ 8-Kriterien erfüllt (Hauptkriterium: Experiment-Workflow).
- **Fähigkeiten:** QA, Abnahmetest.
- **Test:** Strukturierter Abnahmedurchlauf + Logcat-Audit (Crash-frei).

---

## Stufe 2 – direkt danach

### T-070 — Erbgut-Ansicht (bis Basen-Ebene)
- **Abhängigkeiten:** T-062 · **Priorität:** P2
- **Beschreibung:** Genom-Viewer: Gesamterbgut → Chromosomen → Gene → Basen (ANNAHME A7: Referenzdatensatz weibliches Tier); blättern/anschauen/verändern/vergleichen; Referenzdaten unverändert (Overlays); Beleg-Kennzeichnung; unbekannte Wirkungen OFFEN/HYPOTHETISCH (flylab-spec § 23.6 bleibt offen).
- **Abnahme:** Bis Basen zoomen; Veränderung als Simulationseingriff markiert; zwei Erbgüter vergleichbar.
- **Test:** Vollständigkeits-/Integritätstest der Genomdaten; UI-Durchlauf.

### T-071 — FlyWire-Datenintegration (echte Daten)
- **Abhängigkeiten:** T-032, T-070 (parallel möglich) · **Priorität:** P2
- **Beschreibung:** Echten FlyWire-Datensatz (adult, weiblich) einbinden (Entscheidung E22); Datensatz-Objekt mit ID/Version/Quelle/Lizenz/Prüfsumme/Abrufdatum (DATA_MODEL.md § 3); speicherbewusstes Laden (nicht alle ~139k Neuronen gleichzeitig); Übungsdatensatz-Kennzeichnung entfernen.
- **Abnahme:** Echte Daten geladen und nutzbar; Provenienz sichtbar; Speicherverbrauch auf Zielgeräten beherrschbar.
- **Test:** Integritätstest (Prüfsumme); Speicher-/Ladezeit-Messung.

### T-072 — Mehrere Fliegen (5–10)
- **Abhängigkeiten:** T-062 · **Priorität:** P2
- **Beschreibung:** Unabhängige Zustände pro Tier (Erbgut, Geschlecht, Körperzustand, Nervenzustand, Erinnerung, Lernstand, Verhaltenszustand); Kommunikation nur über modellierte Kanäle; Leistung gesichert (Projektvertrag § 31).
- **Abnahme:** 5–10 Fliegen flüssig auf Zielgeräten; tierübergreifender Vergleich möglich.
- **Test:** Multi-Agent-Unit-Tests; Performance-Test.

### T-073 — Experimente als Datei weitergeben
- **Abhängigkeiten:** T-052 · **Priorität:** P2
- **Beschreibung:** Export/Import-Datei (Startzustand + Einstellungen + Seed + Modellversion; Mindestinhalt ist **OFFEN**, flylab-spec § 23.8 – Start: „genug zur Nachstellung").
- **Abnahme:** Export → (anderes Gerät/Neuinstallation) → Import → identische Nachstellung (Determinismus).
- **Test:** Round-trip-Test Export/Import/Nachstellung.

### T-074 — Gehirn-Detailstufe „Bereiche + Beispielzellen"
- **Abhängigkeiten:** T-032, T-060, T-071 · **Priorität:** P2
- **Beschreibung:** Ausgewählte Nervenzellen und Bahnen einzeln anschauen (Ebene L2/L3); Aktivität auf Zell-/Populationsebene; Detailtiefe-Warnungen greifen.
- **Abnahme:** Beispielzellen sichtbar/antippbar; Leistung auf Zielgeräten ok (mit Warnung/Empfehlung).
- **Test:** Performance-Test; visuelle Prüfung.

---

## Stufe 3+

### T-080 — Fortpflanzung/Generationen (wählbare Option) · P3
- **Abhängigkeiten:** T-072 · **Beschreibung:** Lebenszyklus-Umschalter (Altern/Tod/Paarung/Generationen – genaue Umschalter **OFFEN**, flylab-spec § 23.3); Vererbung als vereinfachtes Modell (GENETICS_MODEL.md § 5); Priorität: erst nach Stufe 2.
- **Abnahme:** Option aktivierbar; Generationen mit nachvollziehbarer Vererbung, als MODELLIERT gekennzeichnet.
- **Test:** Vererbungs-Unit-Tests.

### T-081 — Stoff-Experimente vertieft · P3
- **Abhängigkeiten:** T-021, T-062 · **Beschreibung:** Konzentration, Dauer, Aufnahmeort; Wirkung auf Verhalten/Nervensystem/Erholung; fehlende Datenlage → MODELLIERT/HYPOTHETISCH/offene Frage (flylab-spec § 13).
- **Abnahme:** Stoff-Experiment mit vollständiger Kennzeichnung durchführbar.
- **Test:** Wirkmodell-Unit-Tests.

### T-082 — „Gehirnbereiche stören" (voll) · P3
- **Abhängigkeiten:** T-032, T-050 · **Beschreibung:** Bereiche abschalten/dämpfen als Simulationseingriff (ANNAHME A6: Kennzeichnung „Simulationseingriff", kein biologischer Befund).
- **Abnahme:** Eingriff durchführbar; Folgen beobachtbar; Kennzeichnung sichtbar.
- **Test:** Unit-Test Eingriff → Simulationsänderung.

### T-083 — KI-Schicht (BYOK) · P3
- **Abhängigkeiten:** T-062 · **Beschreibung:** Dienste-Auswahl (BYOK; Google/Gemini mindestens; weitere **OFFEN**, flylab-spec § 23.9); drei Aufgaben: Erklären (3 Stufen), Experiment-Hilfe, Auswertung; immer „KI-generiert"-markiert; nie Voraussetzung; kein Schlüssel im Quelltext; graceful ohne Schlüssel.
- **Abnahme:** Mit Schlüssel: drei Aufgaben funktionieren; ohne Schlüssel: alles andere läuft unverändert.
- **Test:** Unit-Tests mit Mock-Dienst; Offline-Test (KI aus).

### T-084 — Detailstufe „so tief wie möglich" · P3
- **Abhängigkeiten:** T-074, T-060 · **Beschreibung:** Bis zu einzelnen Zellen/Verbindungen (L3/L4) mit voller Warn/Empfehlungs-Logik.
- **Abnahme:** Stufe wählbar; Schutz greift; auf A56/OnePlus 6T mit Warnung nutzbar.
- **Test:** Performance-Test unter Last.

---

## Langfristig (P4)

### T-090 — Denk-Sandbox (virtuelle Computerwelt)
- **Abhängigkeiten:** T-083 (und Stufe 2) · **Priorität:** P4 (Entscheidung E26: Kernbestandteil, aber nach dem biologischen Kern)
- **Beschreibung:** Abgeschottete virtuelle Welt (virtuelles Dateisystem + eingeschränkter Befehlsdolmetscher); **nie** echter Gerätezugriff; ausdrücklich künstlich/SIMULIERT; klar getrennt von der biologischen Simulation (Projektvertrag § 18–19, 22).
- **Abnahme:** Virtuelle Welt funktional; Isolation nachweisbar (keine echten Dateizugriffe).
- **Test:** Isolations-/Sicherheitstests.

### T-091 — Männliche Tiere/Balz (parametrisiertes Modell) · P4
### T-092 — Schwarm 20+ (Leistung vor Detailtiefe) · P4
### T-093 — Evolution über viele Generationen · P4
### T-094 — Erweiterte Sinne (Feuchtigkeit, Geräusch, Geschmack, Schwerkraft) · P4 (Reihenfolge OFFEN)
### T-095 — Lehre/Forschung-Funktionen · P4
### T-096 — Weitere virtuelle Tiere/allgemeine Systeme (konzeptionell getrennt) · P4
### T-097 — Android-Peripherie · P4
### T-098 — iOS · P4

---

## Abhängigkeitsübersicht (vereinfacht)

```
T-001 → T-002 → T-020 (Sim-Kern) → T-021 → T-022 → T-024 (Verhalten) → T-030 (Kette) → T-040 (Lernen) → T-050 (Experiment) → T-051 (Zeit) → T-052 (Speichern/Vergleich) → T-062 (Endprüfung)
T-001 → T-003 (Theme) → T-012 (Startseite/DE-EN) → T-031 (Erklärsystem)
T-001 → T-010 (3D) → T-011 (Fliege) → T-032 (Gehirn Bereiche) → T-060 (Leistung)
T-052 → T-061 (Tagebuch)
T-062 → T-070 (Erbgut) → T-074 (Beispielzellen)
T-062 → T-072 (Mehrere Fliegen) → T-080 (Fortpflanzung)
T-032 → T-071 (FlyWire)
T-052 → T-073 (Datei-Weitergabe)
T-062 → T-083 (KI)
T-083 → T-090 (Denk-Sandbox)
```

**Kleinster schnell-laufender Pfad („erster bewusst kleiner Meilenstein"):** T-001 → T-002 → T-003 → T-012 → T-010 → T-011 (M0+M1) – die App ist dann stabil, ruhig gestaltet, zweisprachig und zeigt eine interaktive 3D-Fliege.
