package com.example.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Query
import com.example.respolhpl.data.product.entity.FavProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ProductDao : BaseDao<FavProductEntity> {


    @Query("SELECT * FROM fav_products WHERE id == :id")
     fun getProductById(id: Int): Flow<FavProductEntity?>

    fun getDistinctProductById(id: Int) = getProductById(id).distinctUntilChanged()


}