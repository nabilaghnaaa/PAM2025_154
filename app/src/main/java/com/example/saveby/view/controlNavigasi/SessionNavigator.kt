package com.example.saveby.view.controlNavigasi

class SessionNavigator {

    private var isLoggedIn: Boolean = false

    fun setLoginStatus(status: Boolean) {
        isLoggedIn = status
    }

    fun isUserLoggedIn(): Boolean {
        return isLoggedIn
    }

    fun getStartDestination(): String {
        return if (isLoggedIn) {
            PetaNavigasi.DASHBOARD
        } else {
            PetaNavigasi.LOGIN
        }
    }

    fun logout(): String {
        isLoggedIn = false
        return PetaNavigasi.LOGIN
    }
}
