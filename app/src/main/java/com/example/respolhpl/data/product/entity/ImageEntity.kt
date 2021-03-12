package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(@PrimaryKey val src : String)

