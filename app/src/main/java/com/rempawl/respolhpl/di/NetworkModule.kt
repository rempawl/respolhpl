package com.rempawl.respolhpl.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moczul.ok2curl.CurlInterceptor
import com.rempawl.respolhpl.BuildConfig
import com.rempawl.respolhpl.data.sources.remote.BasicAuthInterceptor
import com.rempawl.respolhpl.data.sources.remote.WooCommerceApi
import com.rempawl.respolhpl.network.NetworkListener
import com.rempawl.respolhpl.network.NetworkListenerImpl
import com.rempawl.respolhpl.utils.log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val BASE_URL = "https://respolhpl-sklep.pl"
    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideNetworkListener(@ApplicationContext context: Context): NetworkListener =
        NetworkListenerImpl(context)

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
            log { "curl $message" }
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
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
    fun provideRemoteDataSource(retrofit: Retrofit): WooCommerceApi =
        retrofit.create(WooCommerceApi::class.java)
}
