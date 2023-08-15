package com.example.respolhpl.data.sources.local

import androidx.room.Delete

interface BaseDao<T> {
    @Delete
    suspend fun delete(entity: T)

}