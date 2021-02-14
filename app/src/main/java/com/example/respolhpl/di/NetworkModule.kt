package com.example.respolhpl.di

import com.example.respolhpl.network.BasicAuthInterceptor
import com.example.respolhpl.network.RespolApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

            .client(client)
            .build()

    @Provides
    fun provideOKHTTPClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(CLI, PRIV))
        .build()

    @Provides
    fun provideRespolAPI(retrofit: Retrofit): RespolApi = retrofit.create(RespolApi::class.java)

    private const val CLI = "ck_07ce9301bc3d1cad02dc4fe9de33eed0a2704ab0"
    private const val PRIV = "cs_c495e920282576a06ebbe89872c6469ac58df2ff"


    const val BASE_URL = "https://respolhpl-sklep.pl"
}