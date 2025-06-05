package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class OrganizerResponse(
    val idOrganizer: Long,
    val idEvent: Long,
    val idRol: Long,
    val idUser: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val name: String,
    val lastName: String
)