package com.example.respolhpl.data.model.domain

import com.example.respolhpl.data.model.remote.RemoteCategory.Companion.ANTIBAC_BOARD_ID
import com.example.respolhpl.data.model.remote.RemoteCategory.Companion.LAMINAT_HPL_ID
import com.example.respolhpl.data.model.remote.RemoteCategory.Companion.MODERNBOX_ID

sealed class ProductCategory {
    abstract fun toCategoryID(): Int

    object AntibacCuttingBoard : ProductCategory() {
        override fun toCategoryID(): Int = ANTIBAC_BOARD_ID
    }

    object LaminatHPL : ProductCategory() {
        override fun toCategoryID(): Int = LAMINAT_HPL_ID
    }

    object ModernBox : ProductCategory() {
        override fun toCategoryID(): Int = MODERNBOX_ID
    }

    companion object {
        fun from(categoryID: Int): ProductCategory = when (categoryID) {
            ANTIBAC_BOARD_ID -> AntibacCuttingBoard
            LAMINAT_HPL_ID -> LaminatHPL
            MODERNBOX_ID -> ModernBox
            else -> throw IllegalStateException("wrong value of categoryID")
        }
    }
}