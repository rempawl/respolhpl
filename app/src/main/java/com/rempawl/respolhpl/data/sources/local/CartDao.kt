package com.rempawl.respolhpl.data.sources.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.rempawl.respolhpl.data.model.database.CartProductEntity
import com.rempawl.respolhpl.data.model.domain.details.ProductType
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao : BaseDao<CartProductEntity> {

    @Update
    suspend fun update(entity: CartProductEntity)

    // todo test?
    @Query("""INSERT INTO cart_products (id, quantity, type,variantId) VALUES(:id,:quantity, :type, :variantId) ON CONFLICT (id, variantId) DO UPDATE SET quantity = quantity + :quantity""")
    suspend fun add(id: Int, quantity: Int, type: ProductType, variantId: Int)

    @Query("SELECT * FROM cart_products")
    fun getCartProducts(): Flow<List<CartProductEntity>>

    @Query("DELETE  FROM cart_products")
    suspend fun clearCart()
}