package com.daniel.miaumart.domain.models

data class SearchML(
    val productId: String,
    val productName: String,
    val productImages: ArrayList<String>?,
    val productPrice: String,
    val category: String
)