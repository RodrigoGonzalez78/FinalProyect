package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class TicketType(
    val id: Int,
    val eventId: Int,
    val name: String,
    val description: String?,
    val available: Int,
    val sold: Int,
    val price: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)