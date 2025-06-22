package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class Rol(
    val id: Long,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class OrganizerRole(val id: Int, val displayName: String) {
    MAIN_ADMIN(1, "Administrador Principal"),
    ADMIN(2, "Administrador"),
    MODERATOR(3, "Moderador"),
    COLLABORATOR(4, "Colaborador");

    companion object {
        fun fromId(id: Int): OrganizerRole? {
            return values().find { it.id == id }
        }

        fun getAvailableRoles(): List<OrganizerRole> {
            return values().filter { it != MAIN_ADMIN }
        }
    }
}
