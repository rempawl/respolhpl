package com.example.respolhpl

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor() : RemoteDataSource {


    override suspend fun getProductByIDAsync(id: Int): RemoteProduct {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsAsync(perPage: Int, page: Int): List<RemoteProductMinimal> {
        return FakeData.minRemoteProds
    }


}