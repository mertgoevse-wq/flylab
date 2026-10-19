# NPU & GPU Integration — Forschungsdokument (Galaxy A56, Exynos 1580)

**Datum:** 2026-10-19 · **Gerät:** Samsung Galaxy A56 (Exynos 1580 / s5e8835, Xclipse 540)
**Evidence-Level:** MODELED (wie in CLAUDE.md gefordert; keine erfundenen Erfolgsgarantien)

---

## 1. Autopsie am Zielgerät (Beweislage)

Direkt inspiziert auf dem Gerät (Termux/PRoot, read-only Zugriff auf /vendor):

### 1.1 NPU-Runtime gefunden: Samsung SLSI ENN 2.3.28

```
/vendor/lib64/libenn_common_utils.so
/vendor/lib64/libenn_cpu_operators.so
/vendor/lib64/libenn_engine.so
/vendor/lib64/libenn_engine_lib.so
/vendor/lib64/libenn_model.so
/vendor/lib64/libenn_public_api_cpp.so          ← öffentliche C++-API (ENN 2.3.28)
/vendor/lib64/libenn_public_api_cpp_lib.so
/vendor/lib64/libenn_user.samsung_slsi.so
/vendor/lib64/libenn_user_driver_cpu.so
/vendor/lib64/libenn_user_driver_gpu.so         ← ENN-GPU-Backend
/vendor/lib64/libenn_user_driver_unified.so
/vendor/lib64/libenn_user_lib.so
/vendor/lib64/libenn_wrapper.so
/vendor/lib64/vendor.samsung_slsi.hardware.enn_aidl-V1-ndk.so   ← AIDL-HAL
```

### 1.2 Extrahierte öffentliche API-Surface (strings, libenn_public_api_cpp.so)

Kernfunktionen (Exzerpt, sortiert):
```
EnnInitialize / EnnDeinitialize
EnnOpenModel / EnnCloseModel
EnnAllocateAllBuffersWithSessionId / EnnBufferCommitWithSessionId
EnnExecuteModelWithSessionId / EnnExecuteModelWithSessionIdAsync / ...Wait
EnnExecuteModelBatch / EnnExecuteModelFastIpc
EnnCreateBuffer / EnnCreateBufferFromFdWithFlag / EnnCreateBufferCache...
EnnGetBuffersInfo / EnnGetBufferMetaByIndex / EnnGetBufferMetaByLabel
EnnGetPreferenceCoreAffinity / EnnGetPreferencePerfMode / EnnGetPreferenceTargetLatency
EnnGetPreferencePresetId / EnnGetPreferencePriority / EnnGetPreferenceTimeOut
```

Das ist dieselbe Bibliothek, die Samsung offiziell über **Exynos AI LiteCore /
Samsung Neural SDK** und den **LiteRT-Next-Samsung-Backend** (developers.google.com/edge/litert/next/samsung)
vertreibt. Dito belegt: NPU-Kern-Nodes `/dev/{npu0,npu1,npu,unpu}_throughput` (2 NPU-Kerne).

### 1.3 GPU

Xclipse 540 (RDNA2-basiert, Vulkan/OpenGL ES). Zugänglich über den LiteRT GPU-De
elegate (OpenCL) bzw. Vulkan-Feature (PackageManager-Feature `android.hardware.vulkan.version`).

---

## 2. Realistische Zugangswege (App-Kontext, nicht-root)

