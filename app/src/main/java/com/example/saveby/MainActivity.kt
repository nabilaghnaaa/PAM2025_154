package com.example.saveby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.ui.theme.SaveByTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = ContainerApp(applicationContext)

        enableEdgeToEdge()
        setContent {
            SaveByTheme {
                SaveByApp(container)
            }
        }
    }
}
