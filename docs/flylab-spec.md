# FlyLab – Produktspezifikation (flylab-spec)

**Status:** Arbeitsgrundlage, erstellt aus einem strukturierten Produktinterview (9 Runden) am 16.09.2026.
**Quellen dieses Dokuments:**
- Direkte Antworten des Auftraggebers aus dem Interview (dieses Dokument)
- `CLAUDE.md` (Entwicklungsvertrag – verbindlich)
- `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` (technische Leitplanken für den Bau)
- `freebuff-flylab-interview.md` (Interview-Modus)
- `README.md`, `docs/ARCHITECTURE.md`, `docs/ROADMAP.md` (bestehender Stand)
- Bestehender Code: Android-Projekt mit Kotlin/Compose-Grundgerüst (`app/`, `minSdk 21`, `targetSdk 34`), derzeit nur ein Startbildschirm-Platzhalter

**Sprachregel:** Dieses Dokument ist auf Deutsch verfasst, weil der Auftraggeber deutschsprachig ist. Technische Fachbegriffe sind erlaubt, wo sie für das umsetzende Agenten-Team nötig sind; im Gespräch mit dem Auftraggeber sind sie zu vermeiden (siehe Interview-Regeln in `freebuff-flylab-interview.md`).

---

## 0. Wichtigste Ergebnisse auf einen Blick

| Bereich | Entscheidung |
|---|---|
| Zielgruppe | Zuerst der Auftraggeber selbst (Lernen/Umgehen), dann Laien; sobald die Grundbausteine stehen, auch Lehre und Forschung |
| Erster Start | Ruhige Startseite mit Auswahl: kurze Einführung, geführtes Experiment, direkt zur Fliege |
| App-Sprachen | Deutsch **und** Englisch, umschaltbar, von Anfang an zweisprachig angelegt |
| Haupterlebnis | Verknüpfung von ganzem Tier und Gehirn: die Kette Reiz → Wahrnehmung → Gehirn → Bewegung → Verhalten → Lernen, mal aufs Tier, mal ins Gehirn gezoomt |
| Sinne ab Start | Geruch, Sehen, Berührung/Wind, Temperatur |
| Bedürfnisse | Ja, von Anfang an (z. B. Hunger, Erschöpfung) |
| Gehirn-Detailtiefe | Drei wählbare Stufen (Bereiche / Bereiche + Beispielzellen / so tief wie möglich), mit Überhitzungswarnung und geräteabhängiger Empfehlung |
| Lernen | Kernfunktion (Belohnung, Vermeidung, Gewöhnung, Erinnerung) |
| Wissenschaftliche Kennzeichnung | **Streng immer** – jede Aussage überall gekennzeichnet (gemessen / belegt / abgeleitet / Modell / Vermutung) |
| Erbgut | Fast gleich wichtig wie Gehirn; Ansicht bis auf einzelne Bausteine (Basen) zoombar |
| Experimente | Umwelt verändern; Gerüche/Stoffe; Gehirnbereiche stören; Versuche vergleichen |
| Zeit | Voll zurückspulbar; zusätzlich Zeitlupe **und** Zeitraffer, Pause, Wiederholung |
| Offline | Alles ohne Internet funktionsfähig; KI optional, Dienst frei wählbar (BYOK) |
| KI-Aufgaben | Erklären, Experiment-Hilfe (Vorschläge, Zusammenfassen), Auswertung (Muster finden) |
| Oberfläche | Querformat bevorzugt (Messinstrument-Charakter), Hochformat unterstützt; dunkel/hell umschaltbar |
| Auswahl-Modi (Start, Aussehen, Detailtiefe, Lebenszyklus) | **Jederzeit wechselbar**, auch mitten im Versuch |
| Tagebuch | Automatische Aufzeichnung + eigene Notizen + Bildschirmfotos + Versuchsvergleich |
| Erste Version | Kompakter Kern (siehe § 20); Erbgut-Ansicht und mehrere Fliegen **direkt danach** in kurzen Schritten |
| Geschlecht | Zuerst nur weiblich (passt zu den echten Daten), Geschlechter später |
| Echte Daten | Ja, echte wissenschaftliche Datensätze einbinden (FlyWire, weibliches Gehirn) |
| Weitergabe | Experimente als Datei weitergebbar |
| Überlastung des Geräts | App reduziert Detailtiefe **automatisch** und sagt es dem Nutzer |
| Zielgeräte | Samsung Galaxy A56 **und** OnePlus 6T (ca. 2018) müssen alles laufen haben |
| Denk-Sandbox (virtuelle Computerwelt) | **Kernbestandteil** von FlyLab – architektonisch von Anfang an vorgesehen, zeitlich später |
| Abnahme (Erfolgskriterium) | Experiment-Workflow: anlegen → laufen lassen → speichern → später öffnen → vergleichen |

---

## 1. Grundidee

### 1.1 Was FlyLab ist
FlyLab ist ein wissenschaftlich ehrliches Simulationslabor für die Taufliege *Drosophila melanogaster* auf Android. Der Nutzer beobachtet eine virtuelle Fliege, kann in ihr Nervensystem „hineinzoomen", Reize setzen, Experimente durchführen, lernen beobachten und alles nachvollziehen, was zwischen „Reiz" und „Verhalten" passiert.

### 1.2 Was FlyLab nicht ist
- Kein Spiel mit erfundenen biologischen Fakten.
- Kein „AI-Dashboard": keine Neonoptik, kein Glasteffekt, keine verspielte Assistenz-Optik.
- Keine medizinischen oder menschenbezogenen Aussagen (z. B. keine Behauptung, die Fliege habe menschliche Belohnungsbiologie wie Endorphine).
- Kein Anspruch, ein vollständiges männliches Fliegengehirn darzustellen (die echten Vollständigkeitsdaten stammen von einem weiblichen Tier).

