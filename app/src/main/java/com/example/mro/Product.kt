package com.example.mro

data class Product (
    val id: String,
    val product_name: String,
    val product_categories: String,
    val product_unit: String
) {
    constructor() : this("","","","")
}