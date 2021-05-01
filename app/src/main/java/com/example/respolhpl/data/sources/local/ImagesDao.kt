package com.example.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ProductWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao : BaseDao<ImageEntity> {

    @Transaction
    @Query("SELECT * FROM products WHERE id = :pId")
    fun getProductImages(pId: Int): Flow<ProductWithImages?>
}