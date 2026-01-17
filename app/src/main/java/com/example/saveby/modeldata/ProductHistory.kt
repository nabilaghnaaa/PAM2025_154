package com.example.saveby.modeldata

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProductHistory(
    @SerializedName("history_id") val historyId: Int = 0,
    @SerializedName("product_id") val productId: Int = 0,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("product_name") val productName: String = "",
    @SerializedName("expired_date") val expiredDate: Date? = null,
    @SerializedName("quantity") val quantity: Int = 0,
    @SerializedName("location") val location: String = "",
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("final_status") val finalStatus: FinalStatus = FinalStatus.CONSUMED,
    @SerializedName("action_date") val actionDate: Date? = null
)