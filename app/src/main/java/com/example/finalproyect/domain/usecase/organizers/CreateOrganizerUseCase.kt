package com.example.finalproyect.domain.usecase.organizers

import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.repository.OrganizerRepository

import javax.inject.Inject

class CreateOrganizerUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        email: String,  // ← Cambiado de userId a email
        roleId: Int
    ): Result<Organizer> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if (!isValidEmail(email)) {
            return Result.failure(Exception("Invalid email format"))
        }
        if (roleId <= 0) {
            return Result.failure(Exception("Invalid role ID"))
        }
        if (roleId == 1) {
            return Result.failure(Exception("Cannot assign main admin role through this method"))
        }

        return organizerRepository.createOrganizer(
            eventId = eventId,
            email = email,  // ← Usar email en lugar de userId
            roleId = roleId
        )
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

