package com.daniel.miaumart.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.FavoritesML
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.models.ShoppingCartML
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.daniel.miaumart.ui.commons.BasicUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel
    @Inject
    constructor(
        private val productsRepository: ProductsRepository
    ): ViewModel() {

    val units = MutableLiveData<Int>()
    val totalPrice = MutableLiveData<Double>()
    private var price: Double = 0.0
    val productsByID = MutableLiveData<Products?>()
    val message = MutableLiveData<String>()
    val isFavorites = MutableLiveData<Boolean>()
    private val favoriteId = MutableLiveData<String>()
    private var categoryG = ""

    init {
        isFavorites.value = false
        units.value = 0
        totalPrice.value = 0.0
    }

    fun getProductDetails(pid: String, category: String){
        categoryG = category
        viewModelScope.launch {
            when(val product = productsRepository.getProductDetails(category, pid)){
                is Resource.Success -> withContext(Dispatchers.Main){
                    productsByID.value = product.data
                    price = product.data?.productPrice!!.toDouble()
                }
                is Resource.Error -> {
                    productsByID.value = null
                    Log.d("Exception", product.message.toString())
                }
            }
        }
    }

    fun checkProductFavorite() {
        productsRepository.getAllProductsFavorites(true, BasicUserData.username) { favoritesList ->
            val favorite = favoritesList.find { it.productName == productsByID.value!!.productName }
            isFavorites.postValue(favorite != null)
            if (favorite != null) {
                favoriteId.postValue(favorite.id)
            }
        }
    }

    fun stopListening(documentName: String, listener: (ArrayList<FavoritesML>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllProductsFavorites(false, documentName, listener)
        }
    }

    fun addToFavorites(){
        val product = productsByID.value
        if (product == null) {
            message.value = "Error adding the product to favorites"
            return
        }
        val favorite = arrayListOf(
            product.id,
            product.productImages?.get(0) ?: "null",
            product.productName,
            product.productPrice,
            categoryG
        )
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = productsRepository.addToFavorites(BasicUserData.username, favorite)
            withContext(Dispatchers.Main) {
                if (favorites is Resource.Success) {
                    favorites.data!!.apply {
                        addOnSuccessListener { message.value = "Product added to favorites successfully!" }
                        addOnFailureListener { message.value = "Error when adding the product to favorites" }
                    }
                } else if (favorites is Resource.Error) {
                    message.value = favorites.message.toString()
                }
            }
        }
    }

    fun deleteFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = productsRepository.deleteFavoriteProduct(BasicUserData.username, favoriteId.value.toString())
            withContext(Dispatchers.Main) {
                result.data?.apply {
                    addOnSuccessListener { message.value = "Product removed from favorites!" }
                    addOnFailureListener { message.value = "Failed to remove product from favorites" }
                } ?: run { message.value = result.message.toString() }
            }
        }
    }

    fun addToCard(){
        if(units.value!! > 0){
            viewModelScope.launch(Dispatchers.IO) {
                when (val resource = productsRepository.addToCard(BasicUserData.username, arrayListOf(
                    productsByID.value?.productName ?: "null",
                    productsByID.value?.productImages?.get(0) ?: "null",
                    productsByID.value?.productPrice ?: "null",
                    units.value.toString()

                ))){
                    is Resource.Success -> withContext(Dispatchers.Main) {
                        val data = resource.data!!
                        data.addOnSuccessListener {
                            message.value = "Product added to cart successfully!"
                            units.value = 0
                            totalPrice.value = 0.0
                        }
                        data.addOnFailureListener { message.value = "Error adding product to cart" }
                    }
                    is Resource.Error -> withContext(Dispatchers.Main) { message.value = resource.message.toString() }
                }
            }
        }else{
            message.value = "You must add at least one product"
        }
    }

    fun less(){
        if(units.value!! > 0){
            units.value = units.value?.minus(1)
            totalPrice.value = price * units.value!!
        }
    }

    fun plus(){
        units.value = units.value?.plus(1)
        totalPrice.value = price * units.value!!
    }

}