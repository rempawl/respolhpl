package com.example.respolhpl.data.product.entity

import androidx.room.*

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId : Int,
    val src: String
)