| Weg | Mechanismus | Status |
|-----|-------------|--------|
| **LiteRT + NNAPI-Delegate** | `Interpreter` mit `NnapiDelegate`; der NNAPI-Runtime-Layer lädt intern die ENN-Treiber (nnapi HAL `samsung_slsi.hardware.enn`) | ✅ implementiert (`AcceleratorBridge`, Strategie NPU) |
| **LiteRT GPU-Delegate** | OpenCL/Vulkan-Kernel auf Xclipse 540 | ✅ implementiert (Strategie GPU) |
| LiteRT Next / LiteRT-AI-Edge (Google Tensor-GPU-NPU-Dispatch) | Googles neuer Pfad für Exynos AI LiteCore | ⏳ dokumentiert, ausstehend ( stabile Maven-Artefakte gerade erst stabilisiert) |
| Direktes Laden von `libenn_public_api_cpp.so` | JNI dlopen der Vendor-So + eigene Buffer-Pipes | ❌ SELinux `untrusted_app` verbietet Vendor-So-Load; **nicht zulässig** |
| Samsung Neural SDK (AAR) | Offizielles Kotlin API | ⏳ erfordert Samsung-Entwicklerkonto + proprietäre AAR-Verteilung |
| PyTorch ExecuTorch Exynos-Backend | Offizielles `enn_executor` | ⏳ GitHub Issue 16395: läuft derzeit nicht auf Retail-Geräten |

**Entscheidung:** NNAPI-Delegate-Weg (1) + GPU-Delegation (2) sind die realistisch
realisierbaren Pfade. Beide sind als `AcceleratorBridge` implementiert.

---

## 3. Implemented in FlyLab

| Komponente | Datei | Zweck |
|------------|-------|-------|
| `AcceleratorProbe` | `sim/backend/AcceleratorProbe.kt` | Geräte-Evidenz (Vulkan-Feature, Exynos-Heuristik, ENN-Lib-Sichtbarkeit) |
| `AcceleratorBridge` | `sim/backend/AcceleratorBridge.kt | LiteRT-Dispatcher **NNAPI(NPU) → GPU → CPU** mit numerischem Self-Test gegen Manifest |
| Kernel-Modell | `assets/neural_kernel.tflite` | 32 KC → 8 MBON, FULLY_CONNECTED+TANH, **deterministisch generiert** (tools/generate_npu_kernel.py, seed 1337), Hand-gesetzbare vollständige NNAPI-Kompatibilität |
| Manifest | `assets/neural_kernel_manifest.json` | CPU-Referenz-Ausgaben + SHA-256 der Gewichte für den Runtime-Self-Test |
| Diagnostics-Publish | `sim/diagnostics/PerformanceDiagnosticsBridge.kt` | Aktiver Backend-Name in `PerformanceDiagnostics.getSnapshot()` sichtbar |
| Unit-Tests | `test/.../AcceleratorBridgeStrategyTest.kt` | Strategie-Ordnung NPU→GPU→CPU, Offload-Flag default-off |

### Selbsttest-Policy (wissenschaftliche Integrität)

Der numerische Self-Test mit Toleranz 2e-2 (fp16-NPU-Rundung) **muss** bestehen,
sonst wird der Hardware-Pfad verworfen und CPU-Fallback aktiv. Ein ignoriertes
Hardware-Ergebnis wäre eine falsche Behauptung — daher: Guerilla-Regel
"validated-or-rejected".

### Numerischer Offload (opt-in, standardmäßig AUS)

Da der Kernel tanh-basiert ist und die biologische Dynamics Sigmoid nutzt, ist der
Kernel ein Hardware-Proxy **nicht** bit-identisch zur CPU-Sim. Standard: aus
(`-Dflylab.npu.offload=true` opt-in fest verdrahtet in `AcceleratorBridge.offloadEnabled()`).

---

## 4. Nächste Schritte

1. **Runtime-Verifikation auf Gerät:** APK installieren; `adb logcat | grep FlyLab` — Wert `status=active backend=NPU (self-test OK; maxErr...)` prüfen.
2. **LiteRT Next Migration** sobald Maven-Artefakte für Exynos AI LiteCore stabil sind (`io.github.google-ai-edge:litert*`).
3. **Realer Offload:** KC->MBON-Schritt in `NeuralDynamics.step` über `AcceleratorBridge.inferMBON()` routen — erst nach positiver Geräterückmeldung (@see `OFFLOAD_PROPERTY`).
4. Optional: Samsung Neural SDK integrieren, wenn Entwicklerkonto verfügbar.
5. Optional: REA-MCP für BIN-Inspektion (libenn) nutzen — aktuell nicht nötig, da API-Surface über `strings`-Exzerpt ausreichend dokumentiert.
