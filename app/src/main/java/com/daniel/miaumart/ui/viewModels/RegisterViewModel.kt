package com.daniel.miaumart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.daniel.miaumart.domain.utilities.SecurityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository
    ): ViewModel() {

    val profileImage = MutableLiveData<Uri>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val completed = MutableLiveData<Boolean>()

    fun register(){
        isLoading.value = true
        when {
            !areFieldsValid() -> {
                message.value = "Incomplete fields!"
                isLoading.value = false
            }
            !isPasswordValid() -> {
                message.value = "Password too short!"
                isLoading.value = false
            }
            !doPasswordsMatch() ->{
                message.value = "Passwords do not match!"
                isLoading.value = false
            }
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val fileName = UUID.randomUUID().toString()
                    val userData = hashMapOf(
                        "username" to username.value,
                        "password" to SecurityService.passwordHasher(password.value.toString()),
                        "profile_image" to fileName
                    )
                    when (val register = userRepository.register(userData, profileImage.value!!, fileName)){
                        is Resource.Success -> withContext(Dispatchers.Main){
                            message.value = register.data!!
                            isLoading.value = false
                            if(register.data.contains("Successfully registered user!")){
                                completed.value = true
                            }
                        }
                        is Resource.Error -> withContext(Dispatchers.Main){
                            message.value = register.message!!
                            isLoading.value = false
                        }
                    }
                }
            }
        }
    }

    private fun areFieldsValid(): Boolean{
        return profileImage.value != null && username.value!!.isNotEmpty() && password.value!!.isNotEmpty() && repeatPassword.value!!.isNotEmpty()
    }

    private fun isPasswordValid(): Boolean{
        return password.value!!.length >= 0
    }

    private fun doPasswordsMatch(): Boolean{
        return password.value!! == repeatPassword.value!!
    }

}