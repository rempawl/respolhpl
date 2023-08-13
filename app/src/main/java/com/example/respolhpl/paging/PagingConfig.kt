package com.example.respolhpl.paging

import javax.inject.Inject

data class PagingConfig constructor(
    val prefetchSize: Int,
    val perPage: Int,
) {
    @Inject
    constructor() : this(DEFAULT_PREFETCH_SIZE, DEFAULT_PAGE_SIZE)

    companion object {
        const val DEFAULT_PREFETCH_SIZE = 30
        const val DEFAULT_PAGE_SIZE = 10
    }
}
