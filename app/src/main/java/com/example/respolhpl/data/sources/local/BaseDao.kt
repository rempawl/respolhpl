package com.example.respolhpl.data.sources.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)

    @Delete
    suspend fun delete(items: List<T>) : Int
}