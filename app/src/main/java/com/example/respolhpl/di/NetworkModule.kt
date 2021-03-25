package com.example.respolhpl.di

import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.remote.BasicAuthInterceptor
import com.example.respolhpl.data.sources.remote.WooCommerceApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {


    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideOKHTTPClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(CLI, PRIV))
        .build()


    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDataSource = retrofit.create(WooCommerceApi::class.java)

    private const val CLI = "ck_07ce9301bc3d1cad02dc4fe9de33eed0a2704ab0"
    private const val PRIV = "cs_c495e920282576a06ebbe89872c6469ac58df2ff"
    private const val BASE_URL = "https://respolhpl-sklep.pl"

}