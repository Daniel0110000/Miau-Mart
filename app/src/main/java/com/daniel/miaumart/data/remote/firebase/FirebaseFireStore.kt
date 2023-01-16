package com.daniel.miaumart.data.remote.firebase

import com.daniel.miaumart.domain.models.Products
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseFirestore.getProducts(category: String): ArrayList<Products> {
    val productsData: ArrayList<Products> = arrayListOf()
    productsData.clear()
    return suspendCoroutine { count ->
        collection(category).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    productsData.add(
                        Products(
                            id = document.id,
                            productName = document.getString("product_name").toString(),
                            productImages = document.get("product_images") as? ArrayList<String>,
                            productPrice = document.getString("product_price").toString()
                        )
                    )
                }
                count.resume(productsData)
            }
    }
}