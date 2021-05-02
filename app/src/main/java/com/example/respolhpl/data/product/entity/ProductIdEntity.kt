package com.example.respolhpl.data.product.entity

import androidx.room.*

@Entity(tableName = "products_ids")
data class ProductIdEntity(@PrimaryKey val productId: Int)
