package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.PagingSource
import com.example.respolhpl.data.product.domain.ProductMinimal

abstract class ProductPagingSource : PagingSource<Int, ProductMinimal>()