package com.example.saveby.repositori

import com.example.saveby.api.ApiConfig
import com.example.saveby.modeldata.FinalStatus
import com.example.saveby.modeldata.ProductHistory

class HistoryRepository {
    private val api = ApiConfig.getApiService()

    suspend fun getHistoryByStatus(userId: Int, status: FinalStatus): List<ProductHistory> {
        return try {
            api.getHistory(userId, status.name).data
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteHistory(historyId: Int): Boolean {
        return try {
            val response = api.deleteHistory(historyId)
            response.status == "success"
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteAllHistory(userId: Int, status: FinalStatus): Boolean {
        return try {
            val response = api.deleteAllHistory(userId, status.name)
            response.status == "success"
        } catch (e: Exception) {
            false
        }
    }

    fun filterHistory(histories: List<ProductHistory>, keyword: String): List<ProductHistory> {
        return histories.filter {
            it.productName.contains(keyword, ignoreCase = true)
        }
    }
}