package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class ImgEntityToDomainMapperTest {

    lateinit var mapper: ImgEntityToDomainMapper

    @Test
    fun map() {
        mapper = ImgEntityToDomainMapper()
        val img = ImageEntity(1, "test")
        val res = mapper.map(img)
        val exp = Image(img.src, img.imageId)
        MatcherAssert.assertThat(res, `is`(exp))
    }
}