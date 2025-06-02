package com.example.finalproyect.domain.usecase.event


import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(eventId: Long): Flow<Event?> {
        return eventRepository.getEventById(eventId)
    }
}