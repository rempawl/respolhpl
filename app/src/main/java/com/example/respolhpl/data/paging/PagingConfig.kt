package com.example.respolhpl.data.paging

import javax.inject.Inject

data class PagingConfig constructor(
    val prefetchSize: Int,
    val limit: Int,
) {
    @Inject
    constructor() : this(DEFAULT_PREFETCH_SIZE, DEFAULT_LIMIT)

    companion object {
        const val DEFAULT_PREFETCH_SIZE = 30
        const val DEFAULT_LIMIT = 10
    }
}
