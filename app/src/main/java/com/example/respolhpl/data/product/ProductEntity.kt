package com.example.respolhpl.data.product

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

    }
}


