package com.example.finalproyect.domain.usecase.event

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.EventRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        eventId: Int,
        name: String,
        description: String?,
        date: LocalDateTime,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        banner: String?,
        locationId: Int,
        isPublic: Boolean
    ): Result<Event> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (name.isBlank()) {
            return Result.failure(Exception("Event name cannot be empty"))
        }
        if (locationId <= 0) {
            return Result.failure(Exception("Invalid location ID"))
        }
        if (endTime.isBefore(startTime)) {
            return Result.failure(Exception("End time cannot be before start time"))
        }

        return eventRepository.updateEvent(
            eventId = eventId,
            name = name,
            description = description,
            date = date,
            startTime = startTime,
            endTime = endTime,
            banner = banner,
            locationId = locationId,
            isPublic = isPublic
        )
    }
}