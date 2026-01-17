package com.example.saveby.view.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.saveby.repositori.ContainerApp
import com.example.saveby.view.controlNavigasi.PetaNavigasi
import com.example.saveby.view.product.HalamanDetailProduk
import com.example.saveby.view.product.HalamanEditProduk
import com.example.saveby.view.product.HalamanTambahProduk
import com.example.saveby.viewmodel.DetailProdukViewModel
import com.example.saveby.viewmodel.EditProdukViewModel
import com.example.saveby.viewmodel.TambahProdukViewModel
import com.example.saveby.viewmodel.viewModelFactory

fun NavGraphBuilder.productGraph(
    navController: NavController,
    container: ContainerApp
) {

    composable(PetaNavigasi.ADD_PRODUCT) {

        val vm: TambahProdukViewModel = viewModel(factory = viewModelFactory {
            TambahProdukViewModel(container.productRepository)
        })

        val isLoading by vm.isLoading.collectAsState(initial = false)

        HalamanTambahProduk(
            onBack = { navController.popBackStack() },
            onSimpan = { product ->
                val productWithUser = product.copy(
                    userId = container.sessionManager.getUserId()
                )
                vm.simpanProduk(
                    product = productWithUser,
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            },
            isLoading = isLoading
        )
    }

    composable(
        route = PetaNavigasi.DETAIL_PRODUCT,
        arguments = listOf(navArgument("productId") { type = NavType.IntType })
    ) { backStackEntry ->

        val productId = backStackEntry.arguments?.getInt("productId") ?: 0

        val detailVM: DetailProdukViewModel = viewModel(factory = viewModelFactory {
            DetailProdukViewModel(container.productRepository)
        })

        val productDetail by detailVM.product.collectAsState(initial = null)

        LaunchedEffect(productId) {
            detailVM.loadDetail(container.sessionManager.getUserId(), productId)
        }

        productDetail?.let { product ->
            HalamanDetailProduk(
                product = product,
                onEdit = {
                    navController.navigate("edit_product/${product.productId}")
                },
                onDelete = {
                    detailVM.deleteProduct(product.productId) {
                        navController.popBackStack()
                    }
                },
                onOpened = { openedDate, storage, newLocation ->
                    detailVM.updateOpened(
                        product = product,
                        openedDate = openedDate,
                        storage = storage,
                        newLocation = newLocation
                    ) {
                        navController.popBackStack()
                    }
                },
                onFinalStatus = { status ->
                    detailVM.updateStatus(product.productId, status) {
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }

    composable(
        route = "edit_product/{productId}",
        arguments = listOf(navArgument("productId") { type = NavType.IntType })
    ) { backStackEntry ->

        val productId = backStackEntry.arguments?.getInt("productId") ?: 0

        val detailVM: DetailProdukViewModel = viewModel(factory = viewModelFactory {
            DetailProdukViewModel(container.productRepository)
        })

        val editVM: EditProdukViewModel = viewModel(factory = viewModelFactory {
            EditProdukViewModel(container.productRepository)
        })

        val currentProduct by detailVM.product.collectAsState(initial = null)
        val isUpdating by editVM.isLoading.collectAsState(initial = false)

        LaunchedEffect(productId) {
            detailVM.loadDetail(container.sessionManager.getUserId(), productId)
        }

        currentProduct?.let { product ->
            HalamanEditProduk(
                product = product,
                onBack = { navController.popBackStack() },
                onUpdate = { updated ->
                    editVM.updateProduct(updated) {
                        navController.navigate(PetaNavigasi.DASHBOARD) {
                            popUpTo(PetaNavigasi.DASHBOARD) { inclusive = true }
                        }
                    }
                },
                onDelete = {
                    detailVM.deleteProduct(product.productId) {
                        navController.navigate(PetaNavigasi.DASHBOARD) {
                            popUpTo(PetaNavigasi.DASHBOARD) { inclusive = true }
                        }
                    }
                },
                isLoading = isUpdating
            )
        }
    }
}
