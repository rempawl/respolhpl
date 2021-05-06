package com.example.respolhpl.fakes

import com.example.respolhpl.data.product.entity.ImageProductJoin
import com.example.respolhpl.data.sources.local.ImageProductJoinDao

class FakeImagesProductsJoinDao : ImageProductJoinDao {
    val items =  mutableListOf<ImageProductJoin>()
    override suspend fun insert(item: ImageProductJoin) {
        items.add(item)
    }

    override suspend fun insert(items: List<ImageProductJoin>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllJoins(): List<ImageProductJoin> {
        TODO("Not yet implemented")
    }
}