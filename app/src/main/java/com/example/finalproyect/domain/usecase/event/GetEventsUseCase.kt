package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(): Flow<List<Event>> {
        return eventRepository.getAllEvents()
    }
}