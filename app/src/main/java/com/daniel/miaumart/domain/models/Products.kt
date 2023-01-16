package com.daniel.miaumart.domain.models

data class Products(
    val id: String,
    val productName: String,
    val productImages: ArrayList<String>?,
    val productPrice: String
)