package com.example.respolhpl.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State>(initialState: State) : ViewModel() {

    val progress = ProgressCounter()

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected fun setState(update: State.() -> State) {
        _state.update { it.update() }
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
}

