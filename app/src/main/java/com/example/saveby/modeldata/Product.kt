package com.example.saveby.modeldata

import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.concurrent.TimeUnit

data class Product(
    @SerializedName("product_id") val productId: Int = 0,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("product_name") val productName: String = "",
    @SerializedName("expired_date") val expiredDate: Date? = null,
    @SerializedName("quantity") val quantity: Int = 0,
    @SerializedName("location") val location: String = "",
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("status") val status: String = "ACTIVE",
    @SerializedName("category") val category: String = "",
    @SerializedName("storage_type") val storageType: String? = null,
    @SerializedName("opened_date") val openedDate: Date? = null,
    @SerializedName("unit") val unit: String = ""
) {

    val daysLeft: Long
        get() {
            if (expiredDate == null) return 0

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val expDate = Calendar.getInstance().apply {
                time = expiredDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val diff = expDate.timeInMillis - today.timeInMillis
            return TimeUnit.MILLISECONDS.toDays(diff)
        }

    val indicatorColor: androidx.compose.ui.graphics.Color
        get() = when {
            daysLeft > 7 -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
            daysLeft >= 1 -> androidx.compose.ui.graphics.Color(0xFFFF9800)
            else -> androidx.compose.ui.graphics.Color(0xFFF44336)
        }

    val priority: Int
        get() = when {
            daysLeft <= 0 -> 0
            daysLeft <= 7 -> 1
            else -> 2
        }
}