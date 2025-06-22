package com.example.finalproyect.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.TicketEntity
import com.example.finalproyect.data.remote.dto.TicketDto
import com.example.finalproyect.data.remote.dto.response.PurchaseTicketResponse
import com.example.finalproyect.domain.model.PurchaseResult
import com.example.finalproyect.domain.model.Ticket
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
fun TicketDto.toTicket(): Ticket {
    return Ticket(
        id = idTicket,
        ticketTypeId = idTicketType,
        userId = idUser,
        qrCode = qrCode,
        price = price,
        entryNumber = entryNumber,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TicketDto.toTicketEntity(): TicketEntity {
    return TicketEntity(
        id = idTicket,
        ticketTypeId = idTicketType,
        userId = idUser,
        qrCode = qrCode,
        price = price,
        entryNumber = entryNumber,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

fun TicketEntity.toTicket(): Ticket {
    return Ticket(
        id = id,
        ticketTypeId = ticketTypeId,
        userId = userId,
        qrCode = qrCode,
        price = price,
        entryNumber = entryNumber,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun PurchaseTicketResponse.toPurchaseResult(): PurchaseResult {
    return PurchaseResult(
        message = message,
        ticket = ticket.toTicket()
    )
}
