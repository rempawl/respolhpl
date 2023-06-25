package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cacheImagesUseCase: CacheImagesUseCase
) : ActionResultUseCase<Int, Product>() {

    override suspend fun doWork(parameter: Int): Product {
        val product = remoteDataSource.getProductById(parameter)
        withContext(Dispatchers.IO) { cacheImagesUseCase(product.images) }
        return product.toDomain()
    }
}
