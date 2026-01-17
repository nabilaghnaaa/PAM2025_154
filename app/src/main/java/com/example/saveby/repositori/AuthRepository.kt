package com.example.saveby.repositori

import com.example.saveby.api.ApiConfig
import com.example.saveby.api.ApiService
import com.example.saveby.modeldata.User

class AuthRepository {


    private val apiService: ApiService = ApiConfig.getApiService()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                throw Exception("Email dan Password tidak boleh kosong")
            }

            val response = apiService.login(email, password)
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                throw Exception("Semua field wajib diisi")
            }

            val response = apiService.register(name, email, password)
            if (response.status == "success") {
                Result.success(User(name = name, email = email))
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun logout() {
    }
}