package com.example.respolhpl.data.sources

import com.example.respolhpl.data.db.ProductDao
import com.example.respolhpl.data.product.entity.ProductEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val productDao: ProductDao) : LocalDataSource {
    override fun getProducts(): List<ProductEntity> {
        TODO("Not yet implemented")
    }
}