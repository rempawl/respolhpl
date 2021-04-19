package com.example.respolhpl.cart

import androidx.room.Dao
import androidx.room.Query
import com.example.respolhpl.data.sources.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao : BaseDao<CartProductDao>{
    @Query("SELECT * FROM cart")
     fun getCartProducts() : Flow<List<CartProductEntity>>
}

