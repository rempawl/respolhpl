package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ImageProductJoin

@Dao
interface ImageProductJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ImageProductJoin)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<ImageProductJoin>)

    @Transaction
    @Query("SELECT * FROM images_with_products")
    suspend fun getAllJoins(): List<ImageProductJoin>
}
