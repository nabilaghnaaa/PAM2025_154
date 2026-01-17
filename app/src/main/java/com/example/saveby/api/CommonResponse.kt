package com.example.saveby.api

import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.User

// Respons standar untuk register/tambah produk
data class CommonResponse(
    val status: String,
    val message: String
)

// Respons khusus Login: membawa data User (PENTING untuk Nama)
data class LoginResponse(
    val status: String,
    val message: String,
    val data: User? = null // Database XAMPP harus kirim field "data" berisi id, name, email
)

data class ProductResponse(
    val status: String,
    val data: List<Product> = emptyList()
)