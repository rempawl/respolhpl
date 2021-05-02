package com.example.respolhpl.data.product.entity

import androidx.room.Entity

@Entity( tableName = "images_with_products",
    primaryKeys = ["productId","imageId"])
data class ImageProductIdJoin(val productId: Int, val imageId: Int)