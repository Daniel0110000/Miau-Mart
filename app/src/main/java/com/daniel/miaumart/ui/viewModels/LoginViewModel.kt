package com.daniel.miaumart.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val completed = MutableLiveData<Boolean>()

    init {
        username.value = ""
        password.value = ""
    }

    fun login() {
        if (!validateFields()) return
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            when (val login = userRepository.login(username.value!!, password.value!!)) {
                is Resource.Success -> withContext(Dispatchers.Main) {
                    if (login.data != null) {
                        userRepository.insertUserData(login.data)
                        completed.value = true
                    } else {
                        message.value = "Incorrect username or password"
                    }
                    isLoading.value = false
                }
                is Resource.Error -> withContext(Dispatchers.Main){
                    message.value = login.message ?: "Error"
                    isLoading.value = false
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        return if (!username.value.isNullOrBlank() && !password.value.isNullOrBlank()) {
            true
        } else {
            message.value = "Incomplete fields!"
            isLoading.value = false
            false
        }
    }

}