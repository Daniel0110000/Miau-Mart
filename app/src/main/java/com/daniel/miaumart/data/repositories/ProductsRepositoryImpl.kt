package com.daniel.miaumart.data.repositories

import com.daniel.miaumart.data.remote.firebase.*
import com.daniel.miaumart.domain.models.*
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.CallHandler
import com.daniel.miaumart.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ProductsRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore
) : ProductsRepository {

    override suspend fun getProducts(category: String): Resource<ArrayList<Products>> = CallHandler.callHandler { firestore.getProducts(category) }

    override suspend fun getProductDetails(category: String, pid: String): Resource<Products> =
        CallHandler.callHandler { firestore.getProductDetails(category, pid) }

    override suspend fun addToCard(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.addToCard(documentName, productDates) }

    override fun getAllProductsCart(runCode: Boolean, documentName: String, callback: (ArrayList<ShoppingCartML>) -> Unit) {
        firestore.getAllProductsCart(runCode, documentName) { products -> callback(products) }
    }

    override suspend fun deleteProductCart(documentName: String, productId: String): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.deleteProductCart(productId, documentName) }

    override suspend fun deleteAllProductsCart(documentName: String): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.deleteAllProductCart(documentName) }

    override suspend fun addToFavorites(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.addToFavorites(documentName, productDates) }

    override fun getAllProductsFavorites(runCode: Boolean, documentName: String, callback: (ArrayList<FavoritesML>) -> Unit) {
        firestore.getAllProductsFavorites(runCode, documentName) { favorites -> callback(favorites) } }

    override suspend fun deleteFavoriteProduct(documentName: String, productId: String): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.deleteFavoriteProduct(productId, documentName) }

    override suspend fun addToHistory(documentName: String, productDates: ArrayList<String>): Resource<Task<Void>> =
        CallHandler.callHandler { firestore.addToHistory(documentName, productDates) }

    override fun getAllHistory(runCode: Boolean, documentName: String, callback: (ArrayList<History>) -> Unit) {
        firestore.getAllHistory(runCode, documentName) { history -> callback(history) }
    }

    override suspend fun deleteAllHistory(documentName: String): Resource<Task<Void>> = CallHandler.callHandler { firestore.deleteAllHistory(documentName) }

    override fun getAllProductsForSearch(listener: (ArrayList<SearchML>) -> Unit) {
        firestore.getAllProductsForSearch { products -> listener(products) }
    }
}