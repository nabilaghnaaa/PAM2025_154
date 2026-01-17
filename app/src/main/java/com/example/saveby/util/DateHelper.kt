package com.example.saveby.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateHelper {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun format(date: Date): String {
        return formatter.format(date)
    }

    fun parse(dateString: String): Date? {
        return try {
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun daysBetween(today: Date, expiredDate: Date): Long {
        val diff = expiredDate.time - today.time
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    fun today(): Date = Calendar.getInstance().time
}
