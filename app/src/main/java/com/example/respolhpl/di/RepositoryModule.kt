package com.example.respolhpl.di

import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.cart.data.sources.CartRepositoryImpl
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
interface RepositoryModule {

    @Reusable
    @Binds
    fun provideProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Reusable
    @Binds
    fun provideCartRepository(repositoryImpl: CartRepositoryImpl) : CartRepository
}