package com.example.smartfarmtracker.model

data class SalesRecord(
    val id: String = "",
    val animalType: String = "",
    val dateOfSale: String = "",
    val produceType: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val totalAmount: Double = 0.0,
    val buyerName: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
