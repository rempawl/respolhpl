package com.example.respolhpl.utils.mappers

import com.example.respolhpl.utils.mappers.implementations.ImageRemoteToDomainMapper
import org.junit.Assert.*
import org.junit.Test

class ListMapperImplTest{
    lateinit var mapper : ListMapperImpl<*,*>

    @Test
    fun imageRemoteToDomainMapper() {
        mapper = ListMapperImpl(ImageRemoteToDomainMapper())

    }

}