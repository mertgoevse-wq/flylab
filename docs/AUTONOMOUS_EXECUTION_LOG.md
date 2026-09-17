# FlyLab – Autonomous Execution Log

Dieses Dokument protokolliert alle Meilensteine, Architekturentscheidungen, Implementierungsschritte und Validierungsergebnisse des autonomen FlyLab-Entwicklungsprozesses.

---

## [2026-09-17] – Phase 0: Preflight, Toolchain & Build-System-Bootstrapping

### 1. Preflight-Ergebnisse
- **Skills & Regeln:** ECC-Richtlinien (`git-workflow`, `security`, `tdd-guide`, `planner`, `code-reviewer`, `coding-style`), FlyLab-Spezifikationen (`CLAUDE.md`, `SCIENTIFIC_MODEL.md`, `SIMULATION_MODEL.md`, `DATA_MODEL.md`).
- **Toolchain-Status:** Linux 6.17 ARM64 (AArch64) unter PRoot, OpenJDK 21, Gradle 8.5, AGP 8.1.4, Android SDK API 34.
- **Cross-Architecture Native Fixes:**
  - Standard Google Maven AAPT2 ist x86_64 und schlägt auf ARM64 fehl (`cannot execute: required file not found`).
  - Lösung: Debian native AArch64 AAPT2-Binärdatei (`/usr/lib/android-sdk/build-tools/debian/aapt2`) über `android.aapt2FromMavenOverride` in `gradle.properties` injiziert.
  - SDK-Lizenzen für Platform 34 und Build-Tools 33.0.1 in `/usr/lib/android-sdk/licenses/android-sdk-license` verifiziert.
- **Ressourcen & Layout:**
  - Fehlende Adaptive Icons angelegt (`ic_launcher.xml`, `ic_launcher_round.xml`, `ic_launcher_background.xml`, `ic_launcher_foreground.xml` mit wissenschaftlich dezentem Fliegen-Vektor).
  - Material 3 Farbschema in `FlyLabTheme.kt` auf klares Labor-Design umgestellt (Labor-Weiß/Slate mit dezenten Smaragd-/Minz-Akzenten `#059669`).
  - `testDebugUnitTest` erfolgreich durchgelaufen (100% grün).

---

## [2026-09-17] – Phase 1: Milestone 1 – Vertikaler Schnitt (In Arbeit)

### Geplante Schritte:
1. **Wissenschaftliche Domänen-Modelle (`com.flylab.domain`):**
   - `ScientificProvenance`: `MEASURED`, `PUBLISHED`, `DERIVED`, `MODELED`, `HYPOTHESIS`.
   - Anatomische Strukturen (Kopf, Thorax, Abdomen, Flügel, Beine, Komplexaugen, Antennen).
   - Gehirnregionen (Antennallobus / AL, Pilzkörper / MB, Zentralkomplex / CX, Optische Loben / OL).
   - Konnektom-Teilnetzwerk basierend auf FlyWire (weibliche Referenz).
   - Neuromodulatoren (Dopamin, Octopamin, Serotonin – strikt ohne menschliche Endorphin-Analogien).
2. **Deterministische Simulations-Engine (`com.flylab.sim`):**
   - Frame-rate-unabhängiger Takt (`SimulationClock`, `SimStep`).
   - Geruchsfeld & sensorische Transduktion.
   - Neuronale Populationsdynamik (L1 Gehirnbereiche & L2/L3 Schlüsselneuronen).
   - Plastizität & Verstärkungslernen (Dopamin-/Octopamin-vermittelt).
   - Motorische Übersetzung in Bewegungsvektoren (Laufen, Orientieren, Nahrungssuche).
3. **Experiment- & Replay-System (`com.flylab.experiment`):**
   - Experimentprotokollierung mit Kausalketten.
   - Determinismus über Seeds.
   - Vor- und Zurückspulen (Time-travel Replay).
4. **Präsentationsschicht (Jetpack Compose & Canvas 3D-Projektion):**
   - Interaktiver 3D-Fliegenbetrachter mit Rotation, Zoom, Pan und Organschichten.
   - Gehirnaktivitäts-Overlay & Konnektom-Graph.
   - Wissenschaftliche Laboroberfläche mit Provenance-Inspector und Versuchssteuerung.
5. **Testabdeckung & Validierung:**
   - Unit-Tests für alle Kernkomponenten mit >80% Abdeckung.
