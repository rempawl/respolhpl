package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class ProductRemoteToProductMapper @Inject constructor(private val imgMapper: ListMapper<ImageRemote, Image>) :
    Mapper<RemoteProduct, Product> {
    override fun map(from: RemoteProduct): Product {
        val imgs = imgMapper.map(from.images)
        return Product(
            id = from.id,
            name = from.name,
            quantity = from.stock_quantity,
            images = imgs,
            thumbnailSrc = imgs.first().src,
            price = from.price,
            description = from.description,
        )
    }
}