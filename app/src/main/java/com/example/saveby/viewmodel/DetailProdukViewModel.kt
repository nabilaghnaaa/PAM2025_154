package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.ProductStatus
import com.example.saveby.repositori.ProductRepository
import com.example.saveby.util.ExpirationCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class DetailProdukViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun loadDetail(userId: Int, productId: Int) {
        viewModelScope.launch {
            _product.value = repository.getDetailProduct(userId, productId)
        }
    }

    // ================= TELAH DIBUKA =================
    fun updateOpened(
        product: Product,
        openedDate: Date,
        storage: String,
        newLocation: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {

            val newExpired = ExpirationCalculator.calculateNewExpiredDate(
                category = product.category,
                storage = storage,
                openedDate = openedDate,
                originalExpired = product.expiredDate
            )

            repository.updateOpenedProduct(
                productId = product.productId,
                newExpired = newExpired
            )

            repository.updateProduct(
                product.copy(
                    expiredDate = newExpired,
                    location = newLocation,
                    status = ProductStatus.OPENED.name
                )
            )

            onDone()
        }
    }

    // ================= HABIS / DIBUANG =================
    fun updateStatus(
        productId: Int,
        status: ProductStatus,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.updateStatus(productId, status)
            onDone()
        }
    }

    fun deleteProduct(productId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            onDone()
        }
    }
}
