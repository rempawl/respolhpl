package com.example.respolhpl.data

sealed class Result<R> {
    data class Success<T>(val data: T) : Result<T>()
    object Loading : Result<Any>()
    data class Error(val exception: Exception) : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success
    val isError: Boolean
        get() = this is Error
    val isLoading: Boolean
        get() = this is Loading
}
