package com.example.respolhpl.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.respolhpl.data.product.Converters
import com.example.respolhpl.data.product.entity.ProductEntity

@Database(version = 3, entities = [ProductEntity::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        const val DB_NAME = "RespolHPLDataBase"

        fun from(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}
