package com.example.respolhpl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDataBase(@ApplicationContext context: Context) = AppDataBase.from(context = context)
//
//    @Provides
//    @Singleton
//    fun provideFavProductDao(dataBase: AppDataBase) = dataBase.favProductDao()
//
//    @Provides
//    @Singleton
//    fun provideCartProductDao(dataBase: AppDataBase) = dataBase.cartProductDao()
//
//    @Provides
//    @Singleton
//    fun provideImagesDao(dataBase: AppDataBase) = dataBase.imagesDao()
//
//    @Provides
//    @Singleton
//    fun provideImagesProductIdJoinDao(dataBase: AppDataBase) = dataBase.imageProductIdJoinDao()
//
//    @Provides
//    @Singleton
//    fun provideProductIdsDao(dataBase: AppDataBase) = dataBase.productIdDao()
}
