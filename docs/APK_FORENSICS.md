# APK Forensics — FlyLab Debug Build

Stand: 2026-10-19 · Datei: `flylab/app/build/outputs/apk/debug/app-debug.apk` (30.321.300 Bytes, gebaut 19.10.2026 20:42)
Branch: `feat/simulator-world-brain-view` · Commit `c06a69a` · Arbeitsbaum sauber
Werkzeuge: `apkanalyzer`, `aapt` (vorhanden). `jadx`/`apktool` **nicht installiert** → kein Decompile, nur Manifest/Dex/Inventar.

## Identität (apkanalyzer, verifiziert)

| Feld | Wert |
|---|---|
| application-id | `com.flylab` |
| versionCode / versionName | 1 / 1.0 |
| targetSdk / minSdk | 34 / 21 |
| Activities | nur `com.flylab.MainActivity` |
| Provider | nur `androidx.startup.InitializationProvider` |
| Berechtigungen | nur eigene `com.flylab.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` — **keine** Kamera-/Netz-/Speicher-Permissions |

## Dex-Befund (`apkanalyzer dex packages`)

- Gesamt: ~134k definierte Methoden, davon ~99k `androidx` (~70k `androidx.compose`, ~23k allein `material.icons`).
- `com.flylab`: ~8.778 Methoden — plausibel für 88 Kotlin-Dateien, keine fremden/verschleierten Pakete.
- Auffällig: `LiveLiterals$*`-Klassen in allen Domain-Modellen → **Debug-Build mit Compose Live Literals** (erwartbar, kein Release).
- Keine 3D-Engine im Dex (kein Filament/SceneView/OpenGL-Wrapper) → bestätigt: Rendering = eigener Compose-Canvas (`Viewport3DCanvas` + `FlyMeshGeometry` + `Camera3D`).

## Native Libs & Assets (`apkanalyzer files list`)

- `assets/neural_kernel.tflite` + `assets/neural_kernel_manifest.json` vorhanden (32 KC → 8 MBON, deterministischer Kernel).
- `libtensorflowlite_jni.so` + `libtensorflowlite_gpu_jni.so` für **alle 4 ABIs** (arm64-v8a, armeabi-v7a, x86, x86_64) → erklärt ~30 MB Größe; für A56-Only-Builds per ABI-Split reduzierbar.
- Keine `.tflite`-Labels jenseits des Kernels, keine weiteren ML-Modelle.

## Quelle ↔ APK-Abgleich

- Paket/Version/Activity stimmen mit `app/build.gradle` + `MainActivity.kt` überein.
- `ConnectomeReference` (repräsentativer Schaltkreis, FlyWire-Motiv-abgeleitet) und `BrainDataRepository` (Platzhalter-Koordinaten, selbst dokumentiert) sind genau das, was die APK enthält: **kein vollständiges FlyWire-Konnektom** — ehrlich gelabelt, kein Widerspruch.
- `docs/NPU_GPU_RESEARCH.md` als MODELED gekennzeichnet — korrekt, kein Validierungsanspruch.
- Die gesuchte Datei **`FlyLab-debug.apk` existiert nirgends** auf diesem System (gesucht in `/home/mert`, `/tmp`); analysiert wurde stattdessen der frische, reproduzierbare Build `app-debug.apk`. **Veraltete APKs: keine gefunden.**

## Offene Punkte

1. Kein Bytecode-Decompile möglich (kein jadx/apktool) → Verifikation auf Struktur-, nicht Code-Ebene.
2. `simulation.SimulationEngine` **und** `sim.SimulationEngine` sind beide im Dex und beide live verdrahtet (RootScreen/ExperimentRunner vs. BrainExplorerViewModel) → zwei Zustandsmodelle, Zusammenführung in Phase B nötig.
3. Debug-Overhead (Live Literals, 4 ABIs) gehört nicht in eine Release-Bewertung.
