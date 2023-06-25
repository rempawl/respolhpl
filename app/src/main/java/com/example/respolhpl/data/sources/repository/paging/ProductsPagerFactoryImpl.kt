package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.respolhpl.data.model.domain.ProductMinimal
import javax.inject.Inject

class ProductsPagerFactoryImpl @Inject constructor(private val productPagingSource: ProductPagingSource) :
    ProductsPagerFactory {

    override fun create(): Pager<Int, ProductMinimal> = Pager(
        config = PagingConfig(
            pageSize = ProductPagingSourceImpl.NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { productPagingSource }
    )

}