package com.example.respolhpl.di

import android.content.Context
import com.example.respolhpl.data.sources.local.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = AppDataBase.from(context = context)

    @Provides
    @Reusable
    fun provideProductDao(dataBase: AppDataBase) = dataBase.favProductDao()

}