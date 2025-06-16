package com.example.finalproyect.data.mappers


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.TicketTypeEntity
import com.example.finalproyect.data.remote.dto.TicketTypeDto
import com.example.finalproyect.domain.model.TicketType
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
fun TicketTypeDto.toTicketType(): TicketType {
    return TicketType(
        id = idTicketType,
        eventId = idEvent,
        name = name,
        description = description,
        available = available,
        sold = sold,
        price = price,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TicketTypeDto.toTicketTypeEntity(): TicketTypeEntity {
    return TicketTypeEntity(
        id = idTicketType,
        eventId = idEvent,
        name = name,
        description = description,
        available = available,
        sold = sold,
        price = price,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

fun TicketTypeEntity.toTicketType(): TicketType {
    return TicketType(
        id = id,
        eventId = eventId,
        name = name,
        description = description,
        available = available,
        sold = sold,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
