package com.example.saveby.repositori

import com.example.saveby.api.ApiConfig
import com.example.saveby.api.ApiService
import com.example.saveby.modeldata.User

class AuthRepository {

    // Memanggil konfigurasi API yang sudah kita buat di folder api
    private val apiService: ApiService = ApiConfig.getApiService()

    // 1. Fungsi Login (Menghubungkan ke login.php di XAMPP)
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                throw Exception("Email dan Password tidak boleh kosong")
            }

            val response = apiService.login(email, password)
            if (response.status == "success" && response.data != null) {
                // Berhasil login, mengembalikan data user dari database
                Result.success(response.data)
            } else {
                // Gagal karena email/pass salah
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            // Gagal karena koneksi internet/server mati
            Result.failure(e)
        }
    }

    // 2. Fungsi Register (Menghubungkan ke register.php di XAMPP)
    suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                throw Exception("Semua field wajib diisi")
            }

            val response = apiService.register(name, email, password)
            if (response.status == "success") {
                // Berhasil register, buat objek user sementara untuk sesi
                Result.success(User(name = name, email = email))
            } else {
                // Gagal karena email sudah ada, dll
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 3. Fungsi Logout
    suspend fun logout() {
        // Di sini nantinya bisa ditambahkan pembersihan SessionManager/Preferences
    }
}