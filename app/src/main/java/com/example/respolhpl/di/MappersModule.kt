package com.example.respolhpl.di

import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.utils.mappers.*
import com.example.respolhpl.utils.mappers.implementations.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {

    companion object {

        @Provides
        @Reusable
        fun provideProductMinimalMapper(): Mapper<RemoteProductMinimal, ProductMinimal> =
            RemoteProductMinimalToProductMinimalMapper()


        @Provides
        @Reusable
        fun provideImageRemoteMapper(): Mapper<ImageRemote, Image> = ImageRemoteToImgMapper()

        @Provides
        @Reusable
        fun provideProductMinimalListMapper(mapper: Mapper<RemoteProductMinimal, ProductMinimal>)
                : ListMapper<RemoteProductMinimal, ProductMinimal> = ListMapperImpl(mapper)

        @Provides
        @Reusable
        fun providesImgRemoteListMapper(mapper: Mapper<ImageRemote, Image>): ListMapper<ImageRemote, Image> =
            ListMapperImpl(mapper)
    }
}
