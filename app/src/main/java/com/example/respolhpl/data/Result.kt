package com.example.respolhpl.data

sealed class Result<R> {
    data class Success<T>(val data: T) : Result<T>()
    object Loading : Result<Any>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    inline fun <reified T> checkIfIsSuccessAndType(): T? {
        return this.takeIf { it.isSuccess }?.let { res ->
            res as Success
            check(res.data is T) { "data should be ${T::class.java}" }
            return res.data
        }
    }
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> checkIfIsSuccessAndListOf(): List<T>? {
        return this.takeIf { it.isSuccess }?.let { res ->
            res as Success
            check(res.data is List<*>) { "data should be list" }
            if(res.data.isEmpty()) return null

            check(res.data[0] is T) { "data should be ${T::class.java}" }
            return res.data as List<T>
        }
    }

    val isSuccess: Boolean
        get() = this is Success
    val isError: Boolean
        get() = this is Error
    val isLoading: Boolean
        get() = this is Loading
}
