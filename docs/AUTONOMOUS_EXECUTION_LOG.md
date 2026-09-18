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

## [2026-09-17] – Phase 1: Milestone 1 – Vertikaler Schnitt (Vollständig implementiert & verifiziert)

### 1. Wissenschaftliche Domänen-Modelle (`com.flylab.domain.model`):
- `ScientificProvenance.kt`: Strenge Evidenzstufen (`MEASURED`, `PUBLISHED`, `DERIVED`, `MODELED`, `HYPOTHESIS`) mit DOI/PMID-Unterstützung und Validierungsregeln (z.B. Verbot erfundener männlicher Gesamtkonnektome).
- `FlyAnatomy.kt`: Vollständige morphologische Hierarchie (Kopf, Thorax, Abdomen, Beine, Flügel, Facettenaugen, Antennen).
- `BrainRegion.kt`: 7 Hauptneuropile (AL, MB, CX, LH, OL, SEZ, LAL) mit gemessenen Volumina, Zellzahlen und Referenzkoordinaten.
- `Neuromodulator.kt`: Drosophila-spezifische Neurochemie (Octopamin, Dopamin, Serotonin, GABA, Acetylcholin, Glutamat). Biologische Schutzfunktion `assertValidDrosophilaModulator` lehnt Säuger-Endorphine/Opioide strikt ab.
- `Synapse.kt`: Unveränderliche FlyWire-Konnektom-Referenz (`SynapseReference`) kombiniert mit isolierten Laufzeit-Plastizitäts-Overlays (`SynapseSimulationOverlay`).

### 2. Konnektom-Schaltkreise & Neuronale Dynamik (`com.flylab.sim`):
- `ConnectomeReference.kt`: Biologisch validierter FlyWire v783 Konnektom-Ausschnitt (AL-Glomeruli DM1, DM4, DA2; 16 KCs mit Zufallsklauen; APL-Inhibitor; DAN-PAM Belohnung & DAN-PPL1 Aversion; MBONs; Zentralkomplex E-PG/P-EN Steuerungsnetzwerk).
- `NeuralDynamicsEngine.kt`: Leaky-Integrate-Rate-Dynamik (`dr/dt = (-r + sigmoid(I - threshold)) / tau_m`) mit Geruchs-Transduktion und APL-Rückkopplungshemmung.
- `PlasticityRule.kt`: Dopamin-modulierte Drei-Faktoren-LTD an KC-zu-MBON-Synapsen für assoziatives Belohnungs- und Vermeidungs-Lernen.
- `SimulationEngine.kt`: Headless deterministische Simulations-Engine mit Ringpuffer-History (bis zu 2000 Snapshots), Time-Travel Scrubbing (`seekToStep`) und optogenetischer Perturbations-Schnittstelle (`setRegionPerturbation`).

### 3. Experiment- & Replay-System (`com.flylab.experiment`):
- `ExperimentConfiguration.kt`: Vorkonfigurierte wissenschaftliche Protokolle (Essigduft-Nahrungssuche, Geosmin-Toxin-Meidung, Antennallobus-Dämpfung).
- `ExperimentRunner.kt`: Deterministische Batch- und Live-Ausführung, Kausales Ereignis-Streaming (`BehaviorChange`, `OdorEncounter`, `FoodFound`, `PlasticityUpdate`) und paarweiser Versuchsvergleich (`TrialComparison`).

### 4. 3D-Geometrie & Canvas-Projektion (`com.flylab.render3d`):
- `Camera3D.kt`: Euklidische 3D-Vektoren, Orbit-Kamera mit Azimut, Elevation, Zoom und Distanz-Projektion mit Tiefensortierung.
- `FlyMeshGeometry.kt`: Prozedurale 3D-Geometrien (Kutikula-Segmente, Flügel, Beine, 3D-Kugelkoordinaten der Neuropile und pulsierende synaptische Verbindungen).

### 5. Benutzeroberfläche & Gestensteuerung (`com.flylab.ui`):
- `Viewport3DCanvas.kt`: Gestengesteuerter 3D-Canvas mit Touch-Rotation, Pinch-to-Zoom, Layer-Filterung (Körper, Gehirn, Konnektom, Aktivitäts-Heatmap).
- `BrainRegionInspector.kt`: Neuropil-Aktivitätsanzeigen mit Live-Schiebereglern für optogenetische Stimulation/Inhibition.
- `SensoryMotorDashboard.kt`: Bilaterale Antennen-Konzentrationsanzeigen, Hunger-/Ermüdungspegel, Drosophila-Neuromodulatoren (OA, DA, 5-HT) und Motorkommandos.
- `ExperimentControlPanel.kt`: Time-Travel Scrub-Slider, Protokollauswahl, Play/Pause, Einzelschritt-Steuerung und Wiedergabegeschwindigkeit (0.5x, 1x, 2x).
- `JournalDialog.kt`: Wissenschaftliches Labortagebuch mit Metriken, Kausalereignis-Log, KC->MBON synaptischen Gewichtsänderungen und Vergleichsanalyse.
- `ProvenanceBadge.kt`: Wissenschaftliches Daten-Herkunfts-Badge mit detailliertem Begründungs- und Publikationsdialog.
- `FlyLabRootScreen.kt` & `MainActivity.kt`: Vollständige Compose-Orchestrierung.

### 6. Validierung & Tests:
- 100% erfolgreiche Unit-Tests (`app/src/test/java/com/flylab/`):
  - `ScientificProvenanceTest`: Evidenzkonsistenz & Endorphin-Blockade verifiziert.
  - `SimulationEngineTest`: Deterministische Chemotaxis & Time-Travel Replay verifiziert.
  - `PlasticityTest`: Dopamin-gesteuerte synaptische Gewichtsdepression verifiziert.
  - `ExperimentRunnerTest`: Protokollvergleich & Kausal-Logging verifiziert.
  - `CameraAndGeometryTest`: 3D-Projektion, Kamerarotation & Mesh-Erzeugung verifiziert.
- APK-Build (`./gradlew assembleDebug`) erfolgreich abgeschlossen: 12 MB lauffähiges Debug-APK generiert.

