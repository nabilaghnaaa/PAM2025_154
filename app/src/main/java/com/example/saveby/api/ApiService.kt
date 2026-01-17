package com.example.saveby.api

import com.example.saveby.modeldata.*
import retrofit2.http.*

interface ApiService {

    // ================= AUTH =================
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): CommonResponse

    // ================= PRODUCT =================
    @GET("get_products.php")
    suspend fun getProducts(@Query("user_id") userId: Int): ProductResponse

    @FormUrlEncoded
    @POST("add_product.php")
    suspend fun addProduct(
        @Field("user_id") userId: Int,
        @Field("product_name") productName: String,
        @Field("expired_date") expiredDate: String,
        @Field("quantity") quantity: Int,
        @Field("location") location: String,
        @Field("photo_url") photoUrl: String?,
        @Field("category") category: String,
        @Field("unit") unit: String
    ): CommonResponse

    @FormUrlEncoded
    @POST("update_product.php")
    suspend fun updateProduct(
        @Field("product_id") productId: Int,
        @Field("product_name") productName: String,
        @Field("expired_date") expiredDate: String,
        @Field("quantity") quantity: Int,
        @Field("location") location: String,
        @Field("photo_url") photoUrl: String?,
        @Field("category") category: String,
        @Field("unit") unit: String
    ): CommonResponse

    @FormUrlEncoded
    @POST("delete_product.php")
    suspend fun deleteProduct(@Field("product_id") productId: Int): CommonResponse

    // ================= STATUS =================
    @FormUrlEncoded
    @POST("update_status.php")
    suspend fun updateStatus(
        @Field("product_id") productId: Int,
        @Field("status") status: String,
        @Field("new_expired") newExpired: String? = null,
        @Field("opened_date") openedDate: String? = null,
        @Field("storage_type") storageType: String? = null
    ): CommonResponse

    // ================= HISTORY =================
    @GET("get_history.php")
    suspend fun getHistory(
        @Query("user_id") userId: Int,
        @Query("status") status: String
    ): HistoryResponse

    // 🔥 FITUR BARU: HAPUS RIWAYAT SATUAN
    @FormUrlEncoded
    @POST("delete_history.php")
    suspend fun deleteHistory(
        @Field("history_id") historyId: Int
    ): CommonResponse

    // 🔥 FITUR BARU: HAPUS SEMUA RIWAYAT (RESET)
    @FormUrlEncoded
    @POST("delete_all_history.php")
    suspend fun deleteAllHistory(
        @Field("user_id") userId: Int,
        @Field("status") status: String
    ): CommonResponse


    // ================= STATISTIC =================
    @GET("statistic.php")
    suspend fun getStatistic(@Query("user_id") userId: Int): StatisticResponse
}