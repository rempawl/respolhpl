package com.example.respolhpl.utils.mappers

import javax.inject.Inject

class ListMapperImpl<T, R> @Inject constructor(private val mapper: Mapper<T, R>) :
    ListMapper<T, R> {
    override fun map(from: List<T>): List<R> {
        return from.map { mapper.map(it) }
    }
}