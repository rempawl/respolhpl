package com.example.respolhpl.data.product.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProductWithImages(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "imageId",
        entity = ImageEntity::class,
        associateBy = Junction(ImageProductJoin::class)
    ) val images: List<ImageEntity>
)