package com.example.shoes_app

data class User(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val isAdmin: Boolean = false,
    val profileImageUrl: String? = null
)