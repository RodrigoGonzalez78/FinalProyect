package com.example.finalproyect.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.OrganizerEntity
import com.example.finalproyect.data.remote.dto.OrganizerDto
import com.example.finalproyect.domain.model.Organizer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Extension para parsear fechas ISO 8601 de forma segura
@RequiresApi(Build.VERSION_CODES.O)
private fun String?.parseIsoDateTime(): LocalDateTime {
    if (this == null) return LocalDateTime.now()
    return try {
        val cleanDateTime = this.replace("Z", "").replace("+00:00", "")
        LocalDateTime.parse(cleanDateTime, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: DateTimeParseException) {
        try {
            val cleanDateTime = this.substring(0, 19)
            LocalDateTime.parse(cleanDateTime)
        } catch (e2: Exception) {
            LocalDateTime.now()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun OrganizerDto.toOrganizer(): Organizer {
    return Organizer(
        id = idOrganizer,
        eventId = idEvent,
        roleId = idRol,
        userId = idUser,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime(),
        name = name,
        lastName = lastName
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun OrganizerDto.toOrganizerEntity(): OrganizerEntity {
    return OrganizerEntity(
        id = idOrganizer,
        eventId = idEvent,
        roleId = idRol,
        userId = idUser,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime(),
        name = name,
        lastName = lastName
    )
}

fun OrganizerEntity.toOrganizer(): Organizer {
    return Organizer(
        id = id,
        eventId = eventId,
        roleId = roleId,
        userId = userId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        lastName = lastName
    )
}