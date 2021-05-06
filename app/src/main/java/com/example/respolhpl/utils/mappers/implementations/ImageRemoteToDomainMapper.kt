package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class ImageRemoteToDomainMapper @Inject constructor() : Mapper<ImageRemote, Image> {
    override fun map(from: ImageRemote): Image {
        return Image.from(from)
    }
}

