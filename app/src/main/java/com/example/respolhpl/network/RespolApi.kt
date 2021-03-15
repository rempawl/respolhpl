package com.example.respolhpl.network

import com.example.respolhpl.data.product.remote.RemoteProduct
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RespolApi {



    @GET(PRODUCTS_PATH)
    fun getAllProductsAsync(): Deferred<List<RemoteProduct>>

    @GET("$PRODUCTS_PATH/{id}")
    fun getProductByIDAsync(@Path(value = "id") id: Int): Deferred<RemoteProduct>

    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
    }
}
