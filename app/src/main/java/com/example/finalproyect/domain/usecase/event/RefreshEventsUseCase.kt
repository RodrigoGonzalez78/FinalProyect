package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.repository.EventRepository
import javax.inject.Inject

class RefreshEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return eventRepository.refreshEvents()
    }
}