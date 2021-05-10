package com.example.respolhpl.utils.mappers

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.utils.mappers.implementations.ImageRemoteToDomainMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert

import org.junit.Test

class ListMapperImplTest{

    @Test
    fun imageRemoteToDomainMapper() {
        val mapper = ListMapperImpl(ImageRemoteToDomainMapper())

        val imgs = listOf(ImageRemote("test",1),ImageRemote("test",2))

        val res = mapper.map(imgs)
        val exp = imgs.map { Image(it.src,it.id) }

        MatcherAssert.assertThat(res,`is`(exp))
    }

}