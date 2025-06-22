package com.example.finalproyect.domain.usecase.organizers


import com.example.finalproyect.domain.repository.OrganizerRepository
import javax.inject.Inject

class CheckUserOrganizerPermissionsUseCase @Inject constructor(
    private val organizerRepository: OrganizerRepository
) {
    suspend operator fun invoke(eventId: Int, userId: Int): Result<OrganizerPermissions> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (userId <= 0) {
            return Result.failure(Exception("Invalid user ID"))
        }

        return try {
            val organizers = organizerRepository.getOrganizersByEvent(eventId)
            if (organizers.isFailure) {
                return Result.failure(organizers.exceptionOrNull() ?: Exception("Failed to get organizers"))
            }

            val organizersList = organizers.getOrNull() ?: emptyList()
            val userOrganizer = organizersList.find { it.userId == userId }

            val permissions = OrganizerPermissions(
                isOrganizer = userOrganizer != null,
                isMainAdmin = userOrganizer?.isMainAdmin == true,
                canAddOrganizers = userOrganizer?.isMainAdmin == true,
                canRemoveOrganizers = userOrganizer?.isMainAdmin == true,
                canUpdateRoles = userOrganizer?.isMainAdmin == true,
                roleId = userOrganizer?.roleId
            )

            Result.success(permissions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class OrganizerPermissions(
    val isOrganizer: Boolean,
    val isMainAdmin: Boolean,
    val canAddOrganizers: Boolean,
    val canRemoveOrganizers: Boolean,
    val canUpdateRoles: Boolean,
    val roleId: Int? = null
)
