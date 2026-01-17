package com.example.saveby.modeldata

import java.time.LocalDateTime

data class User(
    val userId: Int = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val createdAt: LocalDateTime? = null
)