package com.example.respolhpl.data.product.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.respolhpl.data.product.Image
import com.example.respolhpl.data.product.Shipping
import com.example.respolhpl.data.product.remote.RemoteProduct
//    todo relations
//     val categories: List<Int>,
//     val images: List<Image>,
@Entity(tableName = "products")
data class ProductEntity constructor(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val name: String,
    val price: Double,
    val quantity: Int,
    @Embedded val shipping: Shipping,
    val description: String,
) {

    companion object {
        fun from(remoteProduct: RemoteProduct): ProductEntity {

            return ProductEntity(
                id = remoteProduct.id,
                name = remoteProduct.name,
                price = remoteProduct.price,
                quantity = remoteProduct.quantity,
//                categories = remoteProduct.categories.map { cat -> ProductCategory.from(cat) },
//                images = remoteProduct.images.map { img -> Image.from(img) },
                shipping = Shipping.from(remoteProduct.shipping),
                description = remoteProduct.description
            )
        }


    }
}


