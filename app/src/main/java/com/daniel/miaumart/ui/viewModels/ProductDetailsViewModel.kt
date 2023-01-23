package com.daniel.miaumart.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.Products
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

    init {
        units.value = 0
        totalPrice.value = 0.0
    }

    fun getProductDetails(pid: String, category: String){
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

    fun addToCard(){
        if(units.value!! > 0){
            viewModelScope.launch(Dispatchers.IO) {
                when (val cart = productsRepository.addToCard(BasicUserData.username, arrayListOf(
                    productsByID.value?.productName ?: "null",
                    productsByID.value?.productImages?.get(0) ?: "null",
                    productsByID.value?.productPrice ?: "null",
                    units.value.toString()

                ))){
                    is Resource.Success -> withContext(Dispatchers.Main) {
                        if (cart.data == 1){
                            message.value = "Product added to cart successfully!"
                            units.value = 0
                            totalPrice.value = 0.0
                        }
                        else message.value = "Error adding product to cart"
                    }
                    is Resource.Error -> withContext(Dispatchers.Main) { message.value = cart.message.toString() }
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