package com.daniel.miaumart.domain.repositories

import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.models.ShoppingCartML
import com.daniel.miaumart.domain.utilities.Resource

interface ProductsRepository {

    suspend fun getProducts(category: String): Resource<ArrayList<Products>>

    suspend fun getProductDetails(category: String, pid: String): Resource<Products>

    suspend fun addToCard(documentName: String, productDates: ArrayList<String>): Resource<Int>

    fun getAllProductsCart(runCode: Boolean, documentName: String, callback: (ArrayList<ShoppingCartML>) -> Unit)

    suspend fun deleteProductCart(documentName: String, productId: String): Resource<Int>

    suspend fun deleteAllProductsCart(documentName: String): Resource<Int>

}