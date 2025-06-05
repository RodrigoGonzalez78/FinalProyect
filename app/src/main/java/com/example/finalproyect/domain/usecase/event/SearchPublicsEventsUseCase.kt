package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.PaginatedEvents
import com.example.finalproyect.domain.repository.EventRepository
import javax.inject.Inject

class SearchPublicEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        name: String,
        page: Int = 1,
        size: Int = 10
    ): Result<PaginatedEvents> {
        if (name.isBlank()) {
            return Result.failure(Exception("Search name cannot be empty"))
        }
        return eventRepository.searchPublicEvents(name, page, size)
    }
}