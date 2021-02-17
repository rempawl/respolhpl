package com.example.respolhpl.di

import com.example.respolhpl.data.sources.*
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface DataModule {

    @Binds
    @Reusable
    fun provideRemoteDataSource(dataSourceDefault: MockRemoteDataSource) : RemoteDataSource

    @Reusable
    @Binds
    fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl) : LocalDataSource
}