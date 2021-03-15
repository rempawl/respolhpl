package com.example.respolhpl.data.sources

import com.example.respolhpl.data.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getProducts() : Flow<List<ProductEntity>>
    fun getProduct(id : Int) : Flow<ProductEntity?>
}