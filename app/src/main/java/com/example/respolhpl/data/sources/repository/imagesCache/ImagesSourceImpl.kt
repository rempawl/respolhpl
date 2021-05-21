package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageProductJoin
import com.example.respolhpl.data.product.entity.ProductEntity
import com.example.respolhpl.data.sources.local.ImageProductJoinDao
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.utils.mappers.facade.MappersFacade
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


class ImagesSourceImpl @Inject constructor(
    private val productDao: ProductDao, private val imagesDao: ImagesDao,
    private val joinDao: ImageProductJoinDao,
    private val remoteDataSource: RemoteDataSource,
    private val mappers: MappersFacade
) : ImagesSource {
    companion object {
        const val TIMEOUT: Long = 10_000

    }

    override suspend fun getImages(prodId: Int): List<Image> {
        var res = mappers.imgEntityToImg.map(getImagesFromDb(prodId))

        if (res.isEmpty()) {
            res = withTimeout(TIMEOUT) { fetchImages(prodId) }
            saveImages(res, prodId)
        }
        return res
    }

    private suspend fun getImagesFromDb(prodId: Int) =
        imagesDao.getProductImages(prodId).first()?.images


    private suspend fun fetchImages(prodId: Int) =
        mappers.imgRemoteToImg.map(remoteDataSource.getProductById(prodId).images)

    override suspend fun saveImages(res: List<Image>, prodId: Int) {
        val imgs = mappers.imgToImgEntity.map(res)
        val product = ProductEntity(prodId)
        val joins = imgs.map { ImageProductJoin(prodId, it.imageId) }
        joinDao.insert(joins)
        imagesDao.insert(imgs)
        productDao.insert(product)
    }
}