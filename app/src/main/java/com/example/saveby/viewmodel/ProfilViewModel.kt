package com.example.saveby.viewmodel

import androidx.lifecycle.ViewModel
import com.example.saveby.modeldata.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfilViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun setUser(user: User?) {
        _user.value = user
    }

    fun clearSession() {
        _user.value = null
    }
}
