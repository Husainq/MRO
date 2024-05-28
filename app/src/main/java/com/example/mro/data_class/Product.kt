package com.example.mro.data_class

data class Product (
    val imageUrl: String,
    val id: String,
    val productName: String,
    val productCategories: String,
    val productUnit: String
) {
    constructor() : this("","","","","")
}