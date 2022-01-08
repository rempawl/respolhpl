package com.example.respolhpl.cart.data

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
    ) : CartItem() {
        companion object {
            fun from(entity: CartProductEntity): CartProduct = CartProduct(
                id = entity.id,
                quantity = entity.quantity,
                name = entity.name,
                thumbnailSrc = entity.thumbnailSrc,
                price = entity.price
            )


        }
    }

    data class Summary(override val id: Int = Int.MIN_VALUE, val cost: Double) : CartItem()
}
