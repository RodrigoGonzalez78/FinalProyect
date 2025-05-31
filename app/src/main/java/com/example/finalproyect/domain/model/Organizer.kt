package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Organizer(
    val id: Long,
    val eventId: Long,
    val rolId: Long,
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    // Información adicional del organizador
    val name: String,
    val lastName: String
)