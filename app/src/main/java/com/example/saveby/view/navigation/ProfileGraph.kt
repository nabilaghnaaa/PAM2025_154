package com.example.saveby.view.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.saveby.modeldata.User
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.controlNavigasi.PetaNavigasi
import com.example.saveby.view.profile.HalamanProfil

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    container: ContainerApp
) {
    composable(PetaNavigasi.PROFILE) {

        HalamanProfil(
            user = User(
                name = container.sessionManager.getName(),
                email = container.sessionManager.getEmail() ?: ""
            ),
            onBack = { navController.popBackStack() },
            onLogout = {
                container.sessionManager.clearSession()
                navController.navigate(PetaNavigasi.LOGIN) {
                    popUpTo(PetaNavigasi.DASHBOARD) { inclusive = true }
                }
            }
        )
    }
}
