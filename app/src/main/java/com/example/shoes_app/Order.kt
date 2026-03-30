package com.example.shoes_app

data class Order(
    val orderId: String? = null,
    val userId: String? = null,
    val products: List<Product>? = null,
    val totalPrice: Double? = null,
    val status: String? = "Pending",
    val timestamp: Long? = null
)