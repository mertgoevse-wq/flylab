# FlyLab – Validation (Validierung und Abnahme)

**Quellen:** `docs/flylab-spec.md` § 27; `docs/PRD.md` § 8; `CLAUDE.md`; `FLYLAB_AUTONOMOUS_WORLD_PROMPT.md` § 37–38
**Zweck:** wie nachgewiesen wird, dass FlyLab die Anforderungen erfüllt. Kein Code-Bau in diesem Lauf.

---

## 1. Zentrale Abnahme (Hauptkriterium, Entscheidung des Auftraggebers)

**Experiment-Workflow vollständig:**
1. Experiment anlegen (Umwelt/Gerüche/Stoffe/Gehirnbereiche ändern)
2. Laufen lassen (mit Pause, Zeitlupe, Zeitraffer)
3. Speichern
4. Später wieder öffnen
5. Vergleichen (mind. zwei Versuche nebeneinander)

## 2. Stufe-1-Abnahmekriterien (PRD § 8)

| Kriterium | Nachweis |
|---|---|
| Kausalkette sichtbar, klickbar, 3 Erklärstufen, dauerhafte Beleg-Kennzeichnung | Demo-Durchlauf + Strukturtest (T-031) |
| Lernen beobachtbar (Verhalten ändert sich; Verbindungen Vorher/Nachher) | Demo + Unit-Tests (T-040/T-041) |
| Rückspulen bis zu einem Punkt + Weiterlaufen ab dort | Unit-Test (T-051) + Demo |
| Offline (Alles ohne Internet) | Flugmodus-Test: kompletter Workflow ohne Netz |
| Deutsch/Englisch umschaltbar | UI-Durchlauf in beiden Sprachen |
| Dunkel/hell umschaltbar | UI-Durchlauf in beiden Stimmungen |
| Querformat vorrangig, Hochformat unterstützt | Geräte-Rotation-Durchlauf |
| Galaxy A56 **und** OnePlus 6T nutzbar | Device-Tests (oder äquivalente Profile) |
| Automatische Detailreduktion greift | Künstliche Last → Reduktion + sichtbarer Hinweis |
| Keine Dauerüberhitzungswarnung bei Standard-Detailtiefe (OnePlus-6T-Klasse) | Thermal-/Langzeit-Test |

## 3. Stufe-2-Kriterien

- Erbgut-Ansicht bis Basen-Ebene nutzbar (zoomen, verändern als markierter Eingriff, vergleichen)
- Ca. 5–10 Fliegen gleichzeitig, flüssig auf Zielgeräten
- Experiment-Round-trip über Datei (Export → Import → identische Nachstellung)

## 4. Kritische Tests (aus Projektvertrag § 37, unverändert verbindlich)

- **Determinismus (kritisch):** gleicher Seed + gleiches Modell + gleiches Experiment → identisches Ergebnis
- Zurückspulen → Weiterlaufen = ursprünglicher Verlauf
- Experiment-Persistenz: App-Tötung → Öffnen → vollständiger Zustand
- Gehirn-/Bereichsdaten: Vollständigkeit (Name, Beleg, Quelle je Bereich)
- Genom-Integrität: Referenzdaten unverändert (Prüfsumme), Overlays getrennt
- Beleg-Kennzeichnung: jede biologische Aussage trägt eine Kategorie (Strukturtest)
- Offline: keine Kernfunktion braucht Netz
- KI-Fehler: ohne Schlüssel/ohne Netz läuft der Kern unverändert (später, T-083)
- Sandbox-Isolation: keine echten Dateizugriffe (später, T-090)
- Leistung: Framerate/Thermik auf Zielgeräteprofilen

## 5. QA-Prozess (Projektvertrag § 38)

- Unit-Tests + Integrationstests + Build + Lint nach jedem größeren Schritt
- Geräte-Tests, wenn verfügbar; sonst Emulator-Profile der Zielgeräte
- **Nie behaupten, dass Gerätetests stattfanden, wenn sie nicht stattfanden**
- Fehler: untersuchen → reparieren → erneut testen (autonom)

## 6. Wissenschaftliche Validierung

- Beleg-Kategorien werden nie stillschweigend geändert (SCIENTIFIC_MODEL.md § 1)
- Übergangs-„Übungsdaten" sind immer sichtbar als solche markiert
- KI-Ausgaben tragen „KI-generiert" und erwerben nie automatisch Belegstatus
- Keine erfundenen Gen-Wirkungen; Unbekanntes bleibt OFFEN/HYPOTHETISCH

## 7. Abnahme-Durchlauf (Reihenfolge)

1. M0: Build + Start + Theme
2. M1: Fliege interaktiv
3. M2: Reiz → Reaktion
4. M3: Kette sichtbar/erklärbar
5. M4: Lernen sichtbar
6. M5: Workflow-End-to-End (zentrale Abnahme)
7. M6: Gehirn + Leistungsschutz
8. M7: Vollständige Stufe-1-Checkliste (PRD M1–M23)
