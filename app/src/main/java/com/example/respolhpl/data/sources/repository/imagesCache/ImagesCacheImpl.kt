package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import com.example.respolhpl.data.sources.local.ImageProductIdJoinDao
import com.example.respolhpl.data.product.entity.ProductIdEntity
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ImagesCacheImpl @Inject constructor(
    private val productIdsDao: ProductIdsDao, private val imagesDao: ImagesDao,
    private val joinDao: ImageProductIdJoinDao,
    private val mapper: NullableInputListMapper<ImageEntity, Image>
) : ImagesCache {

    override suspend fun getImages(
        prodId: Int,
        onEmpty: suspend () -> List<Image>
    ): List<Image> {

        var res = mapper.map(imagesDao.getProductImages(prodId).first()?.images)

        if (res.isEmpty()) {
            res = withTimeout(ProductRepositoryImpl.TIMEOUT) { onEmpty() }
            saveImages(res, prodId)
        }
        return res
    }

    override suspend fun saveImages(res: List<Image>, prodId: Int) {
        val imgs = res.map { ImageEntity(src = it.src, imageId = it.id) }
        val joins = imgs.map { ImageProductIdJoin(prodId, it.imageId) }
        joinDao.insert(joins)
        imagesDao.insert(imgs)
        productIdsDao.insert(ProductIdEntity(prodId))
    }
}