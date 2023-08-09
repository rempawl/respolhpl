package com.example.respolhpl.di

import android.content.Context
import android.util.Log
import com.example.respolhpl.BuildConfig
import com.example.respolhpl.data.sources.remote.BasicAuthInterceptor
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.remote.WooCommerceApi
import com.example.respolhpl.network.NetworkListener
import com.example.respolhpl.network.NetworkListenerImpl
import com.moczul.ok2curl.CurlInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkListener(@ApplicationContext context: Context): NetworkListener =
        NetworkListenerImpl(context)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun httpCache(@ApplicationContext context: Context): Cache {
        val httpCacheDir = File(context.filesDir, "cache")
        val httpCacheSize = (20 * 1024 * 1024 * 8L) // 20 MB
        return Cache(httpCacheDir, httpCacheSize)
    }

    @Provides
    @Singleton
    fun curlLoggingInterceptor(): CurlInterceptor {
        return CurlInterceptor { message ->
            Log.d("curl", message)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideOKHTTPClient(
        curlLoggingInterceptor: CurlInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(curlLoggingInterceptor)
        .addInterceptor(BasicAuthInterceptor(BuildConfig.API_CLIENT, BuildConfig.API_PRIVATE))
        .build()


    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDataSource =
        retrofit.create(WooCommerceApi::class.java)


    private const val BASE_URL = "https://respolhpl-sklep.pl"

}
