# FlyLab – Scientific Model (Belegsystem und wissenschaftliche Zuordnungen)

**Quellen:** `docs/flylab-spec.md` § 9, § 13, § 26; `CLAUDE.md` (wissenschaftliche Integrität)
**Zielgruppe dieses Dokuments:** umsetzendes Agenten-Team. Produktentscheidungen stehen in `flylab-spec.md`.

---

## 1. Belegkategorien (verbindlich)

Jede biologische Aussage in FlyLab trägt **immer und überall** genau eine dieser Kennzeichnungen. Kategorien werden **nie stillschweigend** in eine andere umgewandelt; jede Änderung ist eine dokumentierte Entscheidung.

| Kategorie | Bedeutung | Beispiele in FlyLab |
|---|---|---|
| **BEOBACHTET** | Im eigenen Simulationslauf tatsächlich aufgetreten (nicht: in der Realität beobachtet) | „Fliege hat Quelle X in Versuch 42 aufgesucht" |
| **BELEGT** (PUBLISHED/MEASURED) | In Wissenschaft und Literatur gut belegt bzw. experimentell gemessen | Grundfunktionen der Geruchsbahn; FlyWire-Referenzdaten eines adulten weiblichen Tiers |
| **ABGELEITET** | Aus anderen Daten berechnet/kombiniert | Aus Verbindungsdaten zusammengefasste Bahnen |
| **MODELLIERT** | Vereinfachtes Rechenmodell, durch echte Daten inspiriert | Lernregel, Verhaltensauswahl, Gen-Wirkungen, männliche Tiere (parametrisiert) |
| **SIMULIERT** | Ergebnis der Simulations-Engine innerhalb eines Laufs | Aktivitätswerte, Verbindungsänderungen während eines Versuchs |
| **HYPOTHETISCH** | Vermutung, ohne belastbare Datenlage | Unbekannte Stoffwirkungen; nicht belegte Gen-Effekte |

*(Hinweis zur Terminologie: `CLAUDE.md` nennt MEASURED/PUBLISHED/DERIVED/MODELED/HYPOTHESIS; der autonome Vertrag zusätzlich OBSERVED/SIMULATED/UNKNOWN. Diese Datei bildet die verbindliche Deutsche Schnittmenge. „UNKNOWN" wird als OFFEN/offene Frage geführt, siehe § 5.)*

## 2. Regeln (aus dem Projektvertrag, unverändert verbindlich)

1. Niemals eine Hypothese als experimentell belegt darstellen.
2. Keine menschliche Biologie auf die Fliege übertragen (z. B. keine Endorphin-Belohnung). Neuromodulatoren nur mit belegten *Drosophila*-Botenstoffen (Dopamin u. a.).
3. Kein vollständiges männliches Connectom erfinden; männliche Simulation bleibt parametrisiertes Modell (MODELLIERT), solange keine Daten vorliegen.
4. Niemals einer willkürlichen Gen-Veränderung eine erfundene Wirkung zuschreiben (HYPOTHETISCH/OFFEN).
5. Keine medizinischen Aussagen.
6. KI-generierte Aussagen sind zusätzlich als „KI-generiert" markiert und werden nie stillschweigend zu Fakten.
7. Referenzdaten werden nie verändert; Simulationsschichten liegen darüber.
8. Wenn für einen Mechanismus keine ausreichenden Daten existieren: als Modell oder offene Frage kennzeichnen, nicht weglassen.
9. Die Denk-Sandbox ist eine künstliche Fähigkeitswelt (SIMULIERT/künstlich), niemals Fliegen-Biologie.

## 3. Zuordnung pro Domäne (Stand der Spezifikation)

| Domäne | Belegniveau | Begründung |
|---|---|---|
| FlyWire-Referenzdaten (weiblich, adult) | BELEGT (als Referenzdatensatz) | Echter vollständiger Datensatz; Lizenz/Version/Prüfsumme zu dokumentieren (DATA_MODEL.md) |
| Übergangs-Übungsdatensatz (falls FlyWire noch nicht integriert) | SIMULIERT (klar als „Übungsdaten" markiert) | Entscheidung aus flylab-spec § 5.3 |
| Geruchsbahn-Grundfunktion | BELEGT (grob), ABGELEITET (Details) | Am besten erforschte Leitung; Details von Datentiefe abhängig |
| Sehen / Berührung / Temperatur – Grundreaktionen | BELEGT (grob), MODELLIERT (spezifische Werte) | Verhalten qualitativ belegt; Schwellenwerte im Modell |
| Bedürfnisse (Hunger, Erschöpfung, Erregung) | MODELLIERT | Existenz und grobe Wirkung belegt; konkrete Zahlen Simulationseinstellung |
| Verhaltensrepertoire (laufen, nähern, meiden …) | BELEGT (Existenz), MODELLIERT (Auswahllogik) | Verhalten existiert; Auswahl ist Modell |
| Lernregel (Belohnung/Vermeidung/Gewöhnung) | MODELLIERT/SIMULIERT | Projektvertrag: explizite, konfigurierbare Regel, markiert als SIMULIERT/MODELLIERT |
| Gedächtnis (Kurz-/Langzeit, Stärke, Verfall) | MODELLIERT | Vereinfachtes Modell |
| Gen-Wirkungen auf Körper/Verhalten | HYPOTHETISCH, außer einzeln belegt | § 23.6 flylab-spec: Umsetzung OFFEN |
| Vererbung/Mutation | MODELLIERT | Vereinfachtes Modell |
| Population/Evolution | MODELLIERT | Ausdrücklich als vereinfachtes Modell gekennzeichnet (flylab-spec § 11) |
| Stoffwirkungen | HYPOTHETISCH/MODELLIERT je nach Datenlage | flylab-spec § 13: keine Daten → Modell/offene Frage |
| Neuromodulation (Dopamin u. a.) | BELEGT (Botenstoff-Liste), MODELLIERT (Wirkstärke im Modell) | Erweiterbares Botenstoff-System aus Projektvertrag |
| Männliche Tiere / Balz | MODELLIERT (später) | Keine vollständigen männlichen Strukturdaten |
| Denk-Sandbox | künstlich/SIMULIERT | Keine Fliegen-Biologie; klar getrennt |

## 4. Kennzeichnung in der Benutzeroberfläche

- Dauerhaft, dezent sichtbar (Entscheidung „streng immer" + Auflösung Widerspruch 3: dauerhaft, aber dezente Markierung; vollständige Erklärung auf Antippen).
- Auf Antippen: Was ist bekannt? Was ist Modell? Wie sicher ist die Aussage?
- In allen drei Erklärstufen vorhanden (ganz einfach / normal / ausführlich-wissenschaftlich).
- Wissenschaftliche Stufe nennt Grenzen der Aussage und Quellenbezug.

## 5. Offene wissenschaftliche Fragen

Siehe `docs/RESEARCH_QUESTIONS.md`. Das dort geführte Register ist der einzige Ort, an dem „UNKNOWN/offen" verwaltet wird.

## 6. Was dieses Dokument NICHT tut

- Es erfindet keine Belege für konkrete Schwellenwerte, Konzentrationen, Lernraten.
- Es behauptet keine Datenlage, die nicht in `flylab-spec.md` oder `CLAUDE.md` steht.
- Konkrete Parameter (z. B. Lernrate) sind technische/modeliste Entscheidungen des Bau-Teams und dort als SIMULIERT/MODELLIERT zu markieren.
