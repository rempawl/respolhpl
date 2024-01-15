package com.rempawl.respolhpl.data.model.domain

import androidx.annotation.Keep
import com.rempawl.respolhpl.data.model.remote.ImageRemote
import java.io.Serializable

@kotlinx.serialization.Serializable
@Keep
data class ProductImage(
    val src: String,
    val id: Int
) : Serializable {
    companion object {
        fun from(remote: ImageRemote): ProductImage {
            return ProductImage(remote.src, remote.id)
        }
    }
}

@Keep
@kotlinx.serialization.Serializable
data class Images(val images: List<ProductImage>) : Serializable
