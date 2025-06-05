package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.PaginatedEvents
import com.example.finalproyect.domain.repository.EventRepository
import javax.inject.Inject

class GetUserEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        size: Int = 10
    ): Result<PaginatedEvents> {
        return eventRepository.getUserEvents(page, size)
    }
}