package com.rempawl.respolhpl.data.store

interface TimeProvider {
    fun currentTimeMillis(): Long
}

class SystemTimeProvider : TimeProvider {
    override fun currentTimeMillis() = System.currentTimeMillis()
}
