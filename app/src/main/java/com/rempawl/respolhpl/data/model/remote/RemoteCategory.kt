package com.rempawl.respolhpl.data.model.remote

import kotlinx.serialization.Serializable


@Serializable
data class RemoteCategory(val id: Int) {
    companion object {
        const val ANTIBAC_BOARD_ID = 21
        const val LAMINAT_HPL_ID = 12
        const val MODERNBOX_ID = -1
    }
}