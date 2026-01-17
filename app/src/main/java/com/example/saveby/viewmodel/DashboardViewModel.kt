package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.Statistic
import com.example.saveby.repositori.ProductRepository
import com.example.saveby.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName

    private val _products = MutableStateFlow<List
    <Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _statistic = MutableStateFlow(Statistic(0, 0))
    val statistic: StateFlow<Statistic> = _statistic

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Memuat nama user dari session
    fun loadSession(sessionManager: SessionManager) {
        _userName.value = sessionManager.getName()
    }

    // Fungsi utama buat tarik data real-time
    fun loadDashboard(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Tarik daftar produk aktif yang belum expired/habis
                val response = repository.getAllActiveProducts(userId)
                _products.value = response

                // 2. Tarik statistik terbaru (Consumed & Wasted) dari database
                // Ini yang bikin angka di kotak Dashboard berubah dari 0 ke 1 dst.
                val stats = repository.getMonthlyStatistic(userId)
                _statistic.value = stats

            } catch (e: Exception) {
                // Jika error, list dikosongkan agar tidak nampilin data lama
                _products.value = emptyList()
                _statistic.value = Statistic(0, 0)
            } finally {
                _isLoading.value = false
            }
        }
    }
}