package com.daniel.miaumart.data.repositories

import android.net.Uri
import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.data.local.room.UserDao
import com.daniel.miaumart.data.remote.firebase.insertImage
import com.daniel.miaumart.data.remote.firebase.login
import com.daniel.miaumart.data.remote.firebase.register
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val store: FirebaseStorage,
        private val db: UserDao
    ): UserRepository {

    override suspend fun register(
        userData: HashMap<String, String?>,
        profileImage: Uri,
        fileName: String
    ): Resource<String> {
        return try{
            store.insertImage(profileImage, fileName)
            Resource.Success(
                data = firestore.register(userData)
            )
        } catch (e: Exception){
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override suspend fun login(username: String, password: String): Resource<User?> {
        return try{
            Resource.Success(
                data = firestore.login(username, password)
            )
        } catch (e: Exception){
            Resource.Error(
                message = "Exception ${e.message}!"
            )
        }
    }

    override suspend fun insertUserData(user: User) {
        db.insertUserData(user)
    }
}