package com.flylab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flylab.ui.BrainExplorerScreen
import com.flylab.ui.BrainExplorerViewModel
import com.flylab.ui.theme.FlyLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyLabTheme {
                FlyLabApp()
            }
        }
    }
}

@Composable
fun FlyLabApp() {
    val viewModel: BrainExplorerViewModel = viewModel()
    val brain by viewModel.brain
    val activity by viewModel.activity
    val isRunning by viewModel.isRunning

    if (brain != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            BrainExplorerScreen(
                brain = brain!!,
                activity = activity
            )

            FloatingActionButton(
                onClick = { viewModel.toggleSimulation() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Stop simulation" else "Start simulation"
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}