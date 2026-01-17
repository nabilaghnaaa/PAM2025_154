package com.example.saveby.repositori

import android.content.Context
import com.example.saveby.util.SessionManager

/**
 * Container untuk inisialisasi semua repository.
 */
class ContainerApp(private val context: Context) {

    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository()
    }

    val historyRepository: HistoryRepository by lazy {
        HistoryRepository()
    }

    fun getAppContext() = context
}