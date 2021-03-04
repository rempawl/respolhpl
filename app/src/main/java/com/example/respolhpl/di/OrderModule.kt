package com.example.respolhpl.di

import com.example.respolhpl.productDetails.OrderModel
import com.example.respolhpl.productDetails.OrderModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object OrderModule {
    @Provides
    fun provideOrderModel(): OrderModel = OrderModelImpl()
}