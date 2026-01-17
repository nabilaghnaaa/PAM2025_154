package com.example.saveby.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun saveLoginSession(userId: Int, name: String, email: String) {
        prefs.edit()
            .putBoolean(Constants.KEY_IS_LOGIN, true)
            .putInt(Constants.KEY_USER_ID, userId)
            .putString(Constants.KEY_NAME, name)
            .putString(Constants.KEY_EMAIL, email)
            .apply()
    }

    fun isLogin(): Boolean = prefs.getBoolean(Constants.KEY_IS_LOGIN, false)
    fun getUserId(): Int = prefs.getInt(Constants.KEY_USER_ID, -1)
    fun getEmail(): String? = prefs.getString(Constants.KEY_EMAIL, null)

    fun getName(): String {
        return prefs.getString(Constants.KEY_NAME, "User") ?: "User"
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}