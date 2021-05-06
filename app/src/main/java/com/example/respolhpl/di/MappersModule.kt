package com.example.respolhpl.di

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.utils.mappers.*
import com.example.respolhpl.utils.mappers.implementations.ImageRemoteToDomainMapper
import com.example.respolhpl.utils.mappers.implementations.ImgEntityToDomainMapper
import com.example.respolhpl.utils.mappers.implementations.RemoteToDomainProductMinimalMapper
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    @Reusable
    fun provideProductMinimalMapper() : Mapper<RemoteProductMinimal,ProductMinimal> =
        RemoteToDomainProductMinimalMapper()

    @Provides
    @Reusable
    fun provideImageEntityMapper() : Mapper<ImageEntity,Image> = ImgEntityToDomainMapper()

    @Provides
    @Reusable
    fun provideImageRemoteMapper() : Mapper<ImageRemote,Image> = ImageRemoteToDomainMapper()

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