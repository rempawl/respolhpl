package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao   {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(products: List<ProductEntity>)

    @Query("SELECT * FROM products_ids")
    fun getAllIds() : Flow<List<ProductEntity>>
}
