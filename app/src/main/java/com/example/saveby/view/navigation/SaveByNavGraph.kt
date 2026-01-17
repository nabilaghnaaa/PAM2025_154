package com.example.saveby.view.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.controlNavigasi.PetaNavigasi

@Composable
fun SaveByNavGraph(
    navController: NavHostController,
    container: ContainerApp,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = PetaNavigasi.LOGIN,
        modifier = Modifier.padding(padding)
    ) {
        authGraph(navController, container)
        dashboardGraph(navController, container)
        productGraph(navController, container)
        historyGraph(navController, container)
        profileGraph(navController, container)
    }
}
