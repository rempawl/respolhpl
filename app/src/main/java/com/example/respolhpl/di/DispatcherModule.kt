package com.example.respolhpl.di

import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.DispatchersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DispatcherModule {
    @Binds
    @Reusable
    fun provideDispatchersProvider(provider: DispatchersProviderImpl): DispatchersProvider
}