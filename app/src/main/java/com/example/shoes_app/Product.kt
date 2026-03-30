package com.example.shoes_app

data class Product(
    val id: String? = null,
    val name: String? = null,
    val brand: String? = null,
    val price: Double? = null,
    val discount: Int? = 0,
    val sizes: List<Int>? = null,
    val category: String? = null,
    val description: String? = null,
    val imageUrls: List<String>? = null, // Support for multiple images
    val imageUrl: String? = null,        // Main image (backward compatibility)
    val rating: Double? = 0.0,
    val stockQuantity: Int? = 0
)