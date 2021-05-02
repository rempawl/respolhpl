package com.example.respolhpl.data.product.entity

import androidx.room.*

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId : Int,
    val src: String
)
@Entity( tableName = "images_with_products",
    primaryKeys = ["productId","imageId"])
data class ImageProductIdJoin(val productId: Int, val imageId: Int)

@Dao
interface ImageProductIdJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: ImageProductIdJoin)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(items: List<ImageProductIdJoin>)


    @Transaction
    @Query("SELECT * FROM images_with_products")
    fun getCourses(): List<ImageProductIdJoin>
}