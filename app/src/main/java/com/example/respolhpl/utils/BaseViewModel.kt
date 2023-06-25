package com.example.respolhpl.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class ProgressCounter {
    private val count = AtomicInteger()
    private val progressState = MutableStateFlow(count.get())

    val observeState: Flow<Boolean>
        get() = progressState.map { it > 0 }.distinctUntilChanged()

    fun addProgress() {
        progressState.value = count.incrementAndGet()
    }

    fun removeProgress() {
        progressState.value = count.decrementAndGet()
    }
}

abstract class BaseViewModel<State>(initialState: State) : ViewModel() {

    val progress = ProgressCounter()

    private val _state = MutableStateFlow<State>(initialState)
    val state = _state.asStateFlow()

    protected fun setState(update: (State) -> State) {
        _state.update { update(it) }
    }

    fun observeProgress(
        progressCounter: ProgressCounter = progress,
        block: suspend CoroutineScope.(showProgress: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            progressCounter.observeState.collect { showProgress ->
                block(showProgress)
            }
        }
    }

    suspend fun <T> withProgress(
        progressCounter: ProgressCounter,
        block: suspend () -> T
    ): T {
        progressCounter.addProgress()
        try {
            return block()
        } finally {
            progressCounter.removeProgress()
        }
    }
// todo base progress etc
//
//    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
//        callbacks.add(callback)
//    }
//
//    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
//        callbacks.remove(callback)
//    }


//    @Suppress("unused")
//    fun notifyChange() {
//        callbacks.notifyCallbacks(this, 0, null)
//    }
//
//    fun notifyPropertyChanged(fieldId: Int) {
//        callbacks.notifyCallbacks(this, fieldId, null)
//    }
}

