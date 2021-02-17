package com.example.respolhpl.data.sources

import kotlinx.coroutines.Deferred
import javax.inject.Inject

class MockRemoteDataSource @Inject constructor(): RemoteDataSource {

    override fun getAllProductsAsync(): Deferred<String> {
        TODO("Not yet implemented")
    }
}