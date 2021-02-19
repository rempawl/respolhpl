package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity  constructor(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name : String,
    val price : Double,
    val category : Int
) {

    companion object{
        const val ANTIBAC_CUTTING_BOARD = 1
        const val LAMINATHPL = 2
        const val MODERNBOX = 3

    }
}


