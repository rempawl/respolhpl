package com.example.respolhpl.data.sources.local

import androidx.room.*
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ProductWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Transaction
    @Query("SELECT * FROM products_ids WHERE productId = :pId")
    fun getProductImages(pId: Int): Flow<ProductWithImages?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<ImageEntity>)
}