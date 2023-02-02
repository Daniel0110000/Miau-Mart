package com.daniel.miaumart.data.remote.firebase

import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.domain.models.*
import com.daniel.miaumart.domain.utilities.Constants.ACCESSORIES
import com.daniel.miaumart.domain.utilities.Constants.FAV
import com.daniel.miaumart.domain.utilities.Constants.FOODS
import com.daniel.miaumart.domain.utilities.Constants.HIS
import com.daniel.miaumart.domain.utilities.Constants.MEDICINES
import com.daniel.miaumart.domain.utilities.Constants.PI
import com.daniel.miaumart.domain.utilities.Constants.PN
import com.daniel.miaumart.domain.utilities.Constants.PP
import com.daniel.miaumart.domain.utilities.Constants.SC
import com.daniel.miaumart.domain.utilities.Constants.TOYS
import com.daniel.miaumart.domain.utilities.Constants.UD_DB
import com.daniel.miaumart.domain.utilities.SecurityService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseFirestore.getProducts(category: String): ArrayList<Products> {
    return suspendCoroutine { count ->
        val productsData = arrayListOf<Products>()
        collection(category).get().addOnSuccessListener { documents ->
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
    return collection(category).document(pid).get().await().let { document ->
        val product = Products(
            id = document.id,
            productName = document.getString(PN).toString(),
            productImages = document.get(PI) as ArrayList<String>,
            productPrice = document.getString(PP).toString()
        )
        product
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
    val document = collection(UD_DB).document(username).get().asDeferred().await()
    return if (document.exists() && SecurityService.validatePassword(
            password,
            document.getString("password")!!
        )
    ) {
        User(
            0,
            document.getString("username").toString(),
            document.getString("profile_image").toString()
        )
    } else null
}

fun FirebaseFirestore.addToCard(documentName: String, productDates: ArrayList<String>): Task<Void> =
    collection(SC).document(documentName)
        .set(mapOf(UUID.randomUUID().toString() to productDates), SetOptions.merge())

var listenerRegistrationCart: ListenerRegistration? = null
fun FirebaseFirestore.getAllProductsCart(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<ShoppingCartML>) -> Unit
) {
    if (runCode) {
        listenerRegistrationCart = collection(SC).document(documentName)
            .addSnapshotListener { document, _ ->
                if (document != null && document.exists()) {
                    val productsList: ArrayList<ShoppingCartML> = arrayListOf()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            val key = field.key
                            if (value is ArrayList<*> && value.size >= 4) {
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

fun FirebaseFirestore.deleteProductCart(productId: String, documentName: String): Task<Void> =
    collection(SC).document(documentName).update(productId, FieldValue.delete())

fun FirebaseFirestore.deleteAllProductCart(documentName: String): Task<Void> =
    collection(SC).document(documentName).set(mapOf<String, Any>())

fun FirebaseFirestore.addToFavorites(
    documentName: String,
    productDates: ArrayList<String>
): Task<Void> =
    collection(FAV).document(documentName)
        .set(mapOf(UUID.randomUUID().toString() to productDates), SetOptions.merge())

var listenerRegistrationFavorites: ListenerRegistration? = null
fun FirebaseFirestore.getAllProductsFavorites(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<FavoritesML>) -> Unit
) {
    if (runCode) {
        listenerRegistrationFavorites = collection(FAV).document(documentName)
            .addSnapshotListener { document, _ ->
                if (document != null && document.exists()) {
                    val favoritesList: ArrayList<FavoritesML> = arrayListOf()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            val key = field.key
                            if (value is ArrayList<*> && value.size >= 5) {
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

fun FirebaseFirestore.deleteFavoriteProduct(productId: String, documentName: String): Task<Void> =
    collection(FAV).document(documentName).update(productId, FieldValue.delete())

fun FirebaseFirestore.addToHistory(
    documentName: String,
    productDates: ArrayList<String>
): Task<Void> =
    collection(HIS).document(documentName)
        .set(mapOf(UUID.randomUUID().toString() to productDates), SetOptions.merge())

var listenerRegistrationHistory: ListenerRegistration? = null
fun FirebaseFirestore.getAllHistory(
    runCode: Boolean,
    documentName: String,
    listener: (ArrayList<History>) -> Unit
) {
    if (runCode) {
        listenerRegistrationHistory = collection(HIS).document(documentName)
            .addSnapshotListener { document, _ ->
                if (document != null && document.exists()) {
                    val historyList = arrayListOf<History>()
                    val data = document.data
                    if (data != null) {
                        for (field in data) {
                            val value = field.value
                            if (value is ArrayList<*> && value.size >= 5) {
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

fun FirebaseFirestore.deleteAllHistory(documentName: String): Task<Void> =
    collection(HIS).document(documentName).set(mapOf<String, Any>())

fun FirebaseFirestore.getAllProductsForSearch(listener: (ArrayList<SearchML>) -> Unit) {
    val collections = listOf(FOODS, MEDICINES, ACCESSORIES, TOYS)
    val productsList: ArrayList<SearchML> = arrayListOf()

    collections.forEachIndexed { index, collection ->
        collection(collection).get().addOnSuccessListener { documents ->
            for (document in documents) {
                productsList.add(
                    SearchML(
                        productId = document.id,
                        productName = document.getString(PN).toString(),
                        productImages = document.get(PI) as? ArrayList<String>,
                        productPrice = document.getString(PP).toString(),
                        category = collection
                    )
                )
            }
            if (index == collections.lastIndex) listener(productsList)
        }
    }
}