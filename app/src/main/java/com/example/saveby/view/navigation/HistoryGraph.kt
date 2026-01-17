package com.example.saveby.view.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.saveby.modeldata.FinalStatus
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.controlNavigasi.PetaNavigasi
import com.example.saveby.view.history.HalamanRiwayat
import com.example.saveby.viewmodel.RiwayatViewModel
import com.example.saveby.viewmodel.viewModelFactory

fun NavGraphBuilder.historyGraph(
    navController: NavController,
    container: ContainerApp
) {

    composable(PetaNavigasi.HISTORY_CONSUMED) {

        val vm: RiwayatViewModel = viewModel(factory = viewModelFactory {
            RiwayatViewModel(container.historyRepository)
        })

        val historyList by vm.history.collectAsState(initial = emptyList())
        val userId = container.sessionManager.getUserId()

        LaunchedEffect(Unit) {
            vm.loadHistory(userId, FinalStatus.CONSUMED)
        }

        HalamanRiwayat(
            title = "Riwayat Terpakai",
            historyList = historyList,
            onBack = { navController.popBackStack() },
            onDeleteOne = { id ->
                vm.deleteOne(id, userId, FinalStatus.CONSUMED)
            },
            onDeleteAll = {
                vm.deleteAll(userId, FinalStatus.CONSUMED)
            }
        )
    }

    composable(PetaNavigasi.HISTORY_WASTED) {

        val vm: RiwayatViewModel = viewModel(factory = viewModelFactory {
            RiwayatViewModel(container.historyRepository)
        })

        val historyList by vm.history.collectAsState(initial = emptyList())
        val userId = container.sessionManager.getUserId()

        LaunchedEffect(Unit) {
            vm.loadHistory(userId, FinalStatus.WASTED)
        }

        HalamanRiwayat(
            title = "Riwayat Terbuang",
            historyList = historyList,
            onBack = { navController.popBackStack() },
            onDeleteOne = { id ->
                vm.deleteOne(id, userId, FinalStatus.WASTED)
            },
            onDeleteAll = {
                vm.deleteAll(userId, FinalStatus.WASTED)
            }
        )
    }
}
