package com.daniel.miaumart.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ShoppingCartViewModel
@Inject
constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val message = MutableLiveData<String>()

    fun startListening(documentName: String, listener: (ArrayList<ShoppingCartML>) -> Unit) {
        viewModelScope.launch {
            try{
                productsRepository.getAllProductsCart(true, documentName, listener)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun stopListening(documentName: String, listener: (ArrayList<ShoppingCartML>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllProductsCart(false, documentName, listener)
        }
    }

    fun deleteProductCart(documentName: String, productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val delete = productsRepository.deleteProductCart(documentName, productId)) {
                is Resource.Success -> withContext(Dispatchers.Main) {
                    if (delete.data == 1) {
                        message.value = "Product successfully removed!"
                    } else {
                        message.value = "Error deleting the product"
                    }
                }
                is Resource.Error -> withContext(Dispatchers.Main) {
                    message.value = delete.message.toString()
                }
            }
        }
    }

    fun toBuy(){
        viewModelScope.launch(Dispatchers.IO) {
            when (val allDelete = productsRepository.deleteAllProductsCart(BasicUserData.username)){
                is Resource.Success -> withContext(Dispatchers.Main){
                    if(allDelete.data == 1) message.value = "Successful purchase!" else message.value = "Error when buying the products"
                }
                is Resource.Error -> withContext(Dispatchers.Main){ message.value = allDelete.message!! }
            }
        }
    }

}