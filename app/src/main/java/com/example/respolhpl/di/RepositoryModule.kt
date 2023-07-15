package com.example.respolhpl.di

import com.example.respolhpl.data.sources.repository.CartRepository
import com.example.respolhpl.data.sources.repository.CartRepositoryImpl
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import com.example.respolhpl.utils.mappers.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class RepositoryModule {

    companion object {

    }

    @Reusable
    @Binds
    abstract fun provideProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Reusable
    @Binds
    abstract fun provideCartRepository(repositoryImpl: CartRepositoryImpl): CartRepository

}
