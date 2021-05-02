package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ProductIdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductIdsDao   {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductIdEntity)

    @Query("SELECT * FROM products_ids")
    fun getAllIds() : Flow<List<ProductIdEntity>>
}
