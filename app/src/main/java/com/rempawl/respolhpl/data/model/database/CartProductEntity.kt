package com.rempawl.respolhpl.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_products")
data class CartProductEntity(
    @PrimaryKey val id: Int,
    val quantity: Int,
)
