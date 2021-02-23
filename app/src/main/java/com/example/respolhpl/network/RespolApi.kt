package com.example.respolhpl.network

import com.example.respolhpl.data.product.remote.RemoteProduct
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RespolApi {

    //todo woocommerce auth error

    @GET("/wp-json/wc/v3/products")
    fun getAllProducts(): Deferred<List<RemoteProduct>>

    companion object
}
