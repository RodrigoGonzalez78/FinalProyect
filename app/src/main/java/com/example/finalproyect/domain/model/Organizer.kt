package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Organizer(
    val id: Int,
    val eventId: Int,
    val roleId: Int,
    val userId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val name: String? = null,
    val lastName: String? = null
) {
    val isMainAdmin: Boolean
        get() = roleId == 1

    val fullName: String
        get() = if (name != null && lastName != null) {
            "$name $lastName"
        } else {
            "Usuario #$userId"
        }
}