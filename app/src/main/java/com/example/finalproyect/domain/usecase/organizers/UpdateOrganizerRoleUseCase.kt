package com.example.finalproyect.domain.usecase.organizers

import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.repository.OrganizerRepository
import javax.inject.Inject

class UpdateOrganizerRoleUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        organizerId: Int,
        newRoleId: Int
    ): Result<Organizer> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (organizerId <= 0) {
            return Result.failure(Exception("Invalid organizer ID"))
        }
        if (newRoleId <= 0) {
            return Result.failure(Exception("Invalid role ID"))
        }
        if (newRoleId == 1) {
            return Result.failure(Exception("Cannot assign main admin role through this method"))
        }

        // Verificar que el organizador existe
        val existingOrganizer = organizerRepository.getOrganizerById(organizerId)
        if (existingOrganizer.isFailure || existingOrganizer.getOrNull() == null) {
            return Result.failure(Exception("Organizer not found"))
        }

        val organizer = existingOrganizer.getOrNull()!!

        // Verificar que no se intente cambiar el rol del admin principal
        if (organizer.isMainAdmin) {
            return Result.failure(Exception("Cannot change role of main admin"))
        }

        return organizerRepository.updateOrganizerRole(
            eventId = eventId,
            organizerId = organizerId,
            newRoleId = newRoleId
        )
    }
}
