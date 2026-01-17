package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saveby.modeldata.FinalStatus
import com.example.saveby.modeldata.ProductHistory
import com.example.saveby.repositori.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RiwayatViewModel(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<ProductHistory>>(emptyList())
    val history: StateFlow<List<ProductHistory>> = _history

    // Fungsi untuk mengambil data riwayat
    fun loadHistory(userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            _history.value = repository.getHistoryByStatus(userId, status)
        }
    }

    // 🔥 FITUR BARU: Hapus satu item riwayat
    fun deleteOne(historyId: Int, userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            val success = repository.deleteHistory(historyId)
            if (success) {
                // Setelah berhasil hapus di database, ambil data terbaru (refresh)
                loadHistory(userId, status)
            }
        }
    }

    // 🔥 FITUR BARU: Hapus semua riwayat (Reset)
    fun deleteAll(userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            val success = repository.deleteAllHistory(userId, status)
            if (success) {
                // Setelah berhasil reset, list akan menjadi kosong (refresh)
                loadHistory(userId, status)
            }
        }
    }
}