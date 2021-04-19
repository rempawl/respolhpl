package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi : RemoteDataSource {


    @GET(PRODUCTS_PATH + STATUS)
    override suspend fun getProductsAsync(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<RemoteProductMinimal>

    @GET("$PRODUCTS_PATH/{id}")
    override suspend fun getProductByIDAsync(@Path(value = "id") id: Int): RemoteProduct


    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
        const val STATUS = "?status=publish"
    }
}
