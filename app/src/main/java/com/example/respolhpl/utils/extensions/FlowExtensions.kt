package com.example.respolhpl.utils.extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

/**
 *  Code taken from https://github.com/Kotlin/kotlinx.coroutines/issues/1446#issuecomment-625244176
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> {
    var delayJob: Job = Job().apply { complete() }
    return onCompletion { delayJob.cancel() }.run {
        flow {
            coroutineScope {
                collect { value ->
                    if (!delayJob.isActive) {
                        emit(value)
                        delayJob = launch { delay(windowDuration) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.refreshWhen(refreshFlow: SharedFlow<Unit>): Flow<T> {
    return refreshFlow
        .onSubscription { emit(Unit) }
        .flatMapLatest {
            this
        }
}