### 1.3 Warum es FlyLab gibt / für wen
- **Phase A (jetzt):** Der Auftraggeber selbst lernt damit umzugehen und die Fliege zu verstehen.
- **Phase B:** Verfügbar für Laien ohne Vorkenntnisse – verständlich erklärt.
- **Phase C (sobald die Grundbausteine stehen):** Lehre (Schule/Hochschule) und Forschende – dann mit höchsten Ansprüchen an Belege und Nachvollziehbarkeit.

### 1.4 Gewünschtes Erlebnis
Ein ruhiges, hochwertiges, wissenschaftliches „Messinstrument" in der Hand: Der Nutzer wählt einen Startpunkt, setzt einen Reiz, schaut der Fliege zu – und kann jederzeit nachfragen: *Was ist da gerade in ihrem Gehirn passiert? Warum hat sie das getan? Was ist dabei wirklich belegt und was ist nur ein Modell?*

### 1.5 Erster Start (Entscheidung)
Ruhige Startseite mit **freier Auswahl**:
1. **Kurze Einführung** (wenige Schritte, zeigt was die App kann),
2. **Geführtes Experiment** (Schritt für Schritt durch die Kausalkette),
3. **Direkt zur Fliege** (ohne Einleitung).
*(ANNAHME, offen markiert: Welches geführte Experiment standardmäßig zuerst angeboten wird – Vorschlag siehe § 23.1.)*

