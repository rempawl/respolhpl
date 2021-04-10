package com.example.respolhpl.di

import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
interface RepositoryModule {

    @Reusable
    @Binds
    fun provideRepository(repository: ProductRepositoryImpl): ProductRepository
}