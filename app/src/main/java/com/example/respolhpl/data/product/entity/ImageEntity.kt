package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    val productId: Int,
    @PrimaryKey(autoGenerate = true) val imageId : Long = 0,
    val src: String
)