### 1.6 Hauptsprachen der App (Entscheidung)
Deutsch und Englisch, **umschaltbar**, von Anfang an zweisprachig angelegt.
*(ANNAHME: Deutsch ist die Voreinstellung; fachliche Bezeichnungen wie „Mushroom Body" erscheinen zusätzlich zu deutschen Namen.)*

---

## 2. Verbindliche Vorgaben aus dem bestehenden Projektvertrag (CLAUDE.md)

Diese Punkte sind bereits festgeschrieben und werden vom Interview **bestätigt**, nicht ersetzt:

- Android-first; Priorität Samsung-Galaxy-A56- und OnePlus-6T-Klasse.
- Wissenschaftliche Integrität: Jede biologische Aussage trägt eine Kennzeichnung — MEASURED (gemessen), PUBLISHED (belegt), DERIVED (abgeleitet), MODELED (modelliert), HYPOTHESIS (vermutet). Niemals Hypothese als Fakt darstellen.
- Referenzdaten: FlyWire-Vollständigkeits-Datensatz eines adulten **weiblichen** Tiers. Kein erfundenes vollständiges männliches Connectom.
- Trennung der Systeme: Darstellung / Simulation / Daten / Versuchssteuerung / Bedienoberfläche / Speichern. Die Simulation muss ohne die 3D-Darstellung nutzbar sein.
- Erster Meilenstein ist ein „vertikaler Streifen": 3D-Fliege + Kamera + Anatomie-Platzhalter + Gehirnregionen + kleines Connectom-Teilstück + simulierte Nervenaktivität + Aktivitäts-Überlagerung + einfache Verhaltensaufgabe + Belohnung + Versuchsprotokoll — **lauffähig**.
- Leistungsanforderungen: GPU-Nutzung, Detailstufen (LOD), Instanzen-Wiedergabe, asynchrones Laden, Hintergrund-Simulation, zeitunabhängige Simulation, speicherbewusstes Laden des Connectoms, progressive Darstellung; niemals alle ~139k Neuronen gleichzeitig voll darstellen.
- UX: Simulator, kein Verwaltungsbrett; ruhige wissenschaftliche Optik.
- Neuartiges Erweiterungssystem für Neuromodulatoren mit belegten *Drosophila*-Botenstoffen (Dopamin u. a.), **nicht** menschliche Biologie kopieren.

---

## 3. Die virtuelle Fliege

### 3.1 Aussehen (Entscheidung: wählbar)
Drei Darstellungsstile, **jederzeit umschaltbar**:
1. **Stilisiert-schön:** klare, ästhetische Fliege (Körper, Kopf, Beine, Flügel), erkennbar ein Modell.
2. **Realistisch:** möglichst naturgetreu nach echten Referenzbildern von *D. melanogaster*.
3. **Abstrakt-lehrbuchhaft:** wie eine gute Lehrbuch-Zeichnung in 3D – vereinfacht, aber anatomisch korrekt proportioniert.

### 3.2 Geschlecht (Entscheidung)
Zunächst **nur weibliche** Tiere (passt zum echten FlyWire-Datensatz). Männliche Tiere und geschlechtsspezifische Verhaltensweisen kommen später und werden dann klar als parametrisiertes Modell gekennzeichnet, solange keine vollständigen männlichen Strukturdaten vorliegen.

### 3.3 Bedürfnisse und Zustand (Entscheidung: ja, von Anfang an)
Die Fliege hat innere Zustände, die ihr Verhalten beeinflussen. Zum Startumfang gehören mindestens:
- Hunger / Sättigung,
- Erschöpfung / Ruhebedürfnis,
- allgemeiner Erregungszustand (z. B. Stress/Aufregung nach Beinahe-Kollisionen).
*(OFFEN: genaue Liste und Feinabstufung weiterer Zustände – z. B. Durst, Fortpflanzungsbereitschaft erst mit männlichen Tieren.)*

### 3.4 Lebenszyklus (Entscheidung: ja, aber wählbar)
Fliegen sollen altern können, sterben können, sich paaren und Nachkommen haben können – **aber der Nutzer wählt selbst aus den Optionen**, ob und welche Teile des Lebenszyklus in einem Versuch aktiv sind (z. B. „unsterblich für Verhaltensexperimente" oder „drei Generationen mit Vererbung").
*(OFFEN: genau welche Umschalter es gibt; ob Eilarven-Stadien dargestellt werden oder Erst-Generationen bei ausgewählter Fortpflanzung mit Adulttieren starten.)*

### 3.5 Bewegung und Verhalten
Grundverhaltensrepertoire (aus dem Projektvertrag, vom Interview bestätigt): laufen, drehen/orientieren, sich nähern, meiden/fliehen, ruhen, Nahrung suchen und aufnehmen, erkunden.
*(OFFEN: weitere Verhaltensweisen wie Putzen, Springen, Flügelschnellen – zu klären, sobald die Grundkette steht. Balz/Paarung erst mit männlichen Tieren relevant.)*

---

## 4. Sinneswahrnehmung (Entscheidung: vier Sinne ab Start)

| Sinn | Was die Fliege wahrnimmt | Erwartete Wirkung in der Simulation |
|---|---|---|
| **Geruch** | Geruchsstoffe in der Luft, örtlich und konzentrationsabhängig | Wichtigster Sinn; ausgebauteste Gehirnleitung; Anziehung/Vermeidung; Lern-Grundlage |
| **Sehen** | Licht, Helligkeit, Bewegung (z. B. plötzliche Schatten), Tageszeit | Orientierung, Schreckreaktion, Tag/Nacht-Rhythmus |
| **Berührung / Wind** | Körperberührung, Luftzug/Vibration von außen | Flucht, Umorientierung, Stillhalten |
| **Temperatur** | Wärme/Kälte, örtliche Unterschiede | Aufsuchen/Mieden von Zonen, Stress bei Extremen |

- Reize wirken räumlich und stärkeabhängig (Konzentration/Intensität), nicht als einfaches An/Aus.
- Die Wahrnehmung muss in der Kausalkette (§ 6) sichtbar werden: Welcher Sinn hat ausgelöst, wie stark?
- *(OFFEN: Weitere Sinne für später – Feuchtigkeit, Geräusche/Vibrationen als eigene Kategorie, Geschmack/Nahrungsprüfung, Schwerkraft. Reihenfolge nicht entschieden.)*

---

## 5. Nervensystem und Gehirn

### 5.1 Struktur
Hierarchisch: Gehirn → Gehirnbereich → Teilbereich → Zelltyp → einzelne Nervenzelle → Verbindungen (Kontaktstellen). Jede Ebene trägt: Name (deutsch + Fachbezeichnung), Beschreibung, Funktion, Beleg-Level, Datenquelle, Version.

### 5.2 Detailtiefe (Entscheidung: drei wählbare Stufen)
1. **Gehirnbereiche:** ca. 50 benannte Bereiche (Geruchszentrum, Sehzentren, Lernzentrum „Pilzkörper" usw.) mit Aktivität und Leitungen dazwischen.
2. **Bereiche + Beispielzellen:** zusätzlich ausgewählte Nervenzellen und Bahnen, die man einzeln anschauen kann.
3. **So tief wie möglich:** bis zu einzelnen Zellen und Verbindungen – rechenintensiv, dafür vollständiger.

**Schutz vor Überlastung (Entscheidung):**
- Bei Stufe 3 (und bei sehr hoher Last allgemein) zeigt die App eine **Warnung zur Überhitzungsgefahr**.
- Je nach Gerätemodell erhält der Nutzer eine **Empfehlung**, welche Stufe für sein Gerät am besten geeignet ist.
- Bei Überlastung reduziert die App die Detailtiefe **automatisch** und teilt das mit (Entscheidung aus Interviewrunde 9; keine Rückfrage, aber sichtbarer Hinweis).

### 5.3 Echte Daten (Entscheidung: ja)
Echte wissenschaftliche Datensätze werden eingebunden – primär das FlyWire-Verschaltungsbild eines adulten weiblichen Tiers – auch wenn das eine eigene, aufwendige Aufgabe ist. Wenn ein Datensatz (noch) nicht verfügbar ist, darf ein klar als „Übungsdaten" gekennzeichneter Ersatz stehen, bis die echten Daten integriert sind. Referenzdaten werden nie verändert; Simulationsschichten liegen darüber (Prinzip aus dem Projektvertrag).

### 5.4 Was der Nutzer im Gehirn tun kann
- Drehen, zoomen, Bereiche antippen, aus-/einblenden, einzelne Bereiche isolieren.
- Aktivität live sehen (helle/leuchtende Bereiche, Pulsschlag, wandernde Signale – genaue Bildsprache später).
- Leitungen/Bahnen zwischen Bereichen verfolgen, Richtungen und Zeitablauf sehen.
- Verbindungen sich verändern sehen, wenn gelernt wird (§ 7).
- Bei allem: sofortige Erklärung + Beleg-Kennzeichnung (§ 9).

---

## 6. Kausalkette – Kernprinzip (Entscheidung: Gesamtkette sichtbar inklusive Details)

Kette: **Auslöser → Wahrnehmung → Aktivität im Nervensystem → Verarbeitung → Bewegung → Verhalten → Ergebnis → Lernen**

Anforderungen:
- Die **ganze Kette** ist als Übersicht sichtbar.
- **Jeder Schritt ist anklickbar** und hat eine Erklärung (in drei Stufen, § 9).
- Bei jedem Schritt kann man **ins Detail gehen**: welche Gehirnbereiche beteiligt sind, wie stark sie aktiv sind, welche Leitung das Signal weitergibt.
- Die Kette funktioniert in beide Blickrichtungen: auf das ganze Tier gezoomt (Körper bewegt sich) und ins Gehirn gezoomt (Signale wandern).
- Jede Verknüpfung der Kette ist ein echtes Simulationsereignis – Protokoll-Einträge (§ 12) werden aus der Simulation erzeugt, nie erfunden.

---

## 7. Lernen und Gedächtnis (Entscheidung: Kernfunktion)

- **Belohnung:** positive Folgen (Nahrung) stärken die Neigung zu dem Verhalten, das davor führte.
- **Vermeidung/Bestrafungeffekt:** negative Folgen (z. B. schlechter Geruch, Erschrecken) schwächen die Neigung.
- **Gewöhnung:** wiederholte harmlose Reize führen zu schwächerer Reaktion.
- **Erinnerung:** Erlebtes wirkt auf späteres Verhalten; Erinnerungen haben Stärke (kann nachlassen) und Zeitpunkt.
- Der Lernvorgang ist **beobachtbar**: Verbindungen im Gehirn verändern sich sichtbar, mit Vorher/Nachher-Werten und Begründung („warum hat sich das geändert?"), klar gekennzeichnet als Modell (Projektvertrag: Änderungsregel ist konfigurierbar und als SIMULIERT/MODELLIERT zu markieren).
- Kurzzeit- und längerwirkende Gedächtnisanteile sind getrennt vorhanden.
- *(OFFEN: genaue Lernaufgaben/Gewohnheitstypen für die erste Version; Vorschlag siehe § 23.)*

---

## 8. Umgebung (Entscheidung: vom Nutzer veränderbar)

Vom Nutzer veränderbare Größen (Kern der Experimentart „Umwelt verändern"):
- Temperatur (örtlich und global),
- Licht (Helligkeit, Farbe?, Tag/Nacht-Zyklus und Tageszeit),
- Feuchtigkeit,
- Gerüche (Art, Ort, Konzentration),
- chemische Stoffe (Art, Ort, Konzentration, Dauer).

Weitere Umgebungselemente, die existieren sollen: Nahrung, Wasser, Hindernisse, räumliche Bereiche, andere Tiere (später mit Mehr-Fliegen), zeitliche Veränderungen.
*(OFFEN: Hindernis-Editor, Wetter, Geräuschquellen als Nutzerwerkzeuge – nicht entschieden; Nahrung/Wasser als Platzierungsobjekte gelten als selbstverständlich vorhanden, werden hier dokumentiert.)*

Umweltgrößen werden als stetige Werte geführt (kein bloßes An/Aus) – Projektvertrag.

---

## 9. Erklärungen und wissenschaftliche Kennzeichnung

### 9.1 Erklärstufen (Entscheidung: drei Stufen, umschaltbar)
1. **Ganz einfach** (Laien-Sprache, ohne Fachbegriffe),
2. **Normal** (gut verständlich, Fachbegriffe mit Erklärung),
3. **Ausführlich-wissenschaftlich** (Details, Quellenbezug, Grenzen der Aussage).

### 9.2 Belege (Entscheidung: streng immer)
**Jede** biologische Aussage trägt **immer und überall** eine Kennzeichnung, mindestens:
- **gemessen / beobachtet** (MEASURED),
- **belegt** (PUBLISHED – wissenschaftlich gut belegt, z. B. aus Literatur/Referenzdaten),
- **abgeleitet** (DERIVED – aus anderen Daten berechnet),
- **Modell** (MODELED/SIMULIERT – vereinfachtes Rechenmodell),
- **Vermutung** (HYPOTHESIS).
Zusätzlich muss auf Nachfrage (Antippen) erkennbar sein: *Was ist bekannt? Was ist ein Modell? Wie sicher ist die Aussage?* Erklärungen behaupten nie pauschal „Dieses Gebiet macht X", ohne den Beleggrad zu nennen. KI-generierte Aussagen werden zusätzlich als „KI-generiert" markiert und werden niemals stillschweigend zu „Fakten".

---

## 10. Erbgut (Entscheidung: fast gleich wichtig, bis auf Bausteine)

- Die Erbgut-Ansicht wird **fast parallel** zum Gehirn entwickelt und gehört von Anfang an zur Produktgeschichte.
- Von Anfang an **bis auf die Ebene einzelner Bausteine (Basen)** zoombar: Gesamterbgut → Abschnitte/Chromosomen → Gene → Basen.
- Der Nutzer kann: darin blättern, Bereiche wählen, Gene anschauen, Basen ansehen, **Veränderungen vornehmen** (Bearbeitungen/Mutationen), zwei Erbgüter vergleichen, Auswirkungen untersuchen.
- Kernfragen der Ansicht: Was ist im Erbgut vorhanden? Was unterscheidet zwei Fliegen? Was könnte diese Unterschiede verursachen? **Was davon ist sicher bekannt – was ist nur ein Modell?**
- Niemals einem willkürlichen Eingriff eine erfundene biologische Wirkung zuschreiben (Projektvertrag). Unbekannte Wirkungen bleiben offen gekennzeichnet.
- *(OFFEN: konkrete Umsetzung der Gen-Wirkung auf Körper/Verhalten/Gehirn – welche Gene zuerst, wie Vererbungsregeln im Detail; Vorschlag siehe § 23.)*

---

## 11. Fortpflanzung, Populationen, Evolution

- **Geschlechter:** erst weiblich (§ 3.2); männliche Tiere später, dann mit Balz/Paarung.
- **Mehrere Fliegen:** später, **eine Handvoll (ca. 5–10)** gleichzeitig – erst, wenn das Kausalketten-Erlebnis steht (Kompakt-Kern, siehe § 20).
- **Gruppenverhalten** (Konkurrenz, Zusammenarbeit, soziale Beziehungen) und **Großgruppen (20+)** sind langfristige Ziele (Leistung geht dann vor Detailtiefe).
- **Populationen/Generationen:** mehrere Generationen mit Vererbung als **wählbare Simulationsoption** (§ 3.4).
- **Evolution:** Unterschiede zwischen Tieren, zufällige Veränderungen, Vererbung, Umweltbedingungen, Fortpflanzungserfolg → Veränderung über Generationen. Immer als **vereinfachtes Modell** gekennzeichnet.
- Jede Fliege besitzt unabhängig: Erbgut, Geschlecht, Körperzustand, Nervenzustand, Erinnerung, Lernstand, Verhaltenszustand (Projektvertrag). Kommunikation zwischen Fliegen nur über ausdrücklich modellierte Kanäle.

---

## 12. Experimente (Entscheidung: vier Kernarten + Workflow)

### 12.1 Kernexperimente (alle vier bestätigt)
1. **Umwelt verändern:** Temperatur, Licht, Feuchtigkeit, Tageszeit ändern und Reaktion beobachten.
2. **Gerüche/Stoffe:** Gerüche und Stoffe platzieren, Konzentration und Dauer einstellen, Reaktionen beobachten.
3. **Gehirnbereiche stören:** Bereiche abschalten oder dämpfen (nach dem Vorbild echter Experimente) und Folgen beobachten – Ergebnis klar als Simulationsmaß gekennzeichnet.
4. **Versuche vergleichen:** zwei oder mehr Versuche mit verschiedenen Bedingungen nebeneinander vergleichen.

### 12.2 Experiment-Workflow (Abnahme-Kriterium, Entscheidung)
Anlegen → laufen lassen → **speichern** → später wieder öffnen → **vergleichen**.

### 12.3 Zeit und Wiederholung (Entscheidung)
- **Voll zurückspulbar:** jederzeit zurückspulen, Schritt für Schritt ansehen und **an einem Punkt weiterlaufen lassen**.
- Pause, Wiederholung von vorn, Schritt-Modus.
- **Zeitlupe und Zeitraffer**, beide frei einstellbar (z. B. Signalwanderung im Gehirn verlangsamen; lange Versuche beschleunigen).
- Gleicher Startzustand + gleiche Zufallsgrundlage + gleiche Modellversion = gleiches Ergebnis (Nachvollziehbarkeit, Projektvertrag).

### 12.4 Weitergabe (Entscheidung)
Experimente können **als Datei weitergegeben** werden (z. B. an Lehrkräfte oder Freunde), inklusive genug Information, um den Versuch nachzustellen.

*(OFFEN: maximale Anzahl gleichzeitig gespeicherter Versuche/Länge der Rückspul-Historie pro Versuch – von Gerätegrenzen abhängig zu machen; ANNAHME: unbegrenzt innerhalb eines Versuchs, solange der Gerätespeicher reicht, mit Speicherplatz-Anzeige.)*

---

## 13. Stoffe und chemische Einflüsse

- Stoffe werden mit Art, Konzentration/Stärke, Dauer und Aufnahmeort simuliert.
- Beobachtbar: Auswirkung auf Verhalten, Nervensystem, Erholung; Unterschiede zwischen Tieren (später mit mehreren Tieren).
- **Keine medizinischen Aussagen erfinden.** Fehlen wissenschaftliche Daten, wird der Effekt als **Modell** oder **offene Frage** gekennzeichnet (Deckungsgleich mit Projektvertrag: Wirkmodelle tragen Beleggrade „gemessen/aus Literatur abgeleitet/erschlossen/vermutet").
- Neuromodulation läuft über das erweiterbare Botenstoff-System des Projektvertrags (belegte *Drosophila*-Botenstoffe wie Dopamin).

---

## 14. 3D-Darstellung

- Fliege in 3D: drehen, zoomen; Körperteile antippen; innere Strukturen einblenden; Nervensystem einblenden; Gehirn einblenden (eigener Ansichtsmodus); aktive Bereiche hervorheben; Bahnen verfolgen.
- Umwelt wird im selben Raum dargestellt (Reizquellen sichtbar).
- Zeitsteuerung als Dauerbedienung: Wiedergabe, Pause, Zurückspulen, einzelne Momente untersuchen, Zeitlupe/Zeitraffer (§ 12.3).
- Zwei Blickebenen jederzeit nutzbar: **ganzes Tier** und **Gehirn** (§ 1.4, § 6).
- Mobiltaugliche Umsetzung: Detailstufen, nur sichtbare Teile zeichnen, ausgewählte Inhalte laden – keine volle Komplexität aller ~139k Neuronen gleichzeitig (Projektvertrag).

---

## 15. Versuchstagebuch (Entscheidung: alle vier Bestandteile)

1. **Automatische Aufzeichnung:** jeder Versuch wird automatisch mit Ablauf, Beobachtungen und Ereignissen aufgezeichnet (echte Simulationsereignisse, keine erfundenen Protokolle).
2. **Eigene Notizen:** zu jedem Versuch schreibbar.
3. **Bildschirmfotos:** aus der laufenden Simulation ins Tagebuch speichern.
4. **Vergleiche:** frühere Versuche nebeneinanderlegen und gegenüberstellen; Zeitverläufe ansehen; Wiederholungen führen.

Bestandteile eines Versuchseintrags (aus dem Projektvertrag): Frage/Ausgangslage, Einstellungen, Zufallsgrundlage, Datensatz- und Modellversion, Reize, Beobachtungen, Lern-/Verbindungsänderungen, Verhaltensänderungen, Ergebnis, Unsicherheit.

---

## 16. Benutzeroberfläche

### 16.1 Grundstimmung (Entscheidung)
- **Beide Stimmungen: dunkel und hell, umschaltbar**; Voreinstellung folgt der Systemeinstellung des Handys.
- Dunkel = „Messinstrument im Labor" (Aktivitätssignale heben sich gut ab); hell = kliner Labor-Look.
- Ruhig, hochwertig, wissenschaftlich, modern. **Kein Neon, kein Glasteffekt, keine „AI-Dashboard"-Optik.**

### 16.2 Ausrichtung (Entscheidung)
- **Querformat bevorzugt** (Messinstrument-Charakter, mehr Platz für 3D-Ansicht plus Bedienelemente daneben).
- Hochformat wird trotzdem unterstützt (kein Zwangs-Rotationssperren-Frust).

### 16.3 Aufbau
- **Immer sichtbar:** die 3D-Hauptszene plus minimaler Overlay (Zeitsteuerung, Aktivität ein/aus, Verbindungen ein/aus, Detailtiefe).
- **Über Menü/Panel:** ausgewähltes Objekt (Name, Funktion, Belege, Aktivität, Verbindungen, Geschichte), Tagebuch, Einstellungen, Erklärstufen, Sprachwahl, Darstellungsstil.
- **Nur bei Bedarf:** vollständige Erklärtexte, Vergleiche, Details zu einzelnen Zellen.
- Alles Wichtige ist in Querformat mit einer Hand an den unteren Rändern erreichbar; nichts Verdeckendes über der Szene.

---

## 17. Einstellbarkeit – alles jederzeit wechselbar (Entscheidung)

Diese Wahlen sind **jederzeit im Lauf der Nutzung** erreichbar und wechselbar – auch mitten im Versuch:
- Startverhalten (Einführung / geführtes Experiment / direkt zur Fliege),
- Darstellungsstil der Fliege (§ 3.1),
- Gehirn-Detailtiefe (§ 5.2),
- Lebenszyklus-Optionen (§ 3.4),
- Erklärstufe (§ 9.1), Sprachwahl, Lichtstimmung (§ 16.1).

*(ANNAHME: Ein Wechsel der Detailtiefe mitten im Versuch verändert die Beobachtungsgenauigkeit, aber nicht das Versuchsergebnis; die Simulation läuft in voller Genauigkeit weiter, nur die Ansicht ändert sich.)*

---

## 18. Leistung, Geräte, Überlastung

- **Zielgeräte (Entscheidung):** Samsung Galaxy A56 **und** OnePlus 6T (ca. 2018) müssen alles lauffähig haben – ggf. mit automatisch reduzierten Details.
- Start des Simulationsteils muss auf OnePlus-6T-Klasse flüssig bleiben; schwere Detailstufen sind auf schwächeren Geräten verfügbar, aber mit Warnung/Empfehlung (§ 5.2).
- **Automatische Reduktion bei Überlastung** (Entscheidung): senkt Detailtiefe eigenständig, zeigt an, was reduziert wurde; Nutzer kann es danach manuell zurückstellen.
- Überhitzungsschutz: Warnung bei langer hoher Last (§ 5.2).
- Offline-Fähigkeit (§ 19) ohne Abstriche bei der Simulation.

---

## 19. Offline und KI

### 19.1 Offline (Entscheidung)
**Alles läuft ohne Internet:** Simulation, alle Daten (Gehirn, Erbgut), Experimente, Tagebuch, Vergleich, Datei-Export. Internet wird nur für freiwillige KI-Funktionen benötigt.

### 19.2 KI (Entscheidung: aktiv einbinden, Dienst frei wählbar)
- Der Nutzer **wählt selbst den KI-Dienst** (eigener Schlüssel, „BYOK"; zumindest Google/Gemini wegen vorhandenem Abo, weitere Dienste möglich).
- **KI-Aufgaben (Entscheidung):**
  1. **Erklären** – verständliche Erklärungen, Fragen beantworten (in den drei Erklärstufen).
  2. **Experiment-Hilfe** – Vorschläge, was man als Nächstes testen könnte; Ergebnisse zusammenfassen.
  3. **Auswertung** – Versuche und Versuchsreihen auswerten, Muster finden.
- **Trennung:** KI ist nie Voraussetzung für die Simulation; KI-Ausgaben sind immer als „KI-generiert" markiert und werden nie stillschweigend zu wissenschaftlichen Fakten. Kein Schlüssel darf im Programmtext stehen.

---

## 20. Erste Version und Reihenfolge (inkl. Widerspruchsauflösung)

### 20.1 Der Widerspruch (dokumentiert)
- Interviewrunde 8: „Ganz groß" – kompakter Kern **plus** Erbgut **plus** mehrere Fliegen von Anfang an.
- Interviewrunde 4: „Schnell etwas Lauffähiges".
- Interviewrunde 9 (**Auflösung**): **Schnell gewinnt.**

### 20.2 Geltende Reihenfolge
1. **Stufe 1 – Kompakter Kern (schnell lauffähig):** Fliege + Gehirn (Bereiche) + Reaktion + **Lernen** + Experiment speichern; dazu die Kausalkette sichtbar, echte Daten soweit nötig (falls ein Datensatz noch nicht integriert ist: klar gekennzeichneter Übungsdatensatz als Übergang), Zeitsteuerung mit Zurückspulen, Beleg-Kennzeichnung „streng immer", deutsch/englisch, dunkel/hell, Querformat.
2. **Stufe 2 – direkt danach in kurzen Schritten:** Erbgut-Ansicht (bis Basen-Ebene, § 10) und mehrere Fliegen (Handvoll, § 11).
3. **Danach:** Fortpflanzung/Generationen als Option, Stoffe vertieft, Auswertung/KI-Funktionen ausgebaut, Denk-Sandbox, Lehr-/Forschungs-Funktionen.

Die Roadmap in `docs/ROADMAP.md` bleibt strukturell gültig (Phasen 0–14), wird aber durch die o. g. Priorisierung des Auftraggebers konkretisiert.

---

## 21. Denk-Sandbox (virtuelle Computerwelt) – Entscheidung: Kernbestandteil

- Die Idee, dass die Fliege (langfristig) in einer **virtuellen, abgeschotteten Computerwelt** Dateien anlegen, kleine Programme ausführen und Aufgaben lösen kann, ist für den Auftraggeber ein **fester Bestandteil von FlyLab** und wird architektonisch von Anfang an mitgedacht.
- **Kein unrealistischer Biologieanspruch:** Das ist ausdrücklich ein künstliches/erfundenes Fähigkeitsfeld, klar getrennt von der biologischen Simulation und gekennzeichnet (entspricht „FLY COGNITIVE SANDBOX" im autonomen Projektvertrag).
- Zeitlich kommt es nach dem biologischen Kern (Stufe 3+, siehe § 20.2).
- Sicherheitsgrundsätze aus dem Projektvertrag gelten: nur abgeschlossene virtuelle Welt, keine echten Gerätdateien, keine unbegrenzten Berechtigungen.

---

## 22. Langfristige Ideen (bestätigt als Fernziel, nicht Teil der ersten Stufen)

- Ausbau auf Lehre und Forschung, sobald die Grundbausteine stehen (§ 1.3).
- Großgruppen/Schwarm (20+ Fliegen) mit Leistung vor Detailtiefe.
- Evolution über viele Generationen mit Vergleichen zwischen Populationen.
- Erweiterte Sinnwelt (Feuchtigkeit, Geräusche, Geschmack) und weitere Verhaltensweisen.
- Zusätzliche virtuelle Tiere/allgemeine simulierte Systeme (Aufgabe-Umgebungen wie Wegsuche, Zeichnen, usw.) – **konzeptionell getrennt** von der biologischen Simulation (Projektvertrag).
- Später: iOS/Weiters; Android-Geräte-Zubehör/Peripherie (Fernziel aus Projektvertrag).

---

## 23. Offene Punkte (OFFEN – nicht erfinden!)

1. Welches geführte Experiment als erstes standardmäßig angeboten wird (Vorschlag: „Ein Geruch – und wie die Fliege ihn lernt", siehe Annahmen A2).
2. Genaue Liste der Bedürfniszustände über Hunger/Erschöpfung/Erregung hinaus.
3. Genaue Umschalter des Lebenszyklus (Altern an/aus, Tod an/aus, Paarung an/aus, Generationen-Zahl).
4. Zusätzliche Verhaltensweisen für die erste Version (Putzen, Springen …).
5. Reihenfolge späterer Sinne (Feuchtigkeit, Geräusch, Geschmack, Schwerkraft).
6. Wie genau Gen-Veränderungen auf Körper/Verhalten/Gehirn wirken und welche Gene zuerst dargestellt werden.
7. Begrenzungen für Rückspul-Historie/Speicherplatz pro Gerät.
8. Umfang und Mindestinhalt der Datei zum Weitergeben von Experimenten (Nutzung auf anderen Geräten).
9. Konkrete KI-Dienste neben Google/Gemini und deren Einbindungsreihenfolge.
10. Hindernisse, Wasserquellen, andere Tiere als frei platzierbare Objekte: welche davon in welcher Stufe.
11. Genauer Bildsprache der Aktivitätsdarstellung (Farben, Pulsformen) – Gestaltungsentscheidung.
12. Umbenennung/Feinschnitt der drei Fliegen-Darstellungsstile, sobald erste Entwürfe vorliegen.

## 24. Annahmen (ANNAHME – dem Auftraggeber vorgeschlagen, nicht von ihm entschieden)

1. **A1:** Deutsch ist die Voreinstellung der App-Sprache (umschaltbar auf Englisch); Fachbegriffe erscheinen zusätzlich in Fachsprache.
2. **A2:** Das geführte Eröffnungsexperiment zeigt die vollständige Kausalkette am Beispiel Geruch → Gehirn → Bewegung → Belohnung → Lernen.
3. **A3:** Detailtiefe ist eine Ansichtseigenschaft: Wechsel mitten im Versuch ändert nur die Anzeige, nicht die laufende Simulation.
4. **A4:** Rückspulen ist innerhalb eines laufenden Versuchs unbegrenzt, begrenzt nur durch Gerätespeicher; der Versuch behält die vollständige Aufzeichnung im Tagebuch.
5. **A5:** Bei automatischer Reduktion wird nach Ende der Lastphase nicht automatisch zurück auf hohe Detailtiefe geschaltet; der Nutzer stellt selbst zurück.
6. **A6:** „Gehirnbereiche stören" wird als Simulationswerkzeug mit eigener Kennzeichnung („Simulationseingriff") geführt, nicht als biologisches Ergebnis.
7. **A7:** Für die Basen-Ebene des Erbguts wird zunächst der bekannte Referenzdatensatz des weiblichen Tiers genutzt; individuelle Abweichungen entstehen durch Simulations-Eingriffe/Vererbung und werden als modelliert gekennzeichnet.

## 25. Widersprüche und ihre Auflösung

| Widerspruch | Auflösung (Entscheidung des Auftraggebers) |
|---|---|
| „Schnell etwas Lauffähiges" (Runde 4) vs. „Ganz groß von Anfang an" (Runde 8) | **Schnell gewinnt** (Runde 9): kompakter Kern zuerst; Erbgut-Ansicht und mehrere Fliegen direkt danach in kurzen Schritten. |
| „Alles offline" vs. „KI aktiv einbinden" | Kein echter Widerspruch nach Klärung: Grundfunktionen komplett offline; KI optional, Dienst frei wählbar. |
| „Streng immer Belege" vs. ruhige Optik | Kennzeichnung ist dauerhaft, aber dezente visuelle Markierung; vollständige Erklärungen erscheinen auf Nachfrage/Antippen. |

## 26. Wissenschaftliche Unsicherheiten und Grenzen (bewusst dokumentiert)

- Vollständige Connectom-Daten existieren nur für ein **weibliches** Tier; männliche Simulation bleibt parametrisiertes Modell, bis belastbare Daten vorliegen.
- Lernregeln, Verhaltensauswahl und Gen-Wirkungen sind **Modelle**, auch wenn sie durch echte Daten inspiriert sind.
- Stoffwirkungen ohne belastbare Datenlage bleiben als Modell/offene Frage gekennzeichnet.
- Die Denk-Sandbox ist eine künstliche Fähigkeitswelt, **keine** Fliegen-Biologie.
- Die App macht keine Aussagen über menschliche Biologie.

## 27. Abnahmekriterien (Erfolg, aus Sicht des Auftraggebers)

**Hauptkriterium (Entscheidung):** Der Experiment-Workflow funktioniert vollständig:
1. Experiment anlegen (Umwelt/Gerüche/Stoffe/Gehirnbereiche ändern),
2. laufen lassen (mit Pause, Zeitlupe, Zeitraffer),
3. speichern,
4. später wieder öffnen,
5. vergleichen (mind. zwei Versuche nebeneinander).

**Dazu muss in Stufe 1 erfüllt sein:**
- Die Kausalkette ist sichtbar, klickbar, in drei Erklärstufen erklärbar, mit dauerhafter Beleg-Kennzeichnung.
- Lernen ist beobachtbar (Verhalten ändert sich, Gehirn-Verbindungen zeigen Vorher/Nachher).
- Rückspulen bis zu einem Punkt und Weiterlaufen ab dort.
- Alles funktioniert offline; deutsch/englisch umschaltbar; dunkel/hell umschaltbar; Querformat vorrangig.
- Auf Galaxy A56 **und** OnePlus 6T nutzbar; automatische Detailreduktion greift, wenn das Gerät überlastet ist.
- Auf OnePlus-6T-Klasse keine Dauerüberhitzungswarnung bei normaler Nutzung der Standard-Detailtiefe.

**Stufe 2 (direkt danach):** Erbgut-Ansicht bis Basen-Ebene nutzbar; ca. 5–10 Fliegen gleichzeitig.

## 28. Konkrete nächste Arbeitsschritte für das umsetzende Agenten-Team (aus den bestätigten Anforderungen abgeleitet)

1. **Bestandsaufnahme & Fundament:** bestehendes Android-Projekt prüfen; Trennung Simulation/Daten/Darstellung/Bedienung/Speichern anlegen (Projektvertrag); leeres Experiment- und Tagebuch-Grundgerüst.
2. **3D-Fliege (Stufe 1):** eine der drei Darstellungsarten zuerst umsetzen (ANNAHME A8: Start mit „Abstrakt-lehrbuchhaft", weil schnell und wissenschaftlich solide; die anderen folgen), drehen/zoomen/antippen.
3. **Gehirn-Ansicht Stufe „Bereiche":** ca. 50 benannte Bereiche mit Aktivität; Beleg-Kennzeichnung überall; Warnung/Empfehlung zur Detailtiefe.
4. **Sinne + Kausalkette:** Geruch, Sehen, Berührung/Wind, Temperatur als räumliche, stärkeabhängige Reize; Kette von Reiz bis Verhalten sichtbar/klickbar.
5. **Lernen + Gedächtnis:** Belohnung/Vermeidung/Gewöhnung mit sichtbarer Verbindungsänderung (Vorher/Nachher).
6. **Experiment-Engine:** anlegen/ausführen/speichern/öffnen/vergleichen; deterministische Wiederholung; voll zurückspulbar; Zeitlupe/Zeitraffer.
7. **Tagebuch:** automatische Aufzeichnung, Notizen, Bildschirmfotos, Vergleich.
8. **Oberfläche:** Startseite mit drei Wahlen; Querformat; dunkel/hell; deutsch/englisch; alles jederzeit wechselbar.
9. **Leistungssicherung:** OnePlus-6T-Ziel, automatische Reduktion, Überhitzungswarnung, Geräte-Empfehlung.
10. **Stufe 2:** Erbgut-Ansicht (Basen-Ebene), danach mehrere Fliegen (5–10), danach Fortpflanzung/Generationen als Option.
11. **KI-Schicht:** optional, Dienst wählbar (BYOK), Aufgaben Erklären/Experiment-Hilfe/Auswertung, immer als KI-generiert markiert.
12. **Denk-Sandbox:** architektonisch vorbereitet (abgeschottete virtuelle Welt), Umsetzung nach Stufe 2.

*(A8 ist eine Annahme des umsetzenden Teams, keine Entscheidung des Auftraggebers – bei Bedarf widersprechen.)*

---

## 29. Verhältnis zu den übrigen geplanten Dokumenten

Der Interview-Auftrag sieht eine Familie weiterer Dokumente vor (u. a. `docs/PRD.md`, `docs/NARRATIVE_PRODUCT_SPEC.md`, `docs/PROJECT_STATE.md`, `docs/SCIENTIFIC_MODEL.md`, `docs/ARCHITECTURE_REQUIREMENTS.md`, `docs/UX_SPEC.md`, `docs/DATA_MODEL.md`, `docs/SIMULATION_MODEL.md`, `docs/GENETICS_MODEL.md`, `docs/EXPERIMENT_MODEL.md`, `docs/AI_MODEL.md`, `docs/DECISIONS.md`, `docs/MILESTONES.md`, `docs/TASK_GRAPH.md`, `docs/VALIDATION.md`, `docs/KNOWN_LIMITATIONS.md`, `docs/RESEARCH_QUESTIONS.md`, `docs/CLAUDE_CODE_HANDOFF.md`). **Diese Spezifikation (flylab-spec.md) ist die einzige Quelle der Wahrheit für alle Produktentscheidungen**; die genannten Dokumente können aus ihr abgeleitet werden, sobald der Auftraggeber dem Inhalt zustimmt. Die wichtigste Ableitung ist `docs/CLAUDE_CODE_HANDOFF.md` (Übergabe an den umsetzenden Agenten-Lauf).
