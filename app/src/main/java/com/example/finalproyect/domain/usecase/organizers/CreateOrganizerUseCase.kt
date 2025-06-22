package com.example.finalproyect.domain.usecase.organizers

import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.repository.OrganizerRepository

import javax.inject.Inject

class CreateOrganizerUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        userId: Int,
        roleId: Int
    ): Result<Organizer> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (userId <= 0) {
            return Result.failure(Exception("Invalid user ID"))
        }
        if (roleId <= 0) {
            return Result.failure(Exception("Invalid role ID"))
        }
        if (roleId == 1) {
            return Result.failure(Exception("Cannot assign main admin role through this method"))
        }

        // Verificar si el usuario ya es organizador del evento
        val isAlreadyOrganizer = organizerRepository.isUserOrganizerOfEvent(eventId, userId)
        if (isAlreadyOrganizer.isSuccess && isAlreadyOrganizer.getOrNull() == true) {
            return Result.failure(Exception("User is already an organizer of this event"))
        }

        return organizerRepository.createOrganizer(
            eventId = eventId,
            userId = userId,
            roleId = roleId
        )
    }
}
