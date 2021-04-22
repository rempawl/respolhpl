package com.example.respolhpl.data.product.domain

import com.example.respolhpl.data.product.remote.RemoteProductMinimal

data class ProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val thumbnailSrc: String?,
    val isLiked : Boolean = false
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