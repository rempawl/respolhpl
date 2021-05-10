package com.example.respolhpl.di

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.utils.mappers.*
import com.example.respolhpl.utils.mappers.facade.MappersFacade
import com.example.respolhpl.utils.mappers.facade.MappersFacadeImpl
import com.example.respolhpl.utils.mappers.implementations.ImageRemoteToImgMapper
import com.example.respolhpl.utils.mappers.implementations.ImgEntityToImgMapper
import com.example.respolhpl.utils.mappers.implementations.ProductRemoteToProductMapper
import com.example.respolhpl.utils.mappers.implementations.RemoteProductMinimalToProductMinimalMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {

    @Binds
    @Reusable
    abstract fun provideMappersFacade(mappersFacadeImpl: MappersFacadeImpl): MappersFacade

    @Binds
    @Reusable
    abstract fun provideRemoteProdToProdMapper(mapper: ProductRemoteToProductMapper): Mapper<RemoteProduct, Product>

    companion object {
        @Provides
        @Reusable
        fun provideProductMinimalMapper(): Mapper<RemoteProductMinimal, ProductMinimal> =
            RemoteProductMinimalToProductMinimalMapper()

        @Provides
        @Reusable
        fun provideImageEntityMapper(): Mapper<ImageEntity, Image> = ImgEntityToImgMapper()

        @Provides
        @Reusable
        fun provideImageRemoteMapper(): Mapper<ImageRemote, Image> = ImageRemoteToImgMapper()

        @Provides
        @Reusable
        fun provideProductMinimalListMapper(mapper: Mapper<RemoteProductMinimal, ProductMinimal>)
                : ListMapper<RemoteProductMinimal, ProductMinimal> = ListMapperImpl(mapper)

        @Provides
        @Reusable
        fun provideImgEntityListMapper(mapper: Mapper<ImageEntity, Image>): NullableInputListMapper<ImageEntity, Image> =
            NullableInputListMapperImpl(mapper)

        @Provides
        @Reusable
        fun providesImgRemoteListMapper(mapper: Mapper<ImageRemote, Image>): ListMapper<ImageRemote, Image> =
            ListMapperImpl(mapper)
    }
}