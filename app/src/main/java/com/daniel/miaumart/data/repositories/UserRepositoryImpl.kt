package com.daniel.miaumart.data.repositories

import android.net.Uri
import com.daniel.miaumart.data.local.room.User
import com.daniel.miaumart.data.local.room.UserDao
import com.daniel.miaumart.data.remote.firebase.*
import com.daniel.miaumart.domain.repositories.UserRepository
import com.daniel.miaumart.domain.utilities.CallHandler
import com.daniel.miaumart.domain.utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val store: FirebaseStorage,
        private val db: UserDao
    ): UserRepository {

    override suspend fun register(userData: HashMap<String, String?>, profileImage: Uri, fileName: String): Resource<String> =
        CallHandler.callHandler { store.updateOrInsertProfileImage(profileImage, fileName); firestore.register(userData) }

    override suspend fun login(username: String, password: String): Resource<User?> = CallHandler.callHandler { firestore.login(username, password) }

    override suspend fun insertUserData(user: User) { CallHandler.callHandler { db.insertUserData(user) } }

    override suspend fun getUserData(): Resource<Flow<List<User>>> = CallHandler.callHandler { db.getUserData() }

    override suspend fun deleteUserData(username: String) { CallHandler.callHandler { db.deleteUserData(username) } }

    override suspend fun getProfileImage(fileName: String): Resource<Uri> = CallHandler.callHandler { store.getProfileImage(fileName) }

    override suspend fun updateProfileImage(newProfileImage: Uri, fileName: String): Resource<Boolean> =
        CallHandler.callHandler { store.updateOrInsertProfileImage(newProfileImage, fileName) }
}