package com.example.mro

data class User (
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "",
    val password: String = "",
    var profilePictureUri: String = ""
)