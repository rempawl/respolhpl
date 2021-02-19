package com.example.respolhpl.data.sources

import com.example.respolhpl.data.product.entity.ProductEntity

interface LocalDataSource {
    fun getProducts() : List<ProductEntity>
}