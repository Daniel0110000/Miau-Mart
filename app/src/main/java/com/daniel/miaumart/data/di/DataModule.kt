package com.daniel.miaumart.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}