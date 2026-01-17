package com.example.saveby.repositori

import com.example.saveby.api.ApiConfig
import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.ProductStatus
import com.example.saveby.modeldata.Statistic
import java.text.SimpleDateFormat
import java.util.*

class ProductRepository {

    private val apiService = ApiConfig.getApiService()
    private val dbDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend fun getAllActiveProducts(userId: Int): List<Product> {
        return try {
            apiService.getProducts(userId).data
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ✅ dipakai detail
    suspend fun getDetailProduct(userId: Int, productId: Int): Product? {
        return try {
            apiService.getProducts(userId).data.find { it.productId == productId }
        } catch (e: Exception) {
            null
        }
    }

    // ================= TAMBAH PRODUK =================
    suspend fun addProduct(product: Product): Boolean {
        return try {
            val dateStr = product.expiredDate?.let { dbDateFormat.format(it) } ?: ""

            val response = apiService.addProduct(
                userId = product.userId,
                productName = product.productName,
                expiredDate = dateStr,
                quantity = product.quantity,
                unit = product.unit,                 // ✅ FIX
                location = product.location,
                photoUrl = product.photoUrl,
                category = product.category
            )

            response.status == "success"
        } catch (e: Exception) {
            false
        }
    }

    // ================= UPDATE PRODUK =================
    suspend fun updateProduct(product: Product): Boolean {
        return try {
            val dateStr = product.expiredDate?.let { dbDateFormat.format(it) } ?: ""

            val response = apiService.updateProduct(
                productId = product.productId,
                productName = product.productName,
                expiredDate = dateStr,
                quantity = product.quantity,
                unit = product.unit,                 // ✅ FIX
                location = product.location,
                photoUrl = product.photoUrl,
                category = product.category
            )

            response.status == "success"
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteProduct(productId: Int): Boolean {
        return try {
            apiService.deleteProduct(productId).status == "success"
        } catch (e: Exception) {
            false
        }
    }

    // ================= TELAH DIBUKA =================
    suspend fun updateOpenedProduct(
        productId: Int,
        newExpired: Date
    ): Boolean {
        return try {
            apiService.updateStatus(
                productId = productId,
                status = ProductStatus.OPENED.name,
                newExpired = dbDateFormat.format(newExpired)
            ).status == "success"
        } catch (e: Exception) {
            false
        }
    }

    // ================= HABIS / DIBUANG =================
    suspend fun updateStatus(productId: Int, status: ProductStatus): Boolean {
        return try {
            apiService.updateStatus(productId, status.name, null).status == "success"
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMonthlyStatistic(userId: Int): Statistic {
        return try {
            apiService.getStatistic(userId).data
        } catch (e: Exception) {
            Statistic(0, 0)
        }
    }
}
