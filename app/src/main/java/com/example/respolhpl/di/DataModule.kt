package com.example.respolhpl.di

import com.example.respolhpl.data.sources.*
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
    fun provideRemoteDataSource(dataSource: MockRemoteDataSource): RemoteDataSource

    @Reusable
    @Binds
    fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Reusable
    @Binds
    fun provideRepository(repository: RepositoryImpl): Repository


}