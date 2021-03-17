package com.example.respolhpl.di

import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
interface RepositoryModule {
    @Reusable
    @Binds
    fun provideRepository(repository: ProductRepositoryImpl): ProductRepository


}