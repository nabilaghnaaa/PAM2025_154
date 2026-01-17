package com.example.saveby.view.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.controlNavigasi.PetaNavigasi
import com.example.saveby.view.dashboard.HalamanDashboard
import com.example.saveby.viewmodel.DashboardViewModel
import com.example.saveby.viewmodel.viewModelFactory

fun NavGraphBuilder.dashboardGraph(
    navController: NavController,
    container: ContainerApp
) {

    composable(PetaNavigasi.DASHBOARD) {
        val vm: DashboardViewModel = viewModel(factory = viewModelFactory {
            DashboardViewModel(container.productRepository)
        })

        val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    vm.loadSession(container.sessionManager)
                    vm.loadDashboard(container.sessionManager.getUserId())
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        val userName by vm.userName.collectAsState()
        val products by vm.products.collectAsState()
        val statistic by vm.statistic.collectAsState()

        HalamanDashboard(
            userName = userName,
            products = products,
            consumedCount = statistic.totalConsumed,
            wastedCount = statistic.totalWasted,
            onAddClick = { navController.navigate(PetaNavigasi.ADD_PRODUCT) },
            onProductClick = { navController.navigate(PetaNavigasi.detailProduct(it.productId)) },
            onProfileClick = { navController.navigate(PetaNavigasi.PROFILE) },
            onConsumedClick = { navController.navigate(PetaNavigasi.HISTORY_CONSUMED) },
            onWastedClick = { navController.navigate(PetaNavigasi.HISTORY_WASTED) },
            onSearch = {}
        )
    }
}


