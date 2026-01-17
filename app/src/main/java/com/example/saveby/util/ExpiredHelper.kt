package com.example.saveby.util

import androidx.compose.ui.graphics.Color

object ExpiredHelper {
    fun getStatusColor(daysLeft: Long): Color {
        return when {
            daysLeft <= 3 -> Constants.COLOR_RED      // Sesuai SRS: Merah H-0 s.d H-3
            daysLeft <= 7 -> Constants.COLOR_ORANGE   // Sesuai SRS: Oranye H-3 s.d H-7
            else -> Constants.COLOR_GREEN            // Sesuai SRS: Hijau > H-7
        }
    }

    fun getStatusLabel(daysLeft: Long): String {
        return when {
            daysLeft < 0 -> "Sudah kedaluwarsa"
            daysLeft == 0L -> "Hari ini"
            else -> "$daysLeft hari lagi"
        }
    }
}
