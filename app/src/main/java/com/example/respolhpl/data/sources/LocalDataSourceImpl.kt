package com.example.respolhpl.data.sources

import com.example.respolhpl.data.db.ProductDao
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val productDao: ProductDao) : LocalDataSource {
}