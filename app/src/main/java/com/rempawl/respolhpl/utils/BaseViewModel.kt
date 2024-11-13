package com.rempawl.respolhpl.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Effects : Effect>(
    initialState: State,
    protected val progress: ProgressSemaphore
) : ViewModel() {


    private val _showError = MutableSharedFlow<AppError>()
    val showError = _showError.asSharedFlow()

    private val _effects = MutableSharedFlow<Effects>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val effects = _effects.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L))

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    val stateValue get() = state.value

    protected fun setState(update: State.() -> State) {
        _state.update { it.update() }
    }

    protected fun setEffect(effect: Effects) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    protected fun addError(error: AppError) {
        viewModelScope.launch {
            _showError.emit(error)
        }
    }

    fun observeProgress(
        progressSemaphore: ProgressSemaphore? = null,
        block: suspend CoroutineScope.(showProgress: Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            (progressSemaphore ?: progress)
                .hasProgress
                .collect { showProgress -> block(showProgress) }
        }
    }

    suspend fun <T> withProgress(
        block: suspend () -> T,
        progressSemaphore: ProgressSemaphore? = null,
    ): T {
        val progress = (progressSemaphore ?: progress).apply { addProgress() }
        try {
            return block()
        } finally {
            progress.removeProgress()
        }
    }

    fun <R> mapStateDistinct(mapper: (State) -> R) = state.map(mapper).distinctUntilChanged()
}

