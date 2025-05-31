package com.example.finalproyect.data.mappers


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.EventWithLocation
import com.example.finalproyect.data.local.entity.LocationEntity
import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.LocationDto
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.Location
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun EventDto.toEventEntity(): EventEntity {
    return EventEntity(
        id = idEvent,
        locationId = idLocation,
        date = LocalDate.parse(date.substring(0, 10)),
        startTime = LocalTime.parse(startTime.substring(11, 19)),
        endTime = LocalTime.parse(endTime.substring(11, 19)),
        name = name,
        description = description,
        banner = banner,
        isPublic = isPublic,
        createdAt = LocalDateTime.parse(createdAt.substring(0, 19)),
        updatedAt = LocalDateTime.parse(updatedAt.substring(0, 19))
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun EventDto.toLocationEntity(): LocationEntity {
    // Nota: Este método es un placeholder, ya que EventDto no contiene datos de ubicación
    // En una implementación real, necesitarías obtener los datos de ubicación de otra fuente
    return LocationEntity(
        id = idLocation,
        name = "",
        direction = "",
        latitude = 0.0,
        longitude = 0.0,
        createdAt = LocalDateTime.parse(createdAt.substring(0, 19)),
        updatedAt = LocalDateTime.parse(updatedAt.substring(0, 19))
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
        createdAt = LocalDateTime.parse(createdAt.substring(0, 19)),
        updatedAt = LocalDateTime.parse(updatedAt.substring(0, 19))
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
