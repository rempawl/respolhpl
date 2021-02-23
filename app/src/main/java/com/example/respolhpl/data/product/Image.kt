package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.remote.ImageRemote

data class Image(
    val src: String
) {
    companion object {
        fun from(remote: ImageRemote): Image {
            return Image(remote.src)
        }
    }
}
