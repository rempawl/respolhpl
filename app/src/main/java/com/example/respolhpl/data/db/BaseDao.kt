package com.example.respolhpl.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert( items : List<T>)

    @Insert
    suspend fun insert(item : T)

    @Update
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)
}