package com.example.finalproyect.domain.usecase.organizers

import com.example.finalproyect.domain.repository.OrganizerRepository

import javax.inject.Inject

class DeleteOrganizerUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        organizerId: Int
    ): Result<Unit> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (organizerId <= 0) {
            return Result.failure(Exception("Invalid organizer ID"))
        }

        // Verificar que el organizador existe
        val existingOrganizer = organizerRepository.getOrganizerById(organizerId)
        if (existingOrganizer.isFailure || existingOrganizer.getOrNull() == null) {
            return Result.failure(Exception("Organizer not found"))
        }

        val organizer = existingOrganizer.getOrNull()!!

        // Verificar que no se intente eliminar al admin principal
        if (organizer.isMainAdmin) {
            return Result.failure(Exception("Cannot delete main admin organizer"))
        }

        return organizerRepository.deleteOrganizer(
            eventId = eventId,
            organizerId = organizerId
        )
    }
}
