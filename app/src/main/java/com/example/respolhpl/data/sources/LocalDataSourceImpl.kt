package com.example.respolhpl.data.sources

import com.example.respolhpl.data.db.ProductDao
import com.example.respolhpl.data.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val productDao: ProductDao) : LocalDataSource {
    override fun getProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    override fun getProduct(id : Int): Flow<ProductEntity?> = productDao.getProductById(id)

}