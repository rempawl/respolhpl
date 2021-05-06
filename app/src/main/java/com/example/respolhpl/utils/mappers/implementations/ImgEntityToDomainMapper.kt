package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class ImgEntityToDomainMapper @Inject constructor() : Mapper<ImageEntity, Image> {
    override fun map(from: ImageEntity): Image {
       return Image(from.src, from.imageId)
    }
}