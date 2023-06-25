package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.PagingSource
import com.example.respolhpl.data.model.domain.ProductMinimal

abstract class ProductPagingSource : PagingSource<Int, ProductMinimal>()