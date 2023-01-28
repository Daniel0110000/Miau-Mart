package com.daniel.miaumart.data.remote.firebase

import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.domain.models.FavoritesML
import com.daniel.miaumart.domain.models.History
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.models.ShoppingCartML
import com.daniel.miaumart.domain.utilities.Constants.PI
import com.daniel.miaumart.domain.utilities.Constants.PN
import com.daniel.miaumart.domain.utilities.Constants.PP
import com.daniel.miaumart.domain.utilities.Constants.SC
import com.daniel.miaumart.domain.utilities.Constants.UD_DB
import com.daniel.miaumart.domain.utilities.SecurityService
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.asDeferred
import java.util.*
import kotlin.collections.ArrayList
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

suspend fun FirebaseFirestore.getProductDetails(category: String, pid: String): Products {
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

suspend fun FirebaseFirestore.register(userData: HashMap<String, String?>): String {
    val document = collection(UD_DB).document(userData["username"].toString())
        .get().asDeferred().await()
    if (!document.exists()) {
        collection(UD_DB).document(userData["username"].toString())
            .set(userData).asDeferred().await()
        return "Successfully registered user!"
    }
    return "Username already registered!"
}

suspend fun FirebaseFirestore.login(username: String, password: String): User? {
    return suspendCoroutine { count ->
        var userData: User? = null
        collection(UD_DB).document(username).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        if (SecurityService.validatePassword(
                                password,
                                document.getString("password").toString()
                            )
                        ) {
                            userData = User(
                                0,
                                document.getString("username").toString(),
                                document.getString("profile_image").toString()
                            )
                        }
                    } else {
                        userData = null
                    }
                }
                count.resume(userData)
            }
    }
}

suspend fun FirebaseFirestore.addToCard(
    documentName: String,
    productDates: ArrayList<String>
): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection(SC).document(documentName).set(
            hashMapOf(
                UUID.randomUUID().toString() to productDates
            ), SetOptions.merge()
        ).addOnSuccessListener {
            codeResult = 1
            count.resume(codeResult)
        }.addOnFailureListener {
            codeResult = 0
            println("The exception is: ${it.message}")
            count.resume(codeResult)
        }
    }
}

var listenerRegistrationCart: ListenerRegistration? = null
fun FirebaseFirestore.getAllProductsCart(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<ShoppingCartML>) -> Unit
) {
    if (runCode) {
        listenerRegistrationCart = collection(SC).document(documentName)
            .addSnapshotListener { document, error ->
                if (document != null && document.exists()) {
                    val productsList: ArrayList<ShoppingCartML> = arrayListOf()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            val key = field.key
                            if (value is ArrayList<*>) {
                                productsList.add(
                                    ShoppingCartML(
                                        key,
                                        value[0].toString(),
                                        value[1].toString(),
                                        value[2].toString(),
                                        value[3].toString()
                                    )
                                )
                            }
                        }
                    }
                    listener(productsList)
                }
            }
    } else {
        listenerRegistrationCart?.remove()
    }
}


suspend fun FirebaseFirestore.deleteProductCart(productId: String, documentName: String): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection(SC).document(documentName).update(
            productId, FieldValue.delete()
        )
            .addOnSuccessListener {
                codeResult = 1
                count.resume(codeResult)
            }
            .addOnFailureListener {
                codeResult = 0
                count.resume(codeResult)
            }
    }
}

suspend fun FirebaseFirestore.deleteAllProductCart(documentName: String): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection(SC).document(documentName).set(mapOf<String, Any>())
            .addOnSuccessListener {
                codeResult = 1
                count.resume(codeResult)
            }
            .addOnFailureListener {
                codeResult = 0
                count.resume(codeResult)
            }
    }
}

suspend fun FirebaseFirestore.addToFavorites(
    documentName: String,
    productDates: ArrayList<String>
): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection("favorites").document(documentName).set(
            hashMapOf(
                UUID.randomUUID().toString() to productDates
            ),
            SetOptions.merge()
        ).addOnSuccessListener {
            codeResult = 1
            count.resume(codeResult)
        }.addOnFailureListener {
            codeResult = 0
            count.resume(codeResult)
        }
    }
}

var listenerRegistrationFavorites: ListenerRegistration? = null
fun FirebaseFirestore.getAllProductsFavorites(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<FavoritesML>) -> Unit
) {
    if (runCode) {
        listenerRegistrationFavorites = collection("favorites").document(documentName)
            .addSnapshotListener { document, error ->
                if (document != null && document.exists()) {
                    val favoritesList: ArrayList<FavoritesML> = arrayListOf()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            val key = field.key
                            if (value is ArrayList<*>) {
                                favoritesList.add(
                                    FavoritesML(
                                        key,
                                        value[0].toString(),
                                        value[1].toString(),
                                        value[2].toString(),
                                        value[3].toString(),
                                        value[4].toString()
                                    )
                                )
                            }
                        }
                    }
                    listener(favoritesList)
                }
            }
    } else {
        listenerRegistrationFavorites?.remove()
    }
}

suspend fun FirebaseFirestore.deleteFavoriteProduct(productId: String, documentName: String): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection("favorites").document(documentName).update(
            productId, FieldValue.delete()
        )
            .addOnSuccessListener {
                codeResult = 1
                count.resume(codeResult)
            }
            .addOnFailureListener {
                codeResult = 0
                count.resume(codeResult)
            }
    }
}

suspend fun FirebaseFirestore.addToHistory(
    documentName: String,
    productDates: ArrayList<String>
): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection("history").document(documentName).set(
            hashMapOf(
                UUID.randomUUID().toString() to productDates
            ),
            SetOptions.merge()
        ).addOnSuccessListener {
            codeResult = 1
            count.resume(codeResult)
        }.addOnFailureListener {
            codeResult = 0
            count.resume(codeResult)
        }
    }
}

var listenerRegistrationHistory: ListenerRegistration? = null
fun FirebaseFirestore.getAllHistory(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<History>) -> Unit
) {
    if (runCode) {
        listenerRegistrationHistory = collection("history").document(documentName)
            .addSnapshotListener { document, error ->
                if (document != null && document.exists()) {
                    val historyList: ArrayList<History> = arrayListOf()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            if (value is ArrayList<*>) {
                                historyList.add(
                                    History(
                                        value[0].toString(),
                                        value[1].toString(),
                                        value[2].toString(),
                                        value[3].toString(),
                                        value[4].toString()
                                    )
                                )
                            }
                        }
                    }
                    listener(historyList)
                }
            }
    } else {
        listenerRegistrationHistory?.remove()
    }
}

suspend fun FirebaseFirestore.deleteAllHistory(documentName: String): Int {
    return suspendCoroutine { count ->
        var codeResult = 0
        collection("history").document(documentName).set(mapOf<String, Any>())
            .addOnSuccessListener {
                codeResult = 1
                count.resume(codeResult)
            }
            .addOnFailureListener {
                codeResult = 0
                count.resume(codeResult)
            }
    }
}