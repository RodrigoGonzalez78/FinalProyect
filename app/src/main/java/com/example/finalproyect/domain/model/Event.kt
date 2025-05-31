package com.example.finalproyect.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Event(
    val id: Long,
    val locationId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val name: String,
    val description: String,
    val banner: String?,
    val isPublic: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    // Relación con Location (para facilitar el uso en la UI)
    val location: Location? = null
)