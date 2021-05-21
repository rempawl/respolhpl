package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class ImgToImgEntityMapper @Inject constructor() : Mapper<Image, ImageEntity> {

    override fun map(from: Image): ImageEntity {
        return ImageEntity(imageId = from.id, src = from.src)
    }
}