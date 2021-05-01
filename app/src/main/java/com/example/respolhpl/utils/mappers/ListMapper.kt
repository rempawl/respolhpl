package com.example.respolhpl.utils.mappers

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import javax.inject.Inject

interface ListMapper<T, R> : Mapper<List<T>, List<R>>

class ImageEntityListMapper  @Inject constructor(): NullableInputListMapper<ImageEntity, Image> {
    override fun map(src: List<ImageEntity>?): List<Image> =
        src?.map { imageEntity ->
            Image(imageEntity.src)
        } ?: emptyList()

}