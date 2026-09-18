package com.flylab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.flylab.ui.FlyLabRootScreen
import com.flylab.ui.theme.FlyLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyLabTheme {
                FlyLabRootScreen()
            }
        }
    }
}
