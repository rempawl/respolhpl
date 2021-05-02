package com.example.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.respolhpl.data.product.entity.ProductIdEntity

@Dao
interface ProductIdsDao   {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductIdEntity)
}