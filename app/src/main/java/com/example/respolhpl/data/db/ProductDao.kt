package com.example.respolhpl.data.db

import androidx.room.Dao
import com.example.respolhpl.data.product.entity.ProductEntity

@Dao
interface ProductDao : BaseDao<ProductEntity> {


}