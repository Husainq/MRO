    package com.example.mro.data_class

    data class MRO(
        val id: String = "",
        val userId: String = "",
        val orderDate: Long = 0,
        val products: List<MROProduct> = emptyList()
    )
