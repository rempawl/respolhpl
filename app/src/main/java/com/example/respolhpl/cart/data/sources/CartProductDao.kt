package com.example.respolhpl.cart.data.sources

import androidx.room.Dao
import androidx.room.Query
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.data.sources.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao : BaseDao<CartProductEntity> {
    @Query("SELECT * FROM cart")
    fun getCartProducts(): Flow<List<CartProductEntity>>

    @Query("SELECT * FROM cart WHERE id == :id")
    fun getCartProductById(id: Int) : Flow<CartProductEntity?>
}

