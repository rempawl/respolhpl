package com.example.respolhpl.utils

import arrow.core.left
import arrow.core.right
import com.example.respolhpl.data.usecase.AsyncUseCase
import com.example.respolhpl.data.usecase.ResultUseCase
import com.example.respolhpl.data.usecase.StoreUseCase
import com.example.respolhpl.data.usecase.invoke
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.EitherResult
import io.mockk.MockKStubScope
import io.mockk.coEvery
import io.mockk.slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified P : Any, R, T : AsyncUseCase<P, R>> T.mock(
    value: R,
    param: P? = null,
    delayMillis: Long = 0,
) = coEvery { this@mock.invoke(param ?: any()) } coAnswers {
    if (delayMillis > 0) delay(delayMillis)
    value
}

inline fun <reified Param : Any, Result, T : StoreUseCase<Param, Result>> T.mockCacheAndFreshWithInputParameters(
    delayMillis: Long = 0,
    crossinline valueFactory: (Param) -> EitherResult<Result>
) {
    val argument = slot<Param>()
    coEvery { this@mockCacheAndFreshWithInputParameters.cacheAndFresh(capture(argument)) } coAnswers {
        flow {
            delay(delayMillis)
            emit(valueFactory(argument.captured))
        }
    }
}

inline fun <FlowType, reified T : Flow<FlowType>, B : Flow<FlowType>> MockKStubScope<T, B>.mockFlow(
    delayMillis: Long? = null,
    crossinline response: () -> FlowType
) =
    answers {
        flow {
            if (delayMillis != null) delay(delayMillis)
            emit(response())
        } as T
    }

inline fun <FlowType, reified T : Flow<FlowType>, B : Flow<FlowType>> MockKStubScope<T, B>.mockFlowResult(
    delayMillis: Long? = null,
    response: FlowType,
    error: DefaultError? = null,
) =
    answers {
        flow {
            if (delayMillis != null) delay(delayMillis)
            if (error != null) {
                emit(error.left())
            } else {
                emit(response.right())
            }
        } as T
    }

inline fun <ResultType, reified T : Flow<EitherResult<ResultType>>, B : Flow<EitherResult<ResultType>>> MockKStubScope<T, B>.mockFlowResponse(
    delayMillis: Long? = null,
    crossinline response: () -> ResultType
) = mockFlow(delayMillis) { response().right() }

inline fun <ResultType, reified T : Flow<EitherResult<ResultType>>, B : Flow<EitherResult<ResultType>>> MockKStubScope<T, B>.mockFlowError(
    delayMillis: Long? = null,
    crossinline error: () -> DefaultError
) = mockFlow(delayMillis) { error().left() }


inline fun <reified Param : Any, Result, T : StoreUseCase<Param, Result>> T.mockCacheAndFresh(
    delayMillis: Long = 0,
    error: DefaultError? = null,
    value: Result,
    param: Param? = null
) {
    coEvery { this@mockCacheAndFresh.cacheAndFresh(param ?: any()) } coAnswers {
        flow {
            if (delayMillis > 0) delay(delayMillis)
            emit(error?.left() ?: value.right())
        }
    }
}
