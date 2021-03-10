package com.example.respolhpl.data.product.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.respolhpl.data.product.Image
import com.example.respolhpl.data.product.remote.RemoteProduct

@Entity(tableName = "products")
data class ProductEntity  constructor(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name : String,
    val price : Double,
    val category : Int,
    @Embedded val images : List<Image>,

) {

    companion object{
        fun from(remoteProduct: RemoteProduct) : ProductEntity{
            TODO()
//            return ProductEntity()
        }

        const val ANTIBAC_CUTTING_BOARD = 1
        const val LAMINATHPL = 2
        const val MODERNBOX = 3

    }
}


