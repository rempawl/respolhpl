package com.example.respolhpl.cart.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartProductEntity(
    @PrimaryKey() val id: Int,
    val name: String,
    val price: Double,
    val thumbnailSrc: String?,
    val quantity: Int,
    val cost : Double = price*quantity
//    val shipping: Shipping
) {
    companion object{
    }
}
