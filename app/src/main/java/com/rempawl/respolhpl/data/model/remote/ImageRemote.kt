package com.rempawl.respolhpl.data.model.remote

import com.rempawl.respolhpl.data.model.domain.ProductImage
import kotlinx.serialization.Serializable

@Serializable
data class ImageRemote(
    val src: String, val id: Int
)

fun ImageRemote.toDomain() = ProductImage(
    src = src,
    id = id
)

fun List<ImageRemote>.toDomain(): List<ProductImage> = this.map { it.toDomain() }
