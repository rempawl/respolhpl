package com.example.respolhpl.data.product.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithImages(
    @Embedded val product: ProductIdEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId",
        entity = ImageEntity::class
    ) val images: List<ImageEntity>
)