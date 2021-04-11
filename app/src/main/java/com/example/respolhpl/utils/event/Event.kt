package com.example.respolhpl.utils.event

class Event<T>( private val content : T) {
    private var wasHandled : Boolean = false

    fun getContentIfNotHandled() : T?{
        if(wasHandled)
            return null

        wasHandled = true
        return content
    }
}