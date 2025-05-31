package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Rol(
    val id: Long,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)