package com.example.respolhpl.paging

import com.example.respolhpl.utils.extensions.DefaultError

sealed class LoadState {
    object Success : LoadState()

    sealed class Loading : LoadState() {
        object LoadingMore : Loading()
        object InitialLoading : Loading()
    }

    sealed class Error : LoadState() {
        data class LoadMoreError(val defaultError: DefaultError) : Error()
        data class InitError(val defaultError: DefaultError) : Error()
    }
}
