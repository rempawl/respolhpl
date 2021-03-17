package com.example.respolhpl.data.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.respolhpl.data.product.remote.RemoteProduct

//todo product minimal(id,name,thumb,price)
//    todo relations
//     val categories: List<Int>,
//     val images: List<Image>,
@Entity(tableName = "products")
data class ProductEntity constructor(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val thumbnailSrc: String,

//    @Embedded val shipping: Shipping,
    val description: String,
) {

    companion object {
        fun from(remoteProduct: RemoteProduct): ProductEntity {
            return ProductEntity(
                id = remoteProduct.id,
                name = remoteProduct.name,
                price = remoteProduct.price,
                quantity = remoteProduct.stock_quantity,
                thumbnailSrc = remoteProduct.images.first().src,
//                categories = remoteProduct.categories.map { cat -> ProductCategory.from(cat) },
//                images = remoteProduct.images.map { img -> Image.from(img) },
//                shipping = Shipping.from(remoteProduct.shipping),
                description = remoteProduct.description
            )
        }


    }
}


