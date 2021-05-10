package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
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