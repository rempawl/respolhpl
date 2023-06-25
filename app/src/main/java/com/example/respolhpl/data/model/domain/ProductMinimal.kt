package com.example.respolhpl.data.model.domain

import com.example.respolhpl.data.model.remote.RemoteProductMinimal

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