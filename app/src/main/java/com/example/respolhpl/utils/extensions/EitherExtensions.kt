package com.example.respolhpl.utils.extensions

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.cancellation.CancellationException

typealias EitherResult<T> = Either<DefaultError, T>

fun <T> Throwable.toResult(): EitherResult<T> = this.toDefaultError().left()

inline fun <T> EitherResult<T>.onSuccess(block: (T) -> Unit): EitherResult<T> =
    onRight(block)

inline fun <T> EitherResult<T>.onError(block: (DefaultError) -> Unit): EitherResult<T> =
    onLeft(block)

fun <T> Flow<EitherResult<T>>.onSuccess(block: suspend (T) -> Unit): Flow<EitherResult<T>> {
    return this.onEach { result ->
        result.onSuccess { block(it) }
    }
}

fun <T> Flow<EitherResult<T>>.onError(block: suspend (DefaultError) -> Unit): Flow<EitherResult<T>> {
    return this.onEach { result ->
        result.onError { block(it) }
    }
}

inline fun <R> runAsResult(block: () -> R): EitherResult<R> {
    return try {
        block().right()
    } catch (e: Throwable) {
        if (e !is CancellationException) {
            e.toResult()
        } else {
            throw e
        }
    }
}

