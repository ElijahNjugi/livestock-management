package com.example.smartfarmtracker.model

data class Animal(
    val id: String = "",
    val name: String = "",
    val type: String = "", // cow, goat, etc.
    val breed: String = "",
    val gender: String = "",
    val age: Int = 0,
    val healthStatus: String = "",
    val lastFeedDate: String = "",
    val feedType: String = "",
    val dailyFeedQuantityKg: Float = 0f,
    val lastCheckupDate: String = "",
    val nextCheckupDate: String = "",
    val vaccinations: List<String> = emptyList(),
    val medications: List<String> = emptyList(),
    val diseases: List<String> = emptyList(),
    val weightKg: Float = 0f,
    val milkProductionLiters: Float = 0f,
    val offspring: List<String> = emptyList(),
    val status: String = "Active",
    val photoUrl: String = "",
    val location: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : java.io.Serializable
