package com.example.mro

data class Product (
    val productId: String,
    val product: String,
    val category: String,
    val units: String,
    var profilePictureUri: String
) {
    constructor() : this("","","","","")
}