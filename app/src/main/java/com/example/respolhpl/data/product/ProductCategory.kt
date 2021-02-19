package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.entity.ProductEntity.Companion.ANTIBAC_CUTTING_BOARD
import com.example.respolhpl.data.product.entity.ProductEntity.Companion.LAMINATHPL
import com.example.respolhpl.data.product.entity.ProductEntity.Companion.MODERNBOX

sealed class ProductCategory {

    object AntibacCuttingBoard : ProductCategory()
    object LaminatHPL : ProductCategory()
    object ModernBox : ProductCategory()

    companion object {
        fun from(entityCategory: Int): ProductCategory = when (entityCategory) {
            ANTIBAC_CUTTING_BOARD -> AntibacCuttingBoard
            LAMINATHPL -> LaminatHPL
            MODERNBOX -> ModernBox
            else -> throw IllegalStateException("wrong value of entity category")
        }


    }
}