package com.example.respolhpl.data.sources

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.network.RespolApi
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class DefaultRemoteDataSource @Inject constructor(private val api : RespolApi): RemoteDataSource {
    override fun getAllProductsAsync(): Deferred<RemoteProduct> =        api.getAllProducts()



}