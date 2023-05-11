package com.example.respolhpl.bindingAdapters


object Converter {
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
