package com.daniel.miaumart.domain.repositories

import com.daniel.miaumart.domain.models.*
import com.daniel.miaumart.domain.utilities.Resource

interface ProductsRepository {

    suspend fun getProducts(category: String): Resource<ArrayList<Products>>

    suspend fun getProductDetails(category: String, pid: String): Resource<Products>

    suspend fun addToCard(documentName: String, productDates: ArrayList<String>): Resource<Int>

    fun getAllProductsCart(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<ShoppingCartML>) -> Unit
    )

    suspend fun deleteProductCart(documentName: String, productId: String): Resource<Int>

    suspend fun deleteAllProductsCart(documentName: String): Resource<Int>

    suspend fun addToFavorites(documentName: String, productDates: ArrayList<String>): Resource<Int>

    fun getAllProductsFavorites(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<FavoritesML>) -> Unit
    )

    suspend fun deleteFavoriteProduct(documentName: String, productId: String): Resource<Int>

    suspend fun addToHistory(documentName: String, productDates: ArrayList<String>): Resource<Int>

    fun getAllHistory(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<History>) -> Unit
    )

    suspend fun deleteAllHistory(documentName: String): Resource<Int>

    fun getAllProductsForSearch(listener: (ArrayList<SearchML>) -> Unit)

}