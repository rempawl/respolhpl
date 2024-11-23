package com.rempawl.respolhpl.di

import com.rempawl.respolhpl.data.sources.repository.CartRepository
import com.rempawl.respolhpl.data.sources.repository.CartRepositoryImpl
import com.rempawl.respolhpl.data.sources.repository.ProductRepository
import com.rempawl.respolhpl.data.sources.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    abstract fun provideCartRepository(repositoryImpl: CartRepositoryImpl): CartRepository
}
