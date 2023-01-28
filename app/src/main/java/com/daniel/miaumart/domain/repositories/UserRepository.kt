package com.daniel.miaumart.domain.repositories

import android.net.Uri
import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.domain.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun register(userData: HashMap<String, String?>, profileImage: Uri, fileName: String): Resource<String>

    suspend fun login(username: String, password: String): Resource<User?>

    suspend fun insertUserData(user: User)

    fun getUserData(): Resource<Flow<List<User>>>

    suspend fun deleteUserData(username: String)

    suspend fun getProfileImage(fileName: String): Resource<Uri>

    suspend fun updateProfileImage(newProfileImage: Uri, fileName: String): Resource<Int>

}