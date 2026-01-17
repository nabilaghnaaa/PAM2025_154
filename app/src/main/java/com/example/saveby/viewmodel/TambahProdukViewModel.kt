package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saveby.modeldata.Product
import com.example.saveby.repositori.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TambahProdukViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun simpanProduk(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 🔥 Tidak bergantung pada nilai boolean
                repository.addProduct(product)

                // 🔥 Kalau tidak error → dianggap sukses
                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan saat menyimpan produk")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
