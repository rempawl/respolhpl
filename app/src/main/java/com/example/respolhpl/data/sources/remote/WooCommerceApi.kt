package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface WooCommerceApi : RemoteDataSource{



    @GET("$PRODUCTS_PATH?per_page=20")
    override fun getAllProductsAsync(): Deferred<List<RemoteProductMinimal>>

    @GET("$PRODUCTS_PATH/{id}")
    override fun getProductByIDAsync(@Path(value = "id") id: Int): Deferred<RemoteProduct>

    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
    }
}
