package com.example.respolhpl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.cart.data.sources.CartProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartProductDao : CartProductDao {
    private val entities: MutableList<CartProductEntity> = FakeData.cartEntities.toMutableList()
    private val ld = MutableLiveData(entities)

    override fun getCartProducts(): Flow<List<CartProductEntity>> =
        ld.asFlow()


    override fun getCartProductById(id: Int): Flow<CartProductEntity?> = flow {
        emit(entities.find { it.id == id })
    }

    override suspend fun insert(items: List<CartProductEntity>) {
        entities.addAll(items)
    }

    override suspend fun insert(item: CartProductEntity) {
        entities.add(item)

    }

    override suspend fun update(item: CartProductEntity) {
        val prod = entities.find { item.id == it.id }
        entities.remove(prod)
        entities.add(item)

    }

    override suspend fun delete(item: CartProductEntity) {
        entities.remove(item)
        ld.value = entities
    }

    override suspend fun delete(items: List<CartProductEntity>): Int {
        entities.removeAll(items)
        return 0
    }


}