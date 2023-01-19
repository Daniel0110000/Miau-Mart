package com.daniel.miaumart.data.remote.firebase

import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.utilities.Constants.PI
import com.daniel.miaumart.domain.utilities.Constants.PN
import com.daniel.miaumart.domain.utilities.Constants.PP
import com.daniel.miaumart.domain.utilities.Constants.UD_DB
import com.daniel.miaumart.domain.utilities.SecurityService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.asDeferred
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
                            productName = document.getString(PN).toString(),
                            productImages = document.get(PI) as? ArrayList<String>,
                            productPrice = document.getString(PP).toString()
                        )
                    )
                }
                count.resume(productsData)
            }
    }
}

suspend fun FirebaseFirestore.getProductDetails(category: String, pid: String): Products{
    return suspendCoroutine { count ->
        collection(category).document(pid).get()
            .addOnSuccessListener { document ->
                val product = Products(
                    id = document.id,
                    productName = document.getString(PN).toString(),
                    productImages = document.get(PI) as? ArrayList<String>,
                    productPrice = document.getString(PP).toString()
                )
                count.resume(product)
            }
    }
}

suspend fun FirebaseFirestore.register(userData: HashMap<String, String?>): String{
    val document = collection(UD_DB).document(userData["username"].toString())
        .get().asDeferred().await()
    if(!document.exists()){
        collection(UD_DB).document(userData["username"].toString())
            .set(userData).asDeferred().await()
        return "Successfully registered user!"
    }
    return "Username already registered!"
}

suspend fun FirebaseFirestore.login(username: String, password: String): User?{
    return suspendCoroutine { count ->
        var userData: User? = null
        collection(UD_DB).document(username).get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val document = task.result
                    if(document!!.exists()){
                        if(SecurityService.validatePassword(password, document.getString("password").toString())){
                            userData = User(
                                0,
                                document.getString("username").toString(),
                                document.getString("profile_image").toString()
                            )
                        }
                    } else{
                        userData = null
                    }
                }
                count.resume(userData)
            }
    }
}