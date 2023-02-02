package com.daniel.miaumart.data.di

import android.content.Context
import androidx.room.Room
import com.daniel.miaumart.data.local.room.UserDao
import com.daniel.miaumart.data.local.room.UserDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providerFirebaseFireStore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providerFirebaseStore() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providerUserDao(@ApplicationContext context: Context): UserDao {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_data.db"
        ).allowMainThreadQueries().build().getUserDao()
    }
}