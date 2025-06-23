package com.example.finalproyect.domain.model

import java.time.LocalDateTime

data class OrganizerResponse(
    val idOrganizer: Int,
    val idEvent: Int,
    val idRol: Int,  // ← Este es el campo que representa el rol
    val idUser: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val name: String,
    val lastName: String
) {
    // Propiedades de conveniencia
    val userOrganizerRole: Int
        get() = idRol  // ← Alias para mayor claridad

    val isMainAdmin: Boolean
        get() = idRol == 1

    val fullName: String
        get() = "$name $lastName"
}
