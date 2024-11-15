package com.rempawl.respolhpl

import com.rempawl.respolhpl.data.usecase.FlowResultUseCase
import com.rempawl.respolhpl.data.usecase.FlowUseCase
import com.rempawl.respolhpl.utils.ProgressSemaphore
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.watchProgress
import kotlinx.coroutines.flow.Flow

abstract class FlowResultStateCaseWithProgress<Param : Any, Result : Any, State : Any>(
    useCase: FlowResultUseCase<Param, Result>,
    progressSemaphore: ProgressSemaphore,
) : FlowStateCaseWithProgress<Param, EitherResult<Result>, State>(useCase, progressSemaphore)

abstract class FlowStateCaseWithProgress<Param : Any, Result : Any, State : Any>(
    private val useCase: FlowUseCase<Param, Result>,
    private val progressSemaphore: ProgressSemaphore,
) : FlowStateCase<Param, Result, State> {

    override fun call(param: Param, stateProvider: () -> State): Flow<State> =
        useCase.call(param)
            .watchProgress(progressSemaphore)
            .transformToState(stateProvider())

    protected abstract fun Flow<Result>.transformToState(state: State): Flow<State>
}

interface StateCase<Param : Any, Result : Any, State : Any> {
    fun call(param: Param, stateProvider: () -> State): State
}

interface FlowStateCase<Param : Any, Result : Any, State : Any> {
    fun call(param: Param, stateProvider: () -> State): Flow<State>
}

interface AsyncStateCase<Param : Any, Result : Any, State : Any> {
    suspend fun call(param: Param, stateProvider: () -> State): State
}
