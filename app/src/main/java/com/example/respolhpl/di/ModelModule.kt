package com.example.respolhpl.di

import com.example.respolhpl.productDetails.CartModel
import com.example.respolhpl.productDetails.CartModelImpl
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPage
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ModelModule {
    @Binds
    @ViewModelScoped
    abstract fun provideCurrentPageState(impl: CurrentViewPagerPageImpl): CurrentViewPagerPage

    companion object {
        @Provides
        @ViewModelScoped
        fun provideCartModel(): CartModel = CartModelImpl()
    }
}