package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.repository.EventRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: Int): Result<Unit> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        return eventRepository.deleteEvent(eventId)
    }
}