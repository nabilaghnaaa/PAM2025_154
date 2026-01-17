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

    fun loadSession(sessionManager: SessionManager) {
        _userName.value = sessionManager.getName()
    }

    fun loadDashboard(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getAllActiveProducts(userId)
                _products.value = response

                val stats = repository.getMonthlyStatistic(userId)
                _statistic.value = stats

            } catch (e: Exception) {
                _products.value = emptyList()
                _statistic.value = Statistic(0, 0)
            } finally {
                _isLoading.value = false
            }
        }
    }
}