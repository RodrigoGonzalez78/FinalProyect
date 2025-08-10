package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: Int,
    val eventId: Int,
    val title: String,
    val description: String,
    val image: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
