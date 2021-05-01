package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ProductIdEntity
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl.Companion.TIMEOUT
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

interface ImagesSource {
    suspend fun getImages(prodId: Int, onEmpty: suspend () -> List<Image>): List<Image>
}


class ImagesSourceImpl @Inject constructor(
    private val productIdsDao: ProductIdsDao, private val imagesDao: ImagesDao,
    private val mapper: NullableInputListMapper<ImageEntity, Image>
) : ImagesSource {
    override suspend fun getImages(prodId: Int, onEmpty: suspend () -> List<Image>): List<Image> {

        var res = mapper.map(imagesDao.getProductImages(prodId).first()?.images ?: emptyList())

        if (res.isEmpty()) {
            res = withTimeout(TIMEOUT) { onEmpty() }
            saveImagesInDb(res, prodId)
        }
        return res
    }

    private suspend fun saveImagesInDb(res: List<Image>, prodId: Int) {
        val imgs = res.map { ImageEntity(productId = prodId, src = it.src) }
        imagesDao.insert(imgs)
        productIdsDao.insert(ProductIdEntity(prodId))
    }
}

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val imagesSource: ImagesSource,
//    private val localDataSource: ProductDao, todo favs
    private val dispatchersProvider: DispatchersProvider,
    private val productsPagerFactory: ProductsPagerFactory
) : ProductRepository {

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return productsPagerFactory.create().flow
    }

    override suspend fun getProductById(id: Int): Flow<Result<*>> = getDataAsFlow {
        try {
            val res = getDataWithTimeout { remoteDataSource.getProductById(id) }
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getProductImages(id: Int): Flow<Result<*>> = getDataAsFlow {
        try {
            val res = imagesSource.getImages(prodId = id, onEmpty = {
                remoteDataSource.getProductById(id).images.map { Image.from(it) } //todo mapper
            })

            Result.Success(res)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun getDataAsFlow(getter: suspend () -> Result<*>) =
        flow {
            emit(getter())
        }.flowOn(dispatchersProvider.io)


    private suspend fun <T> getDataWithTimeout(getter: suspend () -> T): T {
        return withTimeout(TIMEOUT) {
            getter()
        }
    }

    companion object {
        const val TIMEOUT: Long = 10_000
    }

}
