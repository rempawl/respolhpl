package com.rempawl.respolhpl.utils.extensions

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <T1, T2, R> zipAsync(
    firstCaller: suspend () -> T1,
    secondCaller: suspend () -> T2,
    mapper: (T1, T2) -> R
): R = coroutineScope {
    val firstCall = async {
        firstCaller()
    }
    val secondCall = async {
        secondCaller()
    }
    val firstResult = firstCall.await()
    val secondResult = secondCall.await()

    mapper(firstResult, secondResult)
}
