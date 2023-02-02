package com.daniel.miaumart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.miaumart.domain.models.History
import com.daniel.miaumart.domain.repositories.ProductsRepository
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.daniel.miaumart.ui.commons.BasicUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val completed = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val history = MutableLiveData<ArrayList<History>>()

    fun signOff() {
        viewModelScope.launch {
            userRepository.deleteUserData(BasicUserData.username)
            completed.value = true
        }
    }

    fun updateProfileImage(newProfileImage: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val update =
                userRepository.updateProfileImage(newProfileImage, BasicUserData.profileImageId)) {
                is Resource.Success -> withContext(Dispatchers.Main) {
                    message.value =
                        if (update.data == true) "Profile picture updated successfully!" else "Could not update profile image!"
                }
                is Resource.Error -> withContext(Dispatchers.Main) {
                    message.value = update.message!!
                }
            }
        }
    }

    fun getAllHistory(documentName: String, listener: (ArrayList<History>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllHistory(true, documentName, listener)
        }
    }

    fun stopListening(documentName: String, listener: (ArrayList<History>) -> Unit) {
        viewModelScope.launch {
            productsRepository.getAllHistory(false, documentName, listener)
        }
    }

    fun deleteAllHistory() {
        if (history.value!!.size > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val deleteAllHistory =
                    productsRepository.deleteAllHistory(BasicUserData.username)) {
                    is Resource.Success -> withContext(Dispatchers.Main) {
                        deleteAllHistory.data!!.apply {
                            addOnSuccessListener { message.value = "History deleted successfully!" }
                            addOnFailureListener { message.value = "Error deleting history" }
                        }
                    }
                    is Resource.Error -> withContext(Dispatchers.Main) {
                        message.postValue(
                            deleteAllHistory.message.toString()
                        )
                    }
                }
            }
        }
    }

}