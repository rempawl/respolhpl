package com.rempawl.respolhpl.utils.extensions

import arrow.core.Either
import arrow.core.left
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.toDefaultAppError
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

typealias EitherResult<T> = Either<AppError, T>

fun <T> Throwable.toEitherResult(): EitherResult<T> = this.toDefaultAppError().left()

inline fun <T> EitherResult<T>.onSuccess(block: (T) -> Unit): EitherResult<T> =
    onRight(block)

inline fun <T> EitherResult<T>.onError(block: (AppError) -> Unit): EitherResult<T> =
    onLeft(block)

fun <T> Flow<EitherResult<T>>.onSuccess(block: suspend (T) -> Unit): Flow<EitherResult<T>> {
    return this.onEach { result ->
        result.onSuccess { block(it) }
    }
}

fun <T> Flow<EitherResult<T>>.onError(block: suspend (AppError) -> Unit): Flow<EitherResult<T>> {
    return this.onEach { result ->
        result.onError { error ->
            block(error)
        }
    }
}

fun <T, R> Flow<EitherResult<T>>.mapSuccess(mapper: suspend (T) -> R): Flow<EitherResult<R>> {
    return map { upstreamResult ->
        upstreamResult.map {
            mapper(it)
        }
    }
}
