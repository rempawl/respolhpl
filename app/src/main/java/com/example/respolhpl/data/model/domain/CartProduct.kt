package com.example.respolhpl.data.model.domain

sealed class CartItem {
    abstract val id: Int

    data class CartProduct(
        override val id: Int,
        val name: String,
        val quantity: Int,
//    val shipping: Shipping,
        val thumbnailSrc: String?,
        val price: Double,
        val cost: Double = price * quantity
    ) : CartItem()

    data class Summary(override val id: Int = Int.MIN_VALUE, val cost: Double) : CartItem()
}
