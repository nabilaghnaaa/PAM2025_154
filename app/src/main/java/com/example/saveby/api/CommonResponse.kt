package com.example.saveby.api

import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.User

data class CommonResponse(
    val status: String,
    val message: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: User? = null
)

data class ProductResponse(
    val status: String,
    val data: List<Product> = emptyList()
)