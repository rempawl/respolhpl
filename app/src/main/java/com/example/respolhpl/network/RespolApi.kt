package com.example.respolhpl.network

import kotlinx.coroutines.Deferred
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query

interface RespolApi {

    @GET("/wp-json/wc/v3")
    fun getIndex() : Deferred<String>

    companion object{


    }
}
