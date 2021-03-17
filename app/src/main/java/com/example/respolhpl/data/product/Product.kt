package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal

data class ProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val thumbnailSrc: String?
) {
    companion object {
        fun from(remoteProduct: RemoteProductMinimal) = ProductMinimal(
            id = remoteProduct.id,
            name = remoteProduct.name,
            price = remoteProduct.price,
            thumbnailSrc = remoteProduct.images.first().src,
        )
    }
}

data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
//    val shipping: Shipping,
    val images: List<Image>,
//    val productCategories: List<ProductCategory>, todo
    val thumbnailSrc: String?,
    val price: Double,
    val description: String
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
