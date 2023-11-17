package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi {


    @GET(PRODUCTS_PATH)
    suspend fun getProducts(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("status") status: String = DEFAULT_STATUS
    ): List<RemoteProductMinimal>

    @GET("$PRODUCTS_PATH/{id}")
    suspend fun getProductById(@Path(value = "id") id: Int): RemoteProduct


    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
        const val DEFAULT_STATUS = "publish"
    }
}
