package com.daniel.miaumart.domain.repositories

import com.daniel.miaumart.domain.models.*
import com.daniel.miaumart.domain.utilities.Resource
import com.google.android.gms.tasks.Task

interface ProductsRepository {

    suspend fun getProducts(category: String): Resource<ArrayList<Products>>

    suspend fun getProductDetails(category: String, pid: String): Resource<Products>

    suspend fun addToCard(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>>

    fun getAllProductsCart(runCode: Boolean, documentName: String, callback: (ArrayList<ShoppingCartML>) -> Unit)

    suspend fun deleteProductCart(documentName: String, productId: String): Resource<Task<Void>>

    suspend fun deleteAllProductsCart(documentName: String): Resource<Task<Void>>

    suspend fun addToFavorites(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>>

    fun getAllProductsFavorites(runCode: Boolean, documentName: String, callback: (ArrayList<FavoritesML>) -> Unit)

    suspend fun deleteFavoriteProduct(documentName: String, productId: String): Resource<Task<Void>>

    suspend fun addToHistory(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>>

    fun getAllHistory(runCode: Boolean, documentName: String, callback: (ArrayList<History>) -> Unit)

    suspend fun deleteAllHistory(documentName: String): Resource<Task<Void>>

    fun getAllProductsForSearch(listener: (ArrayList<SearchML>) -> Unit)

}