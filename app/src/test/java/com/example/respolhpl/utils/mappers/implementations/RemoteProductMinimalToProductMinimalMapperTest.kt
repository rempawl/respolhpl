package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import org.junit.Test

class RemoteProductMinimalToProductMinimalMapperTest {
    lateinit var mapperProductMinimal: RemoteProductMinimalToProductMinimalMapper

    @Test
    fun map() {
        mapperProductMinimal = RemoteProductMinimalToProductMinimalMapper()
        val images = listOf(ImageRemote("test", 1))
        val prod = RemoteProductMinimal(1, "test", 1.23, images)
        val res = mapperProductMinimal.map(prod)
        val exp = ProductMinimal(
            prod.id,
            name = prod.name,
            price = prod.price,
            thumbnailSrc = images.first().src,
            false
        )
    }
}