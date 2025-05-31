package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Ticket(
    val id: Long,
    val ticketTypeId: Long,
    val userId: Long,
    val qrCode: String,
    val price: Double,
    val entryNumber: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)