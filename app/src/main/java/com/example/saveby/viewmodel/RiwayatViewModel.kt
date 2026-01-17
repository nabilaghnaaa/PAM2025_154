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

    fun loadHistory(userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            _history.value = repository.getHistoryByStatus(userId, status)
        }
    }

    fun deleteOne(historyId: Int, userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            val success = repository.deleteHistory(historyId)
            if (success) {
                loadHistory(userId, status)
            }
        }
    }

    fun deleteAll(userId: Int, status: FinalStatus) {
        viewModelScope.launch {
            val success = repository.deleteAllHistory(userId, status)
            if (success) {
                loadHistory(userId, status)
            }
        }
    }
}