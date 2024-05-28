package com.example.mro

data class Project (
    val projectId: String,
    val project: String,
    val area: String,
    val admin_project: String,
    val project_manager: String,
    val procurement: String,
    val pic: String,
    val material: String,
) {
    constructor() : this("","","","","","","","")
}