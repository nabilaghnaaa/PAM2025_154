package com.example.saveby

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.navigation.SaveByNavGraph

@Composable
fun SaveByApp(container: ContainerApp) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        SaveByNavGraph(
            navController = navController,
            container = container,
            padding = padding
        )
    }
}
