package com.rempawl.respolhpl.utils

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


interface ProgressSemaphore {
    val hasProgress: Flow<Boolean>
    fun addProgress()
    fun removeProgress()
}

@ViewModelScoped
class ProgressSemaphoreImpl @Inject constructor() : ProgressSemaphore {
    private val progressCounter = MutableStateFlow(0)

    override val hasProgress: Flow<Boolean>
        get() = progressCounter.map { it > 0 }.distinctUntilChanged()

    override fun addProgress() {
        progressCounter.update { it + 1 }
    }

    override fun removeProgress() {
        progressCounter.update { it - 1 }
    }
}

fun <T> Flow<T>.watchProgress(counter: ProgressSemaphore): Flow<T> {
    return onStart { counter.addProgress() }
        .removeProgressOnAny(counter)
}

private fun <T> Flow<T>.removeProgressOnAny(counter: ProgressSemaphore): Flow<T> {
    val decreased = AtomicBoolean(false)
    fun decreaseCounter() {
        if (decreased.compareAndSet(false, true)) {
            counter.removeProgress()
        }
    }
    return this.onEach { decreaseCounter() }
        .onCompletion { decreaseCounter() }
}