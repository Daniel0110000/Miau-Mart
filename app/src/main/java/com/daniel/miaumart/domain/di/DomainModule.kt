package com.daniel.miaumart.domain.di

import com.daniel.miaumart.data.repositories.ProductsRepositoryImpl
import com.daniel.miaumart.domain.repositories.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun providerProductsRepository(productsRepositoryImpl: ProductsRepositoryImpl): ProductsRepository

}