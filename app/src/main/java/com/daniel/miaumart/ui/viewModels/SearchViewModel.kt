package com.daniel.miaumart.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.SearchML
import com.daniel.miaumart.domain.repositories.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val allProducts = MutableLiveData<ArrayList<SearchML>?>()
    val message = MutableLiveData<String>()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            productsRepository.getAllProductsForSearch { products ->
                allProducts.value = products
            }
        }
    }


}