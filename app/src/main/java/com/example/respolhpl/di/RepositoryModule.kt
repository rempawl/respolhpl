package com.example.respolhpl.di

import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.cart.data.sources.CartRepositoryImpl
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactoryImpl
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesSource
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
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
        @Provides
        @Reusable
        fun providePagingSource(
            remoteDataSource: RemoteDataSource,
            mapper: ListMapper<RemoteProductMinimal, ProductMinimal>
        ): ProductPagingSource = ProductPagingSourceImpl(remoteDataSource, mapper)


    }

    @Binds
    @Reusable
    abstract fun provideImagesSource(impl: ImagesSourceImpl): ImagesSource

    @Binds
    @Reusable
    abstract fun providePagerFactory(pagerFactoryImpl: ProductsPagerFactoryImpl): ProductsPagerFactory


    @Reusable
    @Binds
    abstract fun provideProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Reusable
    @Binds
    abstract fun provideCartRepository(repositoryImpl: CartRepositoryImpl): CartRepository

}
