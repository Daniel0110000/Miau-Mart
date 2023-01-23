package com.daniel.miaumart.data.repositories

import com.daniel.miaumart.data.remote.firebase.*
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.models.ShoppingCartML
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
        return try{
            Resource.Success(
                data = firestore.deleteAllProductCart(documentName)
            )
        } catch (e: Exception){
            Resource.Error(
                message = "Exception ${e.message}"
            )
        }
    }

}