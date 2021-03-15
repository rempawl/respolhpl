package com.example.respolhpl.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.respolhpl.data.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ProductDao : BaseDao<ProductEntity> {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id == :id")
     fun getProductById(id: Int): Flow<ProductEntity?>

    fun getDistinctProductById(id: Int) = getProductById(id).distinctUntilChanged()


}