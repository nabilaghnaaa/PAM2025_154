package com.example.saveby.view.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.auth.HalamanLogin
import com.example.saveby.view.auth.HalamanRegister
import com.example.saveby.view.controlNavigasi.PetaNavigasi
import com.example.saveby.viewmodel.AuthViewModel
import com.example.saveby.viewmodel.viewModelFactory

fun NavGraphBuilder.authGraph(
    navController: NavController,
    container: ContainerApp
) {

    composable(PetaNavigasi.LOGIN) {
        val vm: AuthViewModel = viewModel(factory = viewModelFactory {
            AuthViewModel(container.authRepository)
        })

        val isLoading by vm.loading.collectAsState(false)
        val error by vm.error.collectAsState(null)

        HalamanLogin(
            onLoginClick = { email, pass ->
                vm.login(email, pass, container.sessionManager) {
                    navController.navigate(PetaNavigasi.DASHBOARD) {
                        popUpTo(PetaNavigasi.LOGIN) { inclusive = true }
                    }
                }
            },
            onRegisterClick = { navController.navigate(PetaNavigasi.REGISTER) },
            isLoading = isLoading,
            errorMessage = error
        )
    }

    composable(PetaNavigasi.REGISTER) {
        val vm: AuthViewModel = viewModel(factory = viewModelFactory {
            AuthViewModel(container.authRepository)
        })

        val isLoading by vm.loading.collectAsState(false)
        val error by vm.error.collectAsState(null)

        HalamanRegister(
            onRegisterClick = { n, e, p ->
                vm.register(n, e, p) { navController.popBackStack() }
            },
            onBackToLogin = { navController.popBackStack() },
            isLoading = isLoading,
            errorMessage = error
        )
    }
}
