package com.example.respolhpl.data.product

data class Product(val id : Long,
val name : String,
val productCategory: ProductCategory,
val price: Double
){
    companion object{
        fun from(entity: ProductEntity) : Product{
            return Product(id = entity.id,
            name = entity.name,
            price = entity.price,
            productCategory = ProductCategory.from(entityCategory = entity.category))
        }
    }
}
