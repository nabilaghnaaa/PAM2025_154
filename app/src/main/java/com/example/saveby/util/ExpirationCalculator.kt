package com.example.saveby.util

import java.util.*

object ExpirationCalculator {

    fun calculateNewExpiredDate(
        category: String,
        storage: String,
        openedDate: Date,
        originalExpired: Date?
    ): Date {

        if (category.equals("Bumbu dan Bahan", ignoreCase = true)) {
            return originalExpired ?: openedDate
        }

        val days = when {
            category.equals("Minuman Kemasan", ignoreCase = true) -> when (storage) {
                "Suhu Ruangan" -> 1
                "Kulkas" -> 2
                "Freezer" -> 7
                else -> 1
            }
            category.equals("Snack & Produk Kering", ignoreCase = true) -> when (storage) {
                "Suhu Ruangan" -> 3
                "Kulkas" -> 7
                "Freezer" -> 30
                else -> 3
            }
            category.equals("Makanan Instan & Kaleng", ignoreCase = true) -> when (storage) {
                "Suhu Ruangan" -> 1
                "Kulkas" -> 2
                "Freezer" -> 14
                else -> 1
            }
            category.equals("Frozen Kemasan", ignoreCase = true) -> when (storage) {
                "Freezer" -> 30
                "Kulkas" -> 2
                else -> 0
            }
            category.equals("Produk Sensitif", ignoreCase = true) -> when (storage) {
                "Suhu Ruangan" -> 1
                "Kulkas" -> 2
                "Freezer" -> 7
                else -> 1
            }
            else -> 1
        }

        return Calendar.getInstance().apply {
            time = openedDate
            add(Calendar.DAY_OF_YEAR, days)
        }.time
    }
}