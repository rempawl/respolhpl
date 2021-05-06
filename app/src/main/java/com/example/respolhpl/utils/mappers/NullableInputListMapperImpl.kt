package com.example.respolhpl.utils.mappers

import javax.inject.Inject

class NullableInputListMapperImpl<I, O> @Inject constructor(
    private val mapper: Mapper<I, O>
) : NullableInputListMapper<I, O> {
    override fun map(from: List<I>?): List<O> {
        return from?.map { mapper.map(it) }.orEmpty()
    }
}