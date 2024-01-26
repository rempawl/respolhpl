package com.rempawl.respolhpl.di

import android.content.Context
import androidx.room.Room
import com.rempawl.respolhpl.data.sources.local.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDataBase =
        Room
            .databaseBuilder(context, AppDataBase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCartDao(db: AppDataBase) = db.cartDao()
}