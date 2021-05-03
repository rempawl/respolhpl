package com.example.respolhpl.di

import com.example.respolhpl.fakes.FakeRemoteDataSource
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class],
replaces = [NetworkModule::class])
interface FakeNetworkModule {
    @Binds
    @Singleton
    fun provideRemoteDataSource(source: FakeRemoteDataSource) : RemoteDataSource

}