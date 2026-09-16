# FlyLab – Research Questions (offene Punkte und Forschungsfragen)

**Quelle:** `docs/flylab-spec.md` § 23 (12 offene Punkte), § 26
**Regel:** Das ist die **einzige** Verwaltungsstelle für „OFFEN". Nichts wird erfunden, nur damit Dokumente vollständig wirken. Jeder offene Punkt wird entweder später entschieden (dann: DECISIONS.md ergänzen) oder bleibt offen.

---

## A. Die 12 offenen Produktfragen (aus dem Interview, unverändert)

| # | Frage | Kontext/Vorschlag | Betrifft Tasks |
|---|---|---|---|
| 1 | Welches geführte Experiment wird standardmäßig zuerst angeboten? | Vorschlag (Annahme A2): „Ein Geruch – und wie die Fliege ihn lernt" | T-012 |
| 2 | Genaue Liste der Bedürfniszustände über Hunger/Erschöpfung/Erregung hinaus (z. B. Durst, Fortpflanzungsbereitschaft erst mit männlichen Tieren) | Start mit den drei genannten | T-023 |
| 3 | Genaue Lebenszyklus-Umschalter (Altern an/aus, Tod an/aus, Paarung an/aus, Generationen-Zahl); ob Larven-Stadien dargestellt werden | Fortpflanzung als Option (E27) | T-080 |
| 4 | Zusätzliche Verhaltensweisen für Version 1 (Putzen, Springen, Flügelschnellen …) | Erst Grundrepertoire | T-024 |
| 5 | Reihenfolge späterer Sinne (Feuchtigkeit, Geräusch, Geschmack, Schwerkraft) | Vier Sinne stehen zuerst | T-094 |
| 6 | Wie Gen-Veränderungen auf Körper/Verhalten/Gehirn wirken; welche Gene zuerst dargestellt werden | Standard: MODELLIERT, BELEGT nur einzeln belegt | T-070 |
| 7 | Begrenzungen für Rückspul-Historie/Speicherplatz pro Gerät | ANNAHME A4 als Startpunkt: unbegrenzt innerhalb Versuchs, Speicherplatz-Anzeige | T-051 |
| 8 | Umfang/Mindestinhalt der Experiment-Weitergabedatei (Nutzung auf anderen Geräten) | Start: „genug zur Nachstellung" | T-073 |
| 9 | Konkrete KI-Dienste neben Google/Gemini und deren Einbindungsreihenfolge | BYOK fest; Google/Gemini mindestens | T-083 |
| 10 | Hindernisse, Wasserquellen, andere Tiere als frei platzierbare Objekte: welche davon in welcher Stufe | Nahrung/Wasser gelten als vorhanden | T-021 |
| 11 | Bildsprache der Aktivitätsdarstellung (Farben, Pulsformen) | Gestaltungsentscheidung; ruhig, kein Neon | T-032, T-041 |
| 12 | Umbenennung/Feinschnitt der drei Darstellungsstile nach ersten Entwürfen | Arbeitsnamen stehen | T-011 |

## B. Wissenschaftliche Forschungsfragen (für wissenschaftliche Kennzeichnung, nicht für Produktentscheidungen)

1. **Lernregel-Kalibrierung:** Welche konkrete Lernregel und Parameter bilden belegte Geruch-Belohnungs-Lernphänomene in angemessener Weise nach? (Bleibt MODELLIERT/SIMULIERT.)
2. **Bedürfnisdynamik:** Welche einfachen, ehrlichen Modelle für Hunger/Erschöpfung/Erregung sind vertretbar, ohne Scheinpräzision zu erzeugen?
3. **Temperatur-/Wind-Schwellen:** Welche repräsentativen Wertebereiche werden modelliert? (Quellen anzugeben; sonst MODELLIERT.)
4. **Gewöhnungsmodell:** Wie wird Gewöhnung nachlassend/stärkeabhängig abgebildet, ohne übertriebene Präzision vorzutäuschen?
5. **Genom-Referenz:** Welche öffentliche Referenz-Version wird für die Basen-Ebene genutzt (Version/Lizenz/Prüfsumme)?
6. **FlyWire-Teilmenge:** Welche Teilmenge des FlyWire-Datensatzes ist auf Zielgeräten speicherbewusst nutzbar (Ebene „Bereiche" vs. „Beispielzellen")?
7. **Stoff-Modelle:** Für welche Stoffe existiert eine belastbare Beleglage (→ BELEGT/ABGELEITET), für welche nur Modell (→ MODELLIERT/HYPOTHETISCH)?
8. **Männliche Tiere:** Welche Parameter sind für eine spätere männliche Simulation aus Literatur ableitbar – und bleiben ausdrücklich parametrisiertes Modell?
9. **Aktivitätsvisualisierung:** Welche Farbskala bleibt für Farbsehschwäche unterscheidbar und dezent (kein Neon)?
10. **Leistungsprofile:** Welche Detailtiefe läuft auf OnePlus-6T-Klasse flüssig ohne Überhitzungswarnung? (Messung im Bau-Lauf, nicht schätzt.)

## C. Prozess

- Ein offener Punkt gilt als „entschieden" erst, wenn er in `docs/DECISIONS.md` mit Datum und Quelle eingetragen ist.
- Das Bau-Team darf Offenes mit ANNAHME überbrücken (als Annahme markiert, widerrufbar).
- Wissenschaftliche Fragen werden nie durch App-Verhalten „beantwortet", sondern nur durch Daten/Quellen mit Beleg-Kategorie.
