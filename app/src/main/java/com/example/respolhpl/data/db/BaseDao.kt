package com.example.respolhpl.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    fun insert( items : List<T>)

    @Insert
    fun insert(item : T)

    @Update
    fun update(item: T)

    @Delete
    fun delete(item: T)
}