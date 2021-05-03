package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import com.example.respolhpl.data.product.entity.ProductIdEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.sources.local.ImageProductIdJoinDao
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ImagesSourceImpl @Inject constructor(
    private val productIdsDao: ProductIdsDao, private val imagesDao: ImagesDao,
    private val joinDao: ImageProductIdJoinDao,
    private val remoteDataSource: RemoteDataSource,
    private val mapper: NullableInputListMapper<ImageEntity, Image>
) : ImagesSource {

    private val remoteMapper = object : ListMapper<ImageRemote, Image> {
        override fun map(src: List<ImageRemote>): List<Image> =
            src.map { Image(it.src, it.id) }
    }

    override suspend fun getImages(prodId: Int): List<Image> {
        var res = mapper.map(imagesDao.getProductImages(prodId).first()?.images)

        if (res.isEmpty()) {
            res = withTimeout(10_000) { fetchImages(prodId) }
            saveImages(res, prodId)
        }
        return res
    }


    private suspend fun fetchImages(prodId: Int) =
        remoteMapper.map(remoteDataSource.getProductById(prodId).images)

    override suspend fun saveImages(res: List<Image>, prodId: Int) {
        val imgs = res.map { ImageEntity(src = it.src, imageId = it.id) }
        val joins = imgs.map { ImageProductIdJoin(prodId, it.imageId) }
        joinDao.insert(joins)
        imagesDao.insert(imgs)
        productIdsDao.insert(ProductIdEntity(prodId))
    }
}