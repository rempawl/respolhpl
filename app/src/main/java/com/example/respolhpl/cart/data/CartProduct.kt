package com.example.respolhpl.cart.data

data class CartProduct(
    val id: Int,
    val name: String,
    val quantity: Int,
//    val shipping: Shipping,
    val thumbnailSrc: String?,
    val price: Double,
) {
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
