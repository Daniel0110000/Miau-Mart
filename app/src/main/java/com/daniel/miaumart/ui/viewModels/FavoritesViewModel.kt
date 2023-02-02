package com.daniel.miaumart.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.FavoritesML
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.daniel.miaumart.ui.commons.BasicUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val message = MutableLiveData<String>()

    fun getAllProductsFavorites(documentName: String, listener: (ArrayList<FavoritesML>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllProductsFavorites(true, documentName, listener)
        }
    }

    fun stopListening(documentName: String, listener: (ArrayList<FavoritesML>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllProductsFavorites(false, documentName, listener)
        }
    }

    fun deleteFavorite(favoriteId: String) {
        viewModelScope.launch {
            try {
                val deleteFavorite =
                    productsRepository.deleteFavoriteProduct(BasicUserData.username, favoriteId)
                withContext(Dispatchers.Main) {
                    when (deleteFavorite) {
                        is Resource.Success -> deleteFavorite.data?.let { message.postValue("Product removed from favorites!") }
                            ?: message.postValue("Failed to remove product from favorites")
                        is Resource.Error -> message.value = deleteFavorite.message.toString()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    message.value = "Failed to remove product from favorites"
                }
            }
        }
    }

}