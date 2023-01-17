package com.daniel.miaumart.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.Resource
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