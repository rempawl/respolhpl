package com.example.respolhpl.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.cart.data.sources.CartProductDao
import com.example.respolhpl.data.product.entity.*

@Database(
    version = 8,
    entities = [FavProductEntity::class, CartProductEntity::class, ProductIdEntity::class,
        ImageEntity::class, ImageProductIdJoin::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favProductDao(): FavProductDao

    abstract fun imageProductIdJoinDao() : ImageProductIdJoinDao

    abstract fun cartProductDao(): CartProductDao

    abstract fun imagesDao(): ImagesDao

    abstract fun productIdDao(): ProductIdsDao

    companion object {
        const val DB_NAME = "RespolHPLDataBase"

        fun from(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}
