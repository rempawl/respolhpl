package com.example.respolhpl.data.product

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun fromCategoryToInt(category: ProductCategory): Int {
        return when (category) {
            ProductCategory.AntibacCuttingBoard -> ANTIBAC_CUTTING_BOARD
            ProductCategory.LaminatHPL -> LAMINATHPL
            ProductCategory.ModernBox -> MODERNBOX
        }
    }

    @TypeConverter
    fun fromIntToCategory(category: Int): ProductCategory = ProductCategory.from(category)

    const val ANTIBAC_CUTTING_BOARD = 1
    const val LAMINATHPL = 2
    const val MODERNBOX = 3
}