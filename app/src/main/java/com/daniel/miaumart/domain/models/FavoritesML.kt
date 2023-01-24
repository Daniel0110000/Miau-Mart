package com.daniel.miaumart.domain.models

data class FavoritesML(
    val id: String,
    val productId: String,
    val productImage: String,
    val productName: String,
    val productPrice: String,
    val productCategory: String
)