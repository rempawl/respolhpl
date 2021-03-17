package com.example.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Query
import com.example.respolhpl.data.product.entity.ProductEntity
import com.example.respolhpl.data.product.entity.ProductEntityMinimal
import com.example.respolhpl.data.sources.local.BaseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ProductDao : BaseDao<ProductEntity> {

    @Query("SELECT id, name,thumbnailSrc,price FROM products")
    fun getAllProductsMinimal(): Flow<List<ProductEntityMinimal>>

    @Query("SELECT * FROM products WHERE id == :id")
     fun getProductById(id: Int): Flow<ProductEntity?>

    fun getDistinctProductById(id: Int) = getProductById(id).distinctUntilChanged()


}