package com.example.respolhpl.di

import com.example.respolhpl.data.DataSource
import com.example.respolhpl.data.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface DataModule {
    @Binds
    fun provideDataSource(dataSource: NetworkDataSource) : DataSource

}