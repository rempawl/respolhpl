package com.example.respolhpl.fakes

import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor() : RemoteDataSource {
    override suspend fun getProductById(id: Int): RemoteProduct {
        return FakeData.remoteProducts.find { it.id == id }
            ?: throw IllegalArgumentException("product not found")
    }

    override suspend fun getProducts(
        perPage: Int,
        page: Int,
        status: String
    ): List<RemoteProductMinimal> {
        TODO("Not yet implemented")
    }


}

class TimeoutFakeDataSource : RemoteDataSource {
    override suspend fun getProductById(id: Int): RemoteProduct {
        delay(ProductRepositoryImpl.TIMEOUT + 1)
        return FakeData.remoteProducts.find { it.id == id }
            ?: throw  java.lang.IllegalArgumentException("product not found")

    }

    override suspend fun getProducts(
        perPage: Int,
        page: Int,
        status: String
    ): List<RemoteProductMinimal> {
        TODO("Not yet implemented")
    }

}