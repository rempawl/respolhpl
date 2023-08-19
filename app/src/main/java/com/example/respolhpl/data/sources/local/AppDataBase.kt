package com.example.respolhpl.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.respolhpl.data.model.database.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}

