package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ImageProductJoin
import com.example.respolhpl.data.product.entity.ProductEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.sources.local.ImageProductJoinDao
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


class ImagesSourceImpl @Inject constructor(
    private val productDao: ProductDao, private val imagesDao: ImagesDao,
    private val joinDao: ImageProductJoinDao,
    private val remoteDataSource: RemoteDataSource,
    private val entityMapper: NullableInputListMapper<ImageEntity, Image>,
    private val remoteMapper: ListMapper<ImageRemote, Image>
) : ImagesSource {

    override suspend fun getImages(prodId: Int): List<Image> {
        var res = entityMapper.map(getImagesFromDao(prodId))

        if (res.isEmpty()) {
            res = withTimeout(10_000) { fetchImages(prodId) }
            saveImages(res, prodId)
        }
        return res
    }

    private suspend fun getImagesFromDao(prodId: Int) =
        imagesDao.getProductImages(prodId).first()?.images


    private suspend fun fetchImages(prodId: Int) =
        remoteMapper.map(remoteDataSource.getProductById(prodId).images)

    override suspend fun saveImages(res: List<Image>, prodId: Int) {
        val imgs = res.map { ImageEntity(src = it.src, imageId = it.id) }
        val joins = imgs.map { ImageProductJoin(prodId, it.imageId) }
        joinDao.insert(joins)
        imagesDao.insert(imgs)
        productDao.insert(ProductEntity(prodId))
    }
}