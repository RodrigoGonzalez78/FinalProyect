package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.repository.EventRepository
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: Int): Result<EventDetail> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        return eventRepository.getEventById(eventId)
    }
}