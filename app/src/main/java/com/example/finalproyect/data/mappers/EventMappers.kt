package com.example.finalproyect.data.mappers


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.EventWithLocation
import com.example.finalproyect.data.local.entity.LocationEntity
import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.LocationDto
import com.example.finalproyect.data.remote.dto.OrganizerResponseDto
import com.example.finalproyect.data.remote.dto.response.EventDetailResponse
import com.example.finalproyect.data.remote.dto.response.PaginatedEventsResponse
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.domain.model.OrganizerResponse
import com.example.finalproyect.domain.model.PaginatedEvents
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Formatters para parsing consistente
@RequiresApi(Build.VERSION_CODES.O)
private val isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

@RequiresApi(Build.VERSION_CODES.O)
private val isoDateFormatter = DateTimeFormatter.ISO_DATE

@RequiresApi(Build.VERSION_CODES.O)
private val isoTimeFormatter = DateTimeFormatter.ISO_TIME

// Extension para parsear fechas ISO 8601 de forma segura
@RequiresApi(Build.VERSION_CODES.O)
fun String.parseIsoDateTime(): LocalDateTime {
    return try {
        // Remover la Z si existe y parsear
        val cleanDateTime = this.replace("Z", "").replace("+00:00", "")
        LocalDateTime.parse(cleanDateTime, isoDateTimeFormatter)
    } catch (e: DateTimeParseException) {
        // Fallback: intentar parsear solo la parte de fecha y hora sin milisegundos
        try {
            val cleanDateTime = this.substring(0, 19) // yyyy-MM-ddTHH:mm:ss
            LocalDateTime.parse(cleanDateTime)
        } catch (e2: Exception) {
            throw IllegalArgumentException("Cannot parse datetime: $this", e2)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.parseIsoDate(): LocalDate {
    return try {
        // Extraer solo la parte de fecha si viene con tiempo
        val dateOnly = if (this.contains("T")) this.substring(0, 10) else this
        LocalDate.parse(dateOnly, isoDateFormatter)
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Cannot parse date: $this", e)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.parseIsoTime(): LocalTime {
    return try {
        // Extraer solo la parte de tiempo si viene con fecha
        val timeOnly = if (this.contains("T")) {
            this.substring(11).replace("Z", "").replace("+00:00", "")
        } else {
            this.replace("Z", "").replace("+00:00", "")
        }
        // Remover milisegundos si existen
        val cleanTime = if (timeOnly.contains(".")) {
            timeOnly.substring(0, timeOnly.indexOf("."))
        } else {
            timeOnly
        }
        LocalTime.parse(cleanTime, isoTimeFormatter)
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Cannot parse time: $this", e)
    }
}

// Mappers corregidos
@RequiresApi(Build.VERSION_CODES.O)
fun EventDto.toEventEntity(userId: String): EventEntity {
    return EventEntity(
        id = idEvent,
        locationId = idLocation?:0,
        date = date.parseIsoDate(),
        startTime = startTime.parseIsoTime(),
        endTime = endTime.parseIsoTime(),
        name = name,
        description = description,
        banner = banner,
        isPublic = isPublic,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime(),
        userId = userId
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocationDto.toLocationEntity(): LocationEntity {
    return LocationEntity(
        id = idLocation,
        name = name,
        direction = direction,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

fun LocationEntity.toLocation(): Location {
    return Location(
        id = id,
        name = name,
        direction = direction,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocationDto.toLocation(): Location {
    return Location(
        id = idLocation,
        name = name,
        direction = direction,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

fun EventEntity.toEvent(location: Location? = null): Event {
    return Event(
        id = id,
        locationId = locationId,
        date = date,
        startTime = startTime,
        endTime = endTime,
        name = name,
        description = description ?: "",
        banner = banner,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        location = location
    )
}

fun EventWithLocation.toEvent(): Event {
    return Event(
        id = event.id,
        locationId = event.locationId,
        date = event.date,
        startTime = event.startTime,
        endTime = event.endTime,
        name = event.name,
        description = event.description ?: "",
        banner = event.banner,
        isPublic = event.isPublic,
        createdAt = event.createdAt,
        updatedAt = event.updatedAt,
        location = location.toLocation()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun EventDto.toEvent(location: Location? = null): Event {
    return Event(
        id = idEvent,
        locationId = idLocation?:0,
        date = date.parseIsoDate(),
        startTime = startTime.parseIsoTime(),
        endTime = endTime.parseIsoTime(),
        name = name,
        description = description ?: "",
        banner = banner,
        isPublic = isPublic,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime(),
        location = location
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun PaginatedEventsResponse.toPaginatedEvents(): PaginatedEvents {
    return PaginatedEvents(
        page = page,
        size = size,
        events = if (events.isNullOrEmpty()) emptyList() else events.map { it.toEvent() }
    )
}

// NUEVO MAPPER para EventDetailResponse
@RequiresApi(Build.VERSION_CODES.O)
fun EventDetailResponse.toEventDetail(): EventDetail {
    return try {
        // Crear el objeto Location
        val location = this.location?.toLocation() ?: Location(
            id = (this.idLocation ?: 0).toLong(),
            name = "Unknown Location",
            direction = "",
            latitude = 0.0,
            longitude = 0.0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // Crear el objeto Event usando los campos directos de EventDetailResponse
        val event = Event(
            id = this.idEvent.toLong()
            ,
            locationId = (this.idLocation ?: 0).toLong(),
            date = this.date.parseIsoDate(),
            startTime = this.startTime.parseIsoTime(),
            endTime = this.endTime.parseIsoTime(),
            name = this.name ?: "",
            description = this.description ?: "",
            banner = this.banner,
            isPublic = this.isPublic ?: false,
            createdAt = this.createdAt.parseIsoDateTime(),
            updatedAt = this.updatedAt.parseIsoDateTime(),
            location = location
        )

        // Crear la lista de organizadores
        val organizers = this.organizers.map { it.toOrganizerResponse() } ?: emptyList()

        EventDetail(
            event = event,
            location = location,
            organizers = organizers
        )
    } catch (e: Exception) {
        throw IllegalArgumentException("Error mapping EventDetailResponse to EventDetail: ${e.message}", e)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun OrganizerResponseDto.toOrganizerResponse(): OrganizerResponse {
    return OrganizerResponse(
        idOrganizer = idOrganizer,
        idEvent = idEvent,
        idRol = idRol,
        idUser = idUser,
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime(),
        name = name,
        lastName = lastName
    )
}
