package com.example.smartfarmtracker.model

import com.google.firebase.Timestamp

data class SalesEntry(
    var id: String = "",
    var dateOfSale: Timestamp? = null,
    var animalId: String = "",
    var animalType: String = "",
    var produceType: String = "",
    var quantity: Double = 0.0,
    var unitPrice: Double = 0.0,
    var totalAmount: Double = 0.0,
    var notes: String = "",
    var createdAt: Long = 0L
)
