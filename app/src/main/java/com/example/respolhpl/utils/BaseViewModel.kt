package com.example.respolhpl.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.utils.extensions.DefaultError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State>(initialState: State) : ViewModel() {

    val progress = ProgressCounter()

    private val _error = MutableSharedFlow<DefaultError>()
    val error = _error.asSharedFlow()

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected fun setState(update: State.() -> State) {
        _state.update { it.update() }
    }

    protected fun addError(error: DefaultError) {
        viewModelScope.launch {
            _error.emit(error)
        }
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

