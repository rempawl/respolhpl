package com.example.respolhpl.utils

import androidx.databinding.InverseMethod

object Converter {
    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(
        value: Int
    ): String {
        return value.toString()
    }

    @JvmStatic
    fun stringToInt(
        value: String
    ): Int {
        if(value.isBlank())
            return 1
        if(value.startsWith("0"))
            return 1
        return value.toInt()

    }
}