package com.example.respolhpl.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface Effect
object NoEffects : Effect

abstract class BaseViewModel<State, Effects : Effect>(initialState: State) : ViewModel() {

    val progress = ProgressCounter()

    private val _showError = MutableSharedFlow<DefaultError>()
    val showError = _showError.asSharedFlow()

    private val _effects = MutableSharedFlow<Effects>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effects = _effects.asSharedFlow()

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected fun setState(update: State.() -> State) {
        _state.update { it.update() }
    }

    protected fun setEffect(effect: Effects) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    protected fun addError(error: DefaultError) {
        viewModelScope.launch {
            _showError.emit(error)
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

    fun <R> mapStateDistinct(mapper: (State) -> R) = state.map(mapper).distinctUntilChanged()
}

