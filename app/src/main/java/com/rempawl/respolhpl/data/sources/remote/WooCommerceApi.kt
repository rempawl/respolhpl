package com.rempawl.respolhpl.data.sources.remote

import com.rempawl.respolhpl.data.model.remote.RemoteProduct
import com.rempawl.respolhpl.data.model.remote.RemoteProductMinimal
import com.rempawl.respolhpl.data.model.remote.RemoteProductVariant
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
    suspend fun getProduct(@Path(value = "id") id: Int): RemoteProduct

    @GET("$PRODUCTS_PATH/{id}/variations")
    suspend fun getProductVariations(@Path(value ="id") id : Int) : List<RemoteProductVariant>

    @GET("${PRODUCTS_PATH}/{id}/variations/{variationId}")
    suspend fun getProductVariant(
        @Path(value = "id") id: Int,
        @Path(value = "variationId") variantId: Int
    ): RemoteProductVariant

    companion object {
        const val PRODUCTS_PATH = "/wp-json/wc/v3/products"
        const val DEFAULT_STATUS = "publish"
    }
}
