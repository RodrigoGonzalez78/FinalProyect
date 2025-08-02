package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.repository.EventRepository
import com.example.finalproyect.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val preferenceManager: PreferenceManager
) {
    suspend operator fun invoke(eventId: Int): Result<EventDetailWithPermissions> {

        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }

        return try {
            val eventDetailResult = eventRepository.getEventById(eventId)

            if (eventDetailResult.isFailure) {
                return Result.failure(eventDetailResult.exceptionOrNull() ?: Exception("Failed to get event detail"))
            }

            val eventDetail = eventDetailResult.getOrNull()
                ?: return Result.failure(Exception("Event not found"))

            val currentUserId = preferenceManager.getCurrentUserID().first().toInt()


            // Verificar si el usuario es organizador y sus permisos
            val userOrganizer = eventDetail.organizers.find { it.idUser == currentUserId }
            val isOrganizer = userOrganizer != null
            val isMainAdmin = userOrganizer?.idRol == 1  // ← Usar idRol en lugar de userOrganizerRole
            val canManageOrganizers = isMainAdmin
            val canManageTicketTypes = userOrganizer?.idRol in listOf(1, 2) // Admin principal o admin
            val canDeleteEvent = isMainAdmin

            Result.success(EventDetailWithPermissions(
                eventDetail = eventDetail,
                isOrganizer = isOrganizer,
                isMainAdmin = isMainAdmin,
                canManageOrganizers = canManageOrganizers,
                canManageTicketTypes = canManageTicketTypes,
                canDeleteEvent = canDeleteEvent,
                userOrganizerRole = userOrganizer?.idRol  // ← Usar idRol
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class EventDetailWithPermissions(
    val eventDetail: EventDetail,
    val isOrganizer: Boolean,
    val isMainAdmin: Boolean,
    val canManageOrganizers: Boolean,
    val canManageTicketTypes: Boolean,
    val canDeleteEvent: Boolean,
    val userOrganizerRole: Int?  // ← Este es el rol del usuario actual (1=admin principal, 2=validador, etc.)
)