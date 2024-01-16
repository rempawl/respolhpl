package com.rempawl.respolhpl.di

import com.rempawl.respolhpl.utils.DispatchersProvider
import com.rempawl.respolhpl.utils.DispatchersProviderImpl
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