package com.example.saveby.util

import androidx.compose.ui.graphics.Color

object Constants {

    // ================= SESSION =================
    const val PREF_NAME = "saveby_session"
    const val KEY_IS_LOGIN = "is_login"
    const val KEY_USER_ID = "user_id"
    const val KEY_NAME = "name"
    const val KEY_EMAIL = "email"

    // ================= STATUS PRODUK =================
    const val STATUS_ACTIVE = "Active"
    const val STATUS_OPENED = "Opened"
    const val STATUS_CONSUMED = "Consumed"
    const val STATUS_WASTED = "Wasted"

    // ================= WARNA INDIKATOR =================
    val COLOR_GREEN = Color(0xFF4CAF50)   // Aman
    val COLOR_ORANGE = Color(0xFFFF9800)  // Mendekati expired
    val COLOR_RED = Color(0xFFF44336)     // Hampir / sudah expired

    // ================= RIWAYAT =================
    const val HISTORY_CONSUMED = "Consumed"
    const val HISTORY_WASTED = "Wasted"
}
