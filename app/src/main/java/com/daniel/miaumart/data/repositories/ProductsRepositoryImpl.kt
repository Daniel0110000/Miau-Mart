package com.daniel.miaumart.data.repositories

import com.daniel.miaumart.data.remote.firebase.*
import com.daniel.miaumart.domain.models.*
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ProductsRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore
) : ProductsRepository {

    override suspend fun getProducts(category: String): Resource<ArrayList<Products>> {
        return try {
            Resource.Success(
                data = firestore.getProducts(category)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override suspend fun getProductDetails(category: String, pid: String): Resource<Products> {
        return try {
            Resource.Success(
                data = firestore.getProductDetails(category, pid)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}"
            )
        }
    }

    override suspend fun addToCard(
        documentName: String,
        productDates: ArrayList<String>
    ): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.addToCard(documentName, productDates)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override fun getAllProductsCart(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<ShoppingCartML>) -> Unit
    ) {
        firestore.getAllProductsCart(runCode, documentName) { products ->
            callback(products)
        }
    }

    override suspend fun deleteProductCart(documentName: String, productId: String): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.deleteProductCart(productId, documentName)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}"
            )
        }
    }

    override suspend fun deleteAllProductsCart(documentName: String): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.deleteAllProductCart(documentName)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}"
            )
        }
    }

    override suspend fun addToFavorites(
        documentName: String,
        productDates: ArrayList<String>
    ): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.addToFavorites(documentName, productDates)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override fun getAllProductsFavorites(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<FavoritesML>) -> Unit
    ) {
        firestore.getAllProductsFavorites(runCode, documentName) { favorites ->
            callback(favorites)
        }
    }

    override suspend fun deleteFavoriteProduct(
        documentName: String,
        productId: String
    ): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.deleteFavoriteProduct(productId, documentName)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override suspend fun addToHistory(
        documentName: String,
        productDates: ArrayList<String>
    ): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.addToHistory(documentName, productDates)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override fun getAllHistory(
        runCode: Boolean,
        documentName: String,
        callback: (ArrayList<History>) -> Unit
    ) {
        firestore.getAllHistory(runCode, documentName) { history ->
            callback(history)
        }
    }

    override suspend fun deleteAllHistory(documentName: String): Resource<Int> {
        return try {
            Resource.Success(
                data = firestore.deleteAllHistory(documentName)
            )
        } catch (e: Exception) {
            Resource.Error(
                message = "Exception ${e.message}"
            )
        }
    }

    override fun getAllProductsForSearch(listener: (ArrayList<SearchML>) -> Unit) {
        firestore.getAllProductsForSearch { products ->
            listener(products)
        }
    }

}