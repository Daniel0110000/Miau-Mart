package com.daniel.miaumart.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val productsRepository: ProductsRepository
    ): ViewModel() {

    private val _productsByCategory = MutableStateFlow(arrayListOf<Products>())
    val productsByCategory = _productsByCategory.asStateFlow()


    val isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.value = true
    }

    fun getProductsByCategory(category: String){
        viewModelScope.launch(Dispatchers.IO) {
            when (val products = productsRepository.getProducts(category)){
                is Resource.Success -> withContext(Dispatchers.Main){
                    _productsByCategory .value = products.data!!
                    isLoading.value = true
                }
                is Resource.Error -> withContext(Dispatchers.Main){
                    isLoading.value = false
                }
            }
        }
    }



}