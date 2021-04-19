package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartProductEntity(
    @PrimaryKey() val id: Long,
    val name: String,
    val price: Double,
    val thumbnailSrc: String,
    val quantity: Int
)
