package com.example.respolhpl.utils.event

import androidx.lifecycle.Observer

class EventObserver<T>(private val handler : (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let{
            handler(it)
        }
    }
}