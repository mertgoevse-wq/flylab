package com.flylab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FlyLab Android Shell\n\nReady to build 3D brain viewer...",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: Start 3D brain viewer */ },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Launch 3D Brain Viewer")
        }
    }
}