package com.rempawl.respolhpl.data.model.database

import androidx.room.Entity
import com.rempawl.respolhpl.data.model.domain.details.ProductType

@Entity(tableName = "cart_products", primaryKeys = ["id", "variantId"])
data class CartProductEntity(
    val id: Int,
    val variantId: Int,
    val quantity: Int,
    val type: ProductType,
)
