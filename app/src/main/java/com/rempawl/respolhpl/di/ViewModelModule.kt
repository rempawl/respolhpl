package com.rempawl.respolhpl.di

import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun provideViewPagerPageManager(impl: ViewPagerPageManagerImpl): ViewPagerPageManager
}
