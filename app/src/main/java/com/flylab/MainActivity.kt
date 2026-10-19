package com.flylab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.flylab.sim.backend.AcceleratorBridge
import com.flylab.sim.diagnostics.PerformanceDiagnosticsBridge
import com.flylab.ui.FlyLabRootScreen
import com.flylab.ui.theme.FlyLabTheme

/**
 * Initializes the hardware accelerator bridge once at app start:
 * LiteRT self-test dispatches the KC->MBON kernel to the Exynos ENN NPU
 * (via NNAPI) or the Xclipse 540 GPU (via the LiteRT GPU delegate), with
 * guaranteed CPU fallback. Result surfaces in PerformanceDiagnostics
 * (currentBackendName) without touching the deterministic sim path.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AcceleratorBridge.initialize(applicationContext)
        PerformanceDiagnosticsBridge.publish()
        setContent {
            FlyLabTheme {
                FlyLabRootScreen()
            }
        }
    }

    override fun onDestroy() {
        AcceleratorBridge.shutdown()
        super.onDestroy()
    }
}
