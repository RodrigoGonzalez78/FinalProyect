package com.example.finalproyect.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Long,
    val name: String,
    val lastName: String,
    val birthday: LocalDate,
    val email: String,
    val phone: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null
)