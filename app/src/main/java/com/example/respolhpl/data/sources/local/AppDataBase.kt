package com.example.respolhpl.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.cart.data.sources.CartProductDao
import com.example.respolhpl.data.product.entity.FavProductEntity

@Database(version = 4, entities = [FavProductEntity::class, CartProductEntity::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun favProductDao(): FavProductDao

    abstract fun cartProductDao(): CartProductDao

    companion object {
        const val DB_NAME = "RespolHPLDataBase"

        fun from(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}
