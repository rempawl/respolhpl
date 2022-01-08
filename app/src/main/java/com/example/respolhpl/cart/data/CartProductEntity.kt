package com.example.respolhpl.cart.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.respolhpl.data.product.domain.Shipping
import kotlin.reflect.jvm.internal.impl.resolve.constants.DoubleValue

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
    fun from(cartProduct: CartItem.CartProduct) = CartProductEntity(id = cartProduct.id,
        name = cartProduct.name,
        price = cartProduct.price,
        thumbnailSrc = cartProduct.thumbnailSrc,
        quantity = cartProduct.quantity
    )
    }
}
