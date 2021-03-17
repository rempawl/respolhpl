package com.example.respolhpl.data.sources.local

import androidx.room.TypeConverter
import com.example.respolhpl.data.product.ProductCategory

object Converters {
    @TypeConverter
    fun fromCategoryToInt(category: ProductCategory): Int = category.toCategoryID()


    @TypeConverter
    fun fromIntToCategory(category: Int): ProductCategory = ProductCategory.from(category)

    const val ANTIBAC_CUTTING_BOARD = 1
    const val LAMINATHPL = 2
    const val MODERNBOX = 3
}