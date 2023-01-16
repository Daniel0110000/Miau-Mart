package com.daniel.miaumart.domain.repositories

import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.utilities.Resource

interface ProductsRepository {

    suspend fun getProducts(category: String): Resource<ArrayList<Products>>

}