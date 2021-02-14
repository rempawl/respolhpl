package com.example.respolhpl.data

import com.example.respolhpl.network.RespolApi
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val api : RespolApi): DataSource {
    override fun getAllProductsAsync(): Deferred<String> =        api.getAllProducts()



}