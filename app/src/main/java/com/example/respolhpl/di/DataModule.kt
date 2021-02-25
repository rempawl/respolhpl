package com.example.respolhpl.di

import com.example.respolhpl.data.sources.LocalDataSource
import com.example.respolhpl.data.sources.LocalDataSourceImpl
import com.example.respolhpl.data.sources.MockRemoteDataSource
import com.example.respolhpl.data.sources.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
interface DataModule {

    @Binds
    @Reusable
    fun provideRemoteDataSource(dataSourceDefault: MockRemoteDataSource): RemoteDataSource

    @Reusable
    @Binds
    fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}