package com.example.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.respolhpl.data.model.database.CartProductEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao : BaseDao<CartProductEntity> {

    @Update
    suspend fun update(entity: CartProductEntity)

    @Query("""INSERT INTO cart_products (id, quantity) VALUES(:id,:quantity) ON CONFLICT (id) DO UPDATE SET quantity = quantity + :quantity""")
    suspend fun add(id: Int, quantity: Int)

    @Query("SELECT * FROM cart_products")
    fun getCartProducts(): Flow<List<CartProductEntity>>

    @Query("DELETE  FROM cart_products")
    suspend fun clearCart()
}