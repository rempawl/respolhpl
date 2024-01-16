package com.rempawl.respolhpl.data.usecase

import arrow.core.raise.either
import arrow.core.right
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

interface AsyncUseCase<in Param, out Output> {
    suspend fun call(parameter: Param): Output
}

interface ResultUseCase<in Param, out Output> : AsyncUseCase<Param, EitherResult<Output>>

abstract class ActionResultUseCase<in Param, out Output> : ResultUseCase<Param, Output> {
    override suspend fun call(parameter: Param): EitherResult<Output> = either {
        doWork(parameter)
    }

    protected abstract suspend fun doWork(parameter: Param): Output
}

interface UseCase<in Param, out Output> {
    fun call(parameter: Param): Output
}

interface FlowUseCase<in Param, out Output> : UseCase<Param, Flow<Output>>

interface FlowResultUseCase<in Param, out Output> : FlowUseCase<Param, EitherResult<Output>>

abstract class ActionFlowResultUseCase<in Param, out Output> : FlowResultUseCase<Param, Output> {
    override fun call(parameter: Param): Flow<EitherResult<Output>> {
        return flow { emit(doWork(parameter).right() as EitherResult<Output>) }
            .catch { error -> emit(error.toResult()) }
    }

    protected abstract suspend fun doWork(parameter: Param): Output
}

suspend operator fun <Output> AsyncUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> AsyncUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> ResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> ResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> ActionResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> ActionResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

operator fun <Output> UseCase<Unit, Output>.invoke() = call(Unit)
operator fun <Param, Output> UseCase<Param, Output>.invoke(param: Param) =
    call(param)

operator fun <Output> FlowUseCase<Unit, Output>.invoke() = call(Unit)
operator fun <Param, Output> FlowUseCase<Param, Output>.invoke(param: Param) =
    call(param)

operator fun <Output> FlowResultUseCase<Unit, Output>.invoke() = call(Unit)
operator fun <Param, Output> FlowResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

operator fun <Output> ActionFlowResultUseCase<Unit, Output>.invoke() = call(Unit)
operator fun <Param, Output> ActionFlowResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)
