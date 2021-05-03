package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageProductIdJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ImageProductIdJoin)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<ImageProductIdJoin>)

    @Transaction
    @Query("SELECT * FROM images_with_products")
    suspend fun getAllJoins(): List<ImageProductIdJoin>
}
