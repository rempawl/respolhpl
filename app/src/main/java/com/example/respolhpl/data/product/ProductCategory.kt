package com.example.respolhpl.data.product

import com.example.respolhpl.data.product.Converters.ANTIBAC_CUTTING_BOARD
import com.example.respolhpl.data.product.Converters.LAMINATHPL
import com.example.respolhpl.data.product.Converters.MODERNBOX
import com.example.respolhpl.data.product.remote.RemoteCategory

sealed class ProductCategory {

    object AntibacCuttingBoard : ProductCategory()
    object LaminatHPL : ProductCategory()
    object ModernBox : ProductCategory()

    companion object {
        fun from(remoteCategory: RemoteCategory): ProductCategory = when (remoteCategory.id) {
            RemoteCategory.ANTIBAC_BOARD_ID -> AntibacCuttingBoard
            RemoteCategory.LAMINAT_HPL -> LaminatHPL
            else -> throw IllegalStateException("wrong value of remoteCategory")
        }

        fun from(entityCategory: Int): ProductCategory = when (entityCategory) {
            ANTIBAC_CUTTING_BOARD -> AntibacCuttingBoard
            LAMINATHPL -> LaminatHPL
            MODERNBOX -> ModernBox
            else -> throw IllegalArgumentException("Wrong value of category")
        }


    }
}