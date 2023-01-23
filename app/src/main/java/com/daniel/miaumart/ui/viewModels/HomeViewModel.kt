package com.daniel.miaumart.ui.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.daniel.miaumart.ui.commons.BasicUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
        private val userRepository: UserRepository
    ): ViewModel() {

    private val _productsByCategory = MutableStateFlow(arrayListOf<Products>())
    val productsByCategory = _productsByCategory.asStateFlow()

    val isLoading = MutableLiveData<Boolean>()
    val unregisteredUser = MutableLiveData<Boolean>()
    val profileImage = MutableLiveData<Uri?>()

    init {
        isLoading.value = true
        getUserData()
    }

    fun getProductsByCategory(category: String){
        viewModelScope.launch(Dispatchers.IO) {
            when (val products = productsRepository.getProducts(category)){
                is Resource.Success -> withContext(Dispatchers.Main){
                    _productsByCategory .value = products.data!!
                    isLoading.value = false
                }
                is Resource.Error -> withContext(Dispatchers.Main){
                    isLoading.value = false
                }
            }
        }
    }

    private fun getUserData(){
        viewModelScope.launch {
            when(val userData = userRepository.getUserData()){
                is Resource.Success -> userData.data?.collect{ data ->
                    if(data.isNotEmpty()){
                        unregisteredUser.value = false
                        BasicUserData.isRegistered = true
                        BasicUserData.username = data[0].username
                        when(val image = userRepository.getProfileImage(data[0].profileImage)){
                            is Resource.Success -> {
                                profileImage.value = image.data
                                BasicUserData.profileImage = image.data.toString()
                            }
                            is Resource.Error -> { profileImage.value = null }
                        }
                    }else{
                        unregisteredUser.value = true
                    }
                }
                is Resource.Error -> Log.e("Exception", userData.message.toString())
            }
        }
    }



}