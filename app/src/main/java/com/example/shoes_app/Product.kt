package com.example.shoes_app

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(
    var id: String? = null,
    var name: String? = null,
    var brand: String? = null,
    var price: Double? = null,
    var category: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var imageUrls: List<String>? = null,
    var discount: Int? = 0,
    var rating: Double? = 0.0,
    var stockQuantity: Int? = 0,
    var reviewsCount: String? = "0"
)
