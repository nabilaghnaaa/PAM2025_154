package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saveby.modeldata.User
import com.example.saveby.repositori.AuthRepository
import com.example.saveby.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // LOGIN: Menerima SessionManager untuk simpan Nama
    fun login(email: String, password: String, sessionManager: SessionManager, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repository.login(email, password)
                result.onSuccess { user ->
                    // SIMPAN DATA LOGIN TERMASUK NAMA
                    sessionManager.saveLoginSession(
                        userId = user.userId,
                        name = user.name, // Ini yang dikirim dari XAMPP
                        email = user.email
                    )
                    onSuccess()
                }.onFailure { e ->
                    _error.value = e.message ?: "Email atau Password salah"
                }
            } catch (e: Exception) {
                _error.value = "Koneksi ke server gagal"
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = repository.register(name, email, password)
            result.onSuccess {
                onSuccess()
            }.onFailure {
                _error.value = it.message ?: "Registrasi gagal"
            }
            _loading.value = false
        }
    }
}