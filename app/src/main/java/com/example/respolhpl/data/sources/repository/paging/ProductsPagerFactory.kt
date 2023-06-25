package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.Pager
import com.example.respolhpl.data.model.domain.ProductMinimal

interface ProductsPagerFactory {
    fun create(): Pager<Int, ProductMinimal>
}