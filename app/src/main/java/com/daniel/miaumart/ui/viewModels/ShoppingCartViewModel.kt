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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel
@Inject
constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val message = MutableLiveData<String>()
    val productDates = MutableLiveData<ArrayList<ShoppingCartML>>()

    fun startListening(documentName: String, listener: (ArrayList<ShoppingCartML>) -> Unit) {
        viewModelScope.launch {
            try { productsRepository.getAllProductsCart(true, documentName, listener) } catch (e: Exception) { e.printStackTrace() }
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
                    delete.data?.apply {
                        addOnSuccessListener { message.value = "Product successfully removed!" }
                        addOnFailureListener { message.value = "Error deleting the product" }
                    }
                }
                is Resource.Error -> withContext(Dispatchers.Main) {
                    message.value = delete.message ?: "Error deleting product!"
                }
            }
        }
    }

    fun toBuy() = viewModelScope.launch(Dispatchers.IO) { addToHistory() }

    private suspend fun addToHistory() {
        val calendar = Calendar.getInstance()
        productDates.value?.forEach { product ->
            productsRepository.addToHistory(
                BasicUserData.username, arrayListOf(
                    product.productImage,
                    product.productName,
                    product.productPrice,
                    product.numberItems,
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
                        calendar.get(
                            Calendar.YEAR
                        )
                    }"
                )
            )
        }
        deleteAllProductsCart()
    }

    private suspend fun deleteAllProductsCart() {
        when (val allDelete = productsRepository.deleteAllProductsCart(BasicUserData.username)) {
            is Resource.Success -> withContext(Dispatchers.Main) {
                allDelete.data!!.apply {
                    addOnSuccessListener {  message.value = "Successful purchase!" }
                    addOnFailureListener { message.value = "Error when buying the products" }
                }
            }
            is Resource.Error -> withContext(Dispatchers.Main) { message.value = allDelete.message!! }
        }
    }
}