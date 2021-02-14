package com.example.respolhpl.data.product

import android.view.Display

sealed class ProductCategory{

    object AntibacCuttingBoard : ProductCategory()
    object LaminatHPL : ProductCategory()
    object ModernBox : ProductCategory()
    companion object{
        const val ANTIBAC_CUTTING_BOARD = 1
        const val LAMINATHPL =2
        const val MODERNBOX = 3
            fun from(entityCategory : Int) : ProductCategory = when(entityCategory){
                ANTIBAC_CUTTING_BOARD -> AntibacCuttingBoard
                LAMINATHPL -> LaminatHPL
                MODERNBOX -> ModernBox
                else -> throw IllegalStateException("wrong value of entity category")
            }


    }
}