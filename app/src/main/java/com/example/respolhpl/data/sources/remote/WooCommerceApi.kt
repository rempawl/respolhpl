package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi : RemoteDataSource {


    @GET(PRODUCTS_PATH)
    override fun getProductsAsync(@Query("per_page")perPage: Int, @Query("page") page: Int): Deferred<List<RemoteProductMinimal>>

    @GET("$PRODUCTS_PATH/{id}")
    override fun getProductByIDAsync(@Path(value = "id") id: Int): Deferred<RemoteProduct>

    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
    }
}
