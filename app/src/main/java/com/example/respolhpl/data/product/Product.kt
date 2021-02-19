package com.example.respolhpl.data.product

data class Product(
    val id: Long,
    val name: String,
    val productCategories: List<ProductCategory>,
    val price: Double
) {
    companion object {
//        fun from(entity: ProductEntity) : Product{
//            return Product(id = entity.id,todo
//            name = entity.name,
//            price = entity.price,
//            productCategories = ProductCategory.from(entityCategory = entity.category))
//        }
    }
}
