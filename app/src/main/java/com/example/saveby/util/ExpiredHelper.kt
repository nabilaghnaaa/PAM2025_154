package com.example.saveby.util

import androidx.compose.ui.graphics.Color

object ExpiredHelper {
    fun getStatusColor(daysLeft: Long): Color {
        return when {
            daysLeft <= 3 -> Constants.COLOR_RED
            daysLeft <= 7 -> Constants.COLOR_ORANGE
            else -> Constants.COLOR_GREEN
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
