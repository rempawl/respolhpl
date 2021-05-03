package com.example.respolhpl.fakes

import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import com.example.respolhpl.data.sources.local.ImageProductIdJoinDao

class FakeImagesProductsJoinDao : ImageProductIdJoinDao {
    val items =  mutableListOf<ImageProductIdJoin>()
    override suspend fun insert(item: ImageProductIdJoin) {
        items.add(item)
    }

    override suspend fun insert(items: List<ImageProductIdJoin>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllJoins(): List<ImageProductIdJoin> {
        TODO("Not yet implemented")
    }
}