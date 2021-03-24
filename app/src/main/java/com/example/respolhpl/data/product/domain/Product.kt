package com.example.respolhpl.data.product.domain

import com.example.respolhpl.data.product.remote.RemoteProduct

data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
//    val shipping: Shipping,
    val images: List<Image>,
//    val productCategories: List<ProductCategory>, todo
    val thumbnailSrc: String?,
    val price: Double,
    val description: String,
    val liked : Boolean = false
) {
    companion object {
        //todo from entity
        fun from(remoteProduct: RemoteProduct): Product {
            return Product(
                id = remoteProduct.id,
                name = remoteProduct.name,
                images = remoteProduct.images.map { Image.from(it) },
                thumbnailSrc = remoteProduct.images.first().src,
                price = remoteProduct.price,
                description = remoteProduct.description,
//                shipping = Shipping.from(remoteProduct.shipping),
                quantity = remoteProduct.stock_quantity
            )
        }
    }
}
