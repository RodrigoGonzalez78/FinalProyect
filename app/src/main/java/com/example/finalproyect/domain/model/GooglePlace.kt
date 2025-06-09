package com.example.finalproyect.domain.model

data class GooglePlace(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val placeId: String,
    val types: List<String> = emptyList()
)