package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.remote.ImageRemote
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class ImageRemoteToImgMapperTest{
    lateinit var mapper: ImageRemoteToImgMapper
    @Test
    fun map(){
        mapper = ImageRemoteToImgMapper()
        val remote = ImageRemote("test",1)
        val res = mapper.map(remote)
        val exp = Image.from(remote)
        MatcherAssert.assertThat(res,`is`(exp))
    }
}