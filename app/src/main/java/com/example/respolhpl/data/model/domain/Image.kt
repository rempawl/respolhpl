package com.example.respolhpl.data.model.domain

import com.example.respolhpl.data.model.remote.ImageRemote

data class Image(
    val src: String,
    val id : Int
) {
    companion object {
        fun from(remote: ImageRemote): Image {
            return Image(remote.src,remote.id)
        }
    }
}
