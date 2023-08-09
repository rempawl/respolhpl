package com.example.respolhpl.di

import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun provideCurrentPageState(impl: ViewPagerPageManagerImpl): ViewPagerPageManager
}
