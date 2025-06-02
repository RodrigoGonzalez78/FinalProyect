package com.example.finalproyect.domain.usecase.event

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.EventRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        name: String,
        description: String,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        banner: String,
        isPublic: Boolean,
        locationName: String,
        locationDirection: String,
        locationLatitude: Double,
        locationLongitude: Double
    ): Result<Event> {
        // Validaciones
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del evento no puede estar vacío"))
        }
        if (description.isBlank()) {
            return Result.failure(IllegalArgumentException("La descripción del evento no puede estar vacía"))
        }
        if (banner.isBlank()) {
            return Result.failure(IllegalArgumentException("El banner del evento no puede estar vacío"))
        }
        if (locationName.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre de la ubicación no puede estar vacío"))
        }
        if (locationDirection.isBlank()) {
            return Result.failure(IllegalArgumentException("La dirección de la ubicación no puede estar vacía"))
        }
        if (locationLatitude == 0.0 || locationLongitude == 0.0) {
            return Result.failure(IllegalArgumentException("Las coordenadas de la ubicación no son válidas"))
        }
        if (startTime >= endTime) {
            return Result.failure(IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin"))
        }

        val dateStr = date.format(DateTimeFormatter.ISO_DATE) + "T00:00:00Z"
        val startTimeStr = date.format(DateTimeFormatter.ISO_DATE) + "T" + startTime.format(DateTimeFormatter.ISO_TIME) + "Z"
        val endTimeStr = date.format(DateTimeFormatter.ISO_DATE) + "T" + endTime.format(DateTimeFormatter.ISO_TIME) + "Z"

        return eventRepository.createEvent(
            name = name,
            description = description,
            date = dateStr,
            startTime = startTimeStr,
            endTime = endTimeStr,
            banner = banner,
            isPublic = isPublic,
            locationName = locationName,
            locationDirection = locationDirection,
            locationLatitude = locationLatitude,
            locationLongitude = locationLongitude
        )
    }
}
