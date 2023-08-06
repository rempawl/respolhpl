package com.example.respolhpl.data.model.domain

import com.example.respolhpl.data.model.remote.ImageRemote
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Image(
    val src: String,
    val id: Int
) : Serializable {
    companion object {
        fun from(remote: ImageRemote): Image {
            return Image(remote.src, remote.id)
        }
    }
}

@kotlinx.serialization.Serializable
data class Images(val images: List<Image>) : Serializable
