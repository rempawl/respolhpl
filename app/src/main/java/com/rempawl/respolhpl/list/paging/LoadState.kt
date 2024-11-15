package com.rempawl.respolhpl.list.paging

import com.rempawl.respolhpl.utils.AppError

sealed class LoadState {
    object Success : LoadState()

    sealed class Loading : LoadState() {
        object LoadingMore : Loading()
        object InitialLoading : Loading()
    }

    sealed class Error : LoadState() {
        data class LoadMoreError(val appError: AppError) : Error()
        data class InitError(val appError: AppError) : Error()
    }
}
