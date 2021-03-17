package com.example.respolhpl.data.sources.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.data.Result as Result

abstract class NetworkBoundResult<ResultType, RequestType>
constructor(private val dispatchersProvider: DispatchersProvider) {

    private val result = MediatorLiveData<Result<ResultType>>()

    init {
//        result.value = Result.Loading()
//        @Suppress("LeakingThis")
//        val dbSource = loadFromDb()
//        result.addSource(dbSource) { data ->
//            result.removeSource(dbSource)
//            if (shouldFetch(data)) {
//                fetchFromNetwork(dbSource)
//            } else {
//                result.addSource(dbSource) { newData ->
//                    setValue(Result.Success(newData))
//                }
//            }
//        }
    }

    private fun setValue(newValue: Result<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
//        result.addSource(dbSource) { newData ->
//            setValue(Result.loading(newData))
//        }
//        result.addSource(apiResponse) { response ->
//            result.removeSource(apiResponse)
//            result.removeSource(dbSource)
/*
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Result.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Result.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Result.error(response.errorMessage, newData))
                    }
                }
            }
*/
        }


    protected open fun onFetchFailed() { }

    fun asLiveData() = result as LiveData<Result<ResultType>>

   protected open fun processResponse(response: RequestType) {}

    protected abstract fun saveCallResult(item: RequestType)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): LiveData<ResultType>

    protected abstract fun createCall(): Result<*>

}
