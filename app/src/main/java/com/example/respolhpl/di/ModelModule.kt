package com.example.respolhpl.di

import com.example.respolhpl.productDetails.currentPageState.CurrentPageState
import com.example.respolhpl.productDetails.currentPageState.CurrentPageStateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ModelModule {
    @Binds
    abstract fun provideCurrentPageState(impl : CurrentPageStateImpl) : CurrentPageState
}