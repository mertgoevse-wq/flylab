# FlyLab – Known Limitations (bekannte Grenzen)

**Quellen:** `docs/flylab-spec.md` § 26; `CLAUDE.md`
**Zweck:** ehrliche Dokumentation dessen, was FlyLab (noch) nicht leisten kann oder nur modelliert. Diese Datei ist Teil des Produkts – die App soll Grenzen ehrlich kommunizieren, nicht verstecken.

---

## 1. Wissenschaftliche Grenzen (grundsätzlich)

1. **Nur weibliche Vollständigkeit:** Vollständige Connectom-Daten existieren nur für ein **weibliches** adultes Tier (FlyWire). Männliche Simulation bleibt parametrisiertes Modell (MODELLIERT), solange keine belastbaren Daten vorliegen. FlyLab erfindet kein vollständiges männliches Gehirn.
2. **Lernregeln sind Modelle:** Die Lernregel (Belohnung/Vermeidung/Gewöhnung) ist ein vereinfachtes, konfigurierbares Modell (SIMULIERT/MODELLIERT) – auch wenn sie durch echte Daten inspiriert ist. Sie beansprucht nicht, die zellulären Mechanismen exakt abzubilden.
3. **Verhaltensauswahl ist ein Modell:** Die Auswahllogik des Verhaltens ist vereinfacht und beansprucht keine vollständige biologische Treue.
4. **Gen-Wirkungen:** Einem willkürlichen Gen-Eingriff wird keine erfundene biologische Wirkung zugeschrieben. Unbekannte Wirkungen bleiben OFFEN/HYPOTHETISCH.
5. **Stoffwirkungen:** Fehlt eine belastbare Datenlage, wird der Effekt als Modell oder offene Frage gekennzeichnet. FlyLab macht keine medizinischen Aussagen.
6. **Population/Evolution ist vereinfacht:** Vererbung, Mutation und Auslese sind Modelle; FlyLab beansprucht nicht, reale Evolutionsdynamik quantitativ abzubilden.
7. **Keine menschliche Biologie:** Keine Endorphin-artige Belohnungsbiologie o. Ä. auf die Fliege übertragen; Neuromodulatoren nur mit belegten *Drosophila*-Botenstoffen.
8. **Denk-Sandbox ist keine Biologie:** Die virtuelle Computerwelt ist eine künstliche Fähigkeitswelt (SIMULIERT/künstlich) und reproduziert keine echte *Drosophila*-Kognition.

## 2. Technische Grenzen (aus den Anforderungen abgeleitet)

1. **Nicht alle Neuronen gleichzeitig:** Die App rendert niemals alle ~139k Neuronen in voller visually Komplexität gleichzeitig (Projektvertrag). Detailtiefe ist gestuft.
2. **Detailstufe 3 ist rechenintensiv:** „So tief wie möglich" kostet Leistung; Warnung/Empfehlung/automatische Reduktion sind daher Pflicht, nicht Optional.
3. **Rückspul-Historie:** Begrenzt durch Gerätespeicher (ANNAHME A4); konkrete Grenzen pro Gerät sind OFFEN (flylab-spec § 23.7).
4. **Übergangs-Übungsdatensatz:** Bis zur FlyWire-Integration läuft die Gehirnansicht mit klar markierten Übungsdaten, nicht mit echten Verbindungsdaten.
5. **KI optional:** Ohne Schlüssel/Netz fehlen KI-Erklärungen/Hilfe/Auswertung – bewusst, aber es ist eine Einschränkung der Komfortfunktionen (nicht des Kerns).
6. **Zielgeräte-Bandbreite:** Optimiert für Galaxy A56/OnePlus 6T; deutlich schwächere Geräte sind nicht garantiert.

## 3. Bewusste Produktgrenzen (Entscheidungen)

- Erst weibliche Fliege; Balz/Paarung erst mit männlichen Tieren (später)
- Erbgut-Ansicht erst nach dem Kompakt-Kern (Stufe 2) – „Schnell gewinnt"
- Mehrere Fliegen erst, wenn das Kausalketten-Erlebnis steht
- Denk-Sandbox zeitlich nach Stufe 2
- Offline-Kern, KI optional – kein Cloud-Zwang

## 4. Umgang mit diesen Grenzen in der App

- Dauerhafte Beleg-Kennzeichnung (streng immer)
- Auf Antippen: Was ist bekannt? Was ist Modell? Wie sicher?
- Übergangsdaten klar markiert
- Automatische Reduktion mit sichtbarem Hinweis
- Offene Punkte bleiben sichtbar (nicht wegdokumentiert)
