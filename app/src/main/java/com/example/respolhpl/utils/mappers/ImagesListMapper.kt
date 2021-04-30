package com.example.respolhpl.utils.mappers

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.remote.ImageRemote

class ImagesListMapper : ListMapper<ImageRemote, Image> {
    override fun map(src: List<ImageRemote>): List<Image> {
        return src.map { Image.from(it) }
    }
}

