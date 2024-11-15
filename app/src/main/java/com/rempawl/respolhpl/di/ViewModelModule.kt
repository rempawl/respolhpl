package com.rempawl.respolhpl.di

import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManagerImpl
import com.rempawl.respolhpl.utils.ProgressSemaphore
import com.rempawl.respolhpl.utils.ProgressSemaphoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun provideViewPagerPageManager(impl: ViewPagerPageManagerImpl): ViewPagerPageManager

    @Binds
    @ViewModelScoped
    abstract fun provideProgressSemaphore(impl: ProgressSemaphoreImpl): ProgressSemaphore
}
