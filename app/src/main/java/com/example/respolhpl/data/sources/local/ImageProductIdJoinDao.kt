package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ImageProductIdJoin

@Dao
interface ImageProductIdJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: ImageProductIdJoin)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(items: List<ImageProductIdJoin>)


    @Transaction
    @Query("SELECT * FROM images_with_products")
    fun getAllJoins(): List<ImageProductIdJoin>
}
