# FlyLab – AI Model (KI-Modell)

**Quellen:** `docs/flylab-spec.md` § 19.2; `CLAUDE.md`; `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` § 23–27
**Status:** Später (nach Stufe 2 geplant); Architektur von Anfang an so anlegen, dass KI optional bleibt.

---

## 1. Produktentscheidungen (verbindlich)

- **Alles Grundlegende läuft offline.** KI ist **nie Voraussetzung** für Simulation, Experimente, Gehirn, Erbgut oder Tagebuch.
- Der Nutzer **wählt selbst den KI-Dienst** (eigener Schlüssel, „BYOK"; zumindest Google/Gemini wegen vorhandenem Abo, weitere Dienste möglich).
- KI wird **aktiv eingebunden** – aber nur in den drei bestätigten Aufgaben (§ 2).

## 2. KI-Aufgaben (alle drei bestätigt)

1. **Erklären:** verständliche Erklärungen, Fragen beantworten – in den drei Erklärstufen (ganz einfach / normal / ausführlich-wissenschaftlich).
2. **Experiment-Hilfe:** Vorschläge, was man als Nächstes testen könnte; Ergebnisse zusammenfassen.
3. **Auswertung:** Versuche und Versuchsreihen auswerten, Muster finden.

## 3. Trennungsregeln (verbindlich)

- KI-Ausgaben sind **immer** als „KI-generiert" markiert.
- KI-Ausgaben werden **nie stillschweigend zu wissenschaftlichen Fakten**; sie erhalten keine Beleg-Kategorie aus SCIENTIFIC_MODEL.md und überschreiben keine.
- KI receives only: Simulationszustand, Experiment-Daten, Beobachtungen, Belege – Output: Erklärung/Hypothese/Vorschlag/Zusammenfassung/Unsicherheit (Projektvertrag § 25).
- Fällt KI aus oder ist kein Schlüssel konfiguriert: Kernfunktionen laufen ohne Einschränkung weiter (Projektvertrag § 24: „disable cloud AI gracefully").

## 4. Sicherheit

- Kein Schlüssel im Programmtext (Projektvertrag § 44).
- KI-Agenten erhalten **nie** unbegrenzten Gerätezugriff (Projektvertrag § 26: Zulassungsliste, Sandkasten, Zeitlimits, Ressourcengrenzen, Dateisystem-Isolation).
- Denk-Sandbox: KI läuft dort nur innerhalb der abgeschotteten virtuellen Welt (SIMULATION_MODEL.md § 12).

## 5. Dienste-Auswahl

- Bestätigt: Nutzer wählt Dienst selbst (BYOK). Google/Gemini mindestens vorgesehen.
- **OFFEN** (flylab-spec § 23.9): konkrete weitere Dienste und deren Einbindungsreihenfolge.
- Umsetzung als austauschbare Schnittstelle (Projektvertrag § 23: kein fester Dienst; lokale KI/OpenAI-kompatibel/Andere möglich) – konkrete Technik entscheidet das Bau-Team.

## 6. Kennzeichnung in der Oberfläche

- Jede KI-Antwort sichtbar als „KI-generiert".
- Hypothesen/Vorschläge der KI sind HYPOTHETISCH, bis sie im Experiment beobachtet (BEOBACHTET) oder als Beleg gekennzeichnet sind – sie erwerben nie automatisch Belegstatus.

## 7. Später (nicht bestätigt, nicht einplanen)

- Lokale KI-Modelle auf dem Gerät: möglich, aber **keine bestätigte Anforderung** (flylab-spec § 19.2 nennt nur freie Dienstwahl) – OFFEN.
- Wissens-Einlesen (Bücher/Paper/Notizen) mit Provenienz (Projektvertrag § 27): langfristige Idee, keine Stufe-1/2-Anforderung.
