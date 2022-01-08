package com.example.respolhpl.utils.extensions

import android.view.View
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

@ExperimentalCoroutinesApi
fun View.clicks(): Flow<Unit> = callbackFlow {
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
}