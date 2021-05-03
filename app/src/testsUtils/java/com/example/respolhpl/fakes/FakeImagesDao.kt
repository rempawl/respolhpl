package com.example.respolhpl.fakes

import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ProductWithImages
import com.example.respolhpl.data.sources.local.ImagesDao
import kotlinx.coroutines.flow.Flow

class FakeImagesDao : ImagesDao {
//    val images =  mutableListOf()

    override fun getProductImages(pId: Int): Flow<ProductWithImages?> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(item: ImageEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(items: List<ImageEntity>) {
        TODO("Not yet implemented")
    }
}