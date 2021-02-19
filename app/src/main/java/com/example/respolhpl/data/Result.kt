package com.example.respolhpl.data

import java.lang.Exception

sealed class  Result<R> {
    data class  Success<T> (val data : T) : Result<T>()
    object Loading : Result<Any>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
