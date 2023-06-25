package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class ImageRemoteToImgMapper @Inject constructor() : Mapper<ImageRemote, Image> {
    override fun map(from: ImageRemote): Image {
        return Image(
            src = from.src,
            id = from.id
        )
    }
}

