package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Location(
    val id: Long,
    val name: String,
    val direction: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)