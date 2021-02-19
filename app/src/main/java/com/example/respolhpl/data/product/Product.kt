package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.remote.RemoteProduct

data class Product(
    val id: Long,
    val name: String,
//    val productCategories: List<ProductCategory>, todo
    val thumbnailSrc: String?,
    val price: Double
) {
    companion object {
        fun from(remoteProduct: RemoteProduct): Product {
            return Product(
                id = remoteProduct.id,
                name = remoteProduct.name,
                thumbnailSrc = remoteProduct.images.first().src,
                price = remoteProduct.price
            )
        }
//        fun from(entity: ProductEntity) : Product{
//            return Product(id = entity.id,todo
//            name = entity.name,
//            price = entity.price,
//            productCategories = ProductCategory.from(entityCategory = entity.category))
//        }
    }
}
