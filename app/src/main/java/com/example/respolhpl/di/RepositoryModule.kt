package com.example.respolhpl.di

import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.cart.data.sources.CartRepositoryImpl
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.*
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesCache
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesCacheImpl
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.utils.mappers.ImageEntityListMapper
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import com.example.respolhpl.utils.mappers.ProductsMinimalListMapper
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
            mapper: ProductsMinimalListMapper
        ): ProductPagingSource = ProductPagingSourceImpl(remoteDataSource, mapper)
    }

    @Binds
    @Reusable
    abstract fun provideImagesSource(impl : ImagesCacheImpl) : ImagesCache

    @Binds
    @Reusable
    abstract fun providePagerFactory(pagerFactoryImpl: ProductsPagerFactoryImpl): ProductsPagerFactory

    @Binds
    @Reusable
    abstract fun provideProductsMapper(mapper: ProductsMinimalListMapper): ListMapper<RemoteProductMinimal, ProductMinimal>

    @Binds
    @Reusable
    abstract fun provideImageEntitiesMapper(mapper: ImageEntityListMapper) : NullableInputListMapper<ImageEntity,Image>

    @Reusable
    @Binds
    abstract fun provideProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Reusable
    @Binds
    abstract fun provideCartRepository(repositoryImpl: CartRepositoryImpl): CartRepository
}