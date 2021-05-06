package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_products")
data class FavProductEntity constructor(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val price: Double,
    val thumbnailSrc: String
//    val isLiked : Boolean= true
) {
    companion object
}



