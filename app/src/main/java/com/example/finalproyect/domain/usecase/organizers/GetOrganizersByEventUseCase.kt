package com.example.finalproyect.domain.usecase.organizers

import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.repository.OrganizerRepository
import javax.inject.Inject

class GetOrganizersByEventUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(eventId: Int): Result<List<Organizer>> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }

        return organizerRepository.getOrganizersByEvent(eventId)
    }
}