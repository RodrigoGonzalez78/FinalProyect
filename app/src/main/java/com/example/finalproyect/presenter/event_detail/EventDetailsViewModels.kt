package com.example.finalproyect.presenter.event_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.Notification
import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.usecase.event.DeleteEventUseCase
import com.example.finalproyect.domain.usecase.event.EventDetailWithPermissions
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
import com.example.finalproyect.domain.usecase.notification.DeleteNotificationUseCase
import com.example.finalproyect.domain.usecase.notification.GetNotificationsByEventUseCase
import com.example.finalproyect.domain.usecase.organizers.CreateOrganizerUseCase
import com.example.finalproyect.domain.usecase.organizers.DeleteOrganizerUseCase
import com.example.finalproyect.domain.usecase.organizers.GetOrganizersByEventUseCase
import com.example.finalproyect.domain.usecase.organizers.UpdateOrganizerRoleUseCase
import com.example.finalproyect.domain.usecase.ticket.GetUserTicketForEventUseCase
import com.example.finalproyect.domain.usecase.ticket_type.DeleteTicketTypeUseCase
import com.example.finalproyect.domain.usecase.ticket_type.GetTicketTypesByEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailUseCase: GetEventDetailUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val getTicketTypesByEventUseCase: GetTicketTypesByEventUseCase,
    private val deleteTicketTypeUseCase: DeleteTicketTypeUseCase,
    private val createOrganizerUseCase: CreateOrganizerUseCase,
    private val getOrganizersByEventUseCase: GetOrganizersByEventUseCase,
    private val updateOrganizerRoleUseCase: UpdateOrganizerRoleUseCase,
    private val deleteOrganizerUseCase: DeleteOrganizerUseCase,
    private val getUserTicketForEventUseCase: GetUserTicketForEventUseCase,
    private val getNotificationsByEventUseCase: GetNotificationsByEventUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState

    private val _activeSection = MutableStateFlow("overview")
    val activeSection: StateFlow<String> = _activeSection

    private var currentEventId: Int = 0

    fun loadEventDetail(eventId: String) {
        val id = eventId.toIntOrNull()
        if (id == null || id <= 0) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "ID de evento inválido"
            )
            return
        }

        currentEventId = id

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = getEventDetailUseCase(id)
                result.fold(
                    onSuccess = { eventDetailWithPermissions ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            eventDetailWithPermissions = eventDetailWithPermissions,
                            error = null
                        )

                        loadTicketTypes()
                        loadOrganizers()
                        loadUserTicket()
                        loadNotifications()
                    },
                    onFailure = { error ->
                        Log.e("Varibles view", eventId)
                        Log.e("Varibles view", error.message ?: "")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error desconocido"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun setActiveSection(section: String) {
        _activeSection.value = section
    }

    private fun loadTicketTypes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingTicketTypes = true, ticketTypesError = null)

            val result = getTicketTypesByEventUseCase(currentEventId)
            result.fold(
                onSuccess = { ticketTypes ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingTicketTypes = false,
                        ticketTypes = ticketTypes,
                        ticketTypesError = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingTicketTypes = false,
                        ticketTypesError = error.message ?: "Error al cargar tipos de ticket"
                    )
                }
            )
        }
    }

    private fun loadNotifications() {
        Log.e("Carga", "No cargar ################")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingNotifications = true, notificationError = null)

            val result = getNotificationsByEventUseCase(currentEventId)
            result.fold(
                onSuccess = { notificationsList ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingNotifications = false,
                        notification = notificationsList,
                        notificationError = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingNotifications = false,
                        notificationError = error.message ?: "Error al cargar las notificaciones"
                    )
                }
            )
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingNotification = true)

            val result = deleteNotificationUseCase(notificationId)

            result.fold(
                onSuccess = {
                    val updatedList = _uiState.value.notification.filter { it.id != notificationId }
                    _uiState.value = _uiState.value.copy(
                        isDeletingNotification = false,
                        notification = updatedList,
                        successMessage = "Notificación eliminada exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeletingNotification = false,
                        error = error.message ?: "Error al eliminar notificación"
                    )
                }
            )
        }
    }

    fun deleteTicketType(ticketTypeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingTicketType = true)

            val result = deleteTicketTypeUseCase(currentEventId, ticketTypeId)

            result.fold(
                onSuccess = {
                    val updatedList = _uiState.value.ticketTypes.filter { it.id != ticketTypeId }
                    _uiState.value = _uiState.value.copy(
                        isDeletingTicketType = false,
                        ticketTypes = updatedList,
                        successMessage = "Tipo de ticket eliminado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeletingTicketType = false,
                        error = error.message ?: "Error al eliminar tipo de ticket"
                    )
                }
            )
        }
    }

    // ORGANIZERS
    private fun loadOrganizers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOrganizers = true, organizersError = null)

            val result = getOrganizersByEventUseCase(currentEventId)
            result.fold(
                onSuccess = { organizers ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingOrganizers = false,
                        organizers = organizers,
                        organizersError = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingOrganizers = false,
                        organizersError = error.message ?: "Error al cargar organizadores"
                    )
                }
            )
        }
    }

    fun createOrganizer(email: String, roleId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingOrganizer = true)

            val result = createOrganizerUseCase(
                eventId = currentEventId,
                email = email,
                roleId = roleId
            )

            result.fold(
                onSuccess = { organizer ->
                    _uiState.value = _uiState.value.copy(
                        isCreatingOrganizer = false,
                        organizers = _uiState.value.organizers + organizer,
                        successMessage = "Organizador agregado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isCreatingOrganizer = false,
                        error = error.message ?: "Error al agregar organizador"
                    )
                }
            )
        }
    }

    fun updateOrganizerRole(organizerId: Int, newRoleId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdatingOrganizer = true)

            val result = updateOrganizerRoleUseCase(
                eventId = currentEventId,
                organizerId = organizerId,
                newRoleId = newRoleId
            )

            result.fold(
                onSuccess = { updatedOrganizer ->
                    val updatedList = _uiState.value.organizers.map { organizer ->
                        if (organizer.id == organizerId) updatedOrganizer else organizer
                    }
                    _uiState.value = _uiState.value.copy(
                        isUpdatingOrganizer = false,
                        organizers = updatedList,
                        successMessage = "Rol de organizador actualizado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isUpdatingOrganizer = false,
                        error = error.message ?: "Error al actualizar rol de organizador"
                    )
                }
            )
        }
    }

    fun deleteOrganizer(organizerId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingOrganizer = true)

            val result = deleteOrganizerUseCase(currentEventId, organizerId)

            result.fold(
                onSuccess = {
                    val updatedList = _uiState.value.organizers.filter { it.id != organizerId }
                    _uiState.value = _uiState.value.copy(
                        isDeletingOrganizer = false,
                        organizers = updatedList,
                        successMessage = "Organizador eliminado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeletingOrganizer = false,
                        error = error.message ?: "Error al eliminar organizador"
                    )
                }
            )
        }
    }

    // USER TICKET
    private fun loadUserTicket() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingUserTicket = true, userTicketError = null)

            val result = getUserTicketForEventUseCase(currentEventId)
            result.fold(
                onSuccess = { ticket ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingUserTicket = false,
                        userTicket = ticket,
                        userTicketError = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingUserTicket = false,
                        userTicketError = error.message ?: "Error al cargar ticket del usuario"
                    )
                }
            )
        }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingEvent = true)

            val result = deleteEventUseCase(currentEventId)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isDeletingEvent = false,
                        successMessage = "Evento eliminado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeletingEvent = false,
                        error = error.message ?: "Error al eliminar evento"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun refreshData() {
        if (currentEventId > 0) {
            loadEventDetail(currentEventId.toString())
        }
    }
}

data class EventDetailUiState(
    // Estado general
    val isLoading: Boolean = false,
    val error: String? = null,

    // Datos del evento
    val eventDetailWithPermissions: EventDetailWithPermissions? = null,

    // Tipos de ticket
    val ticketTypes: List<TicketType> = emptyList(),
    val isLoadingTicketTypes: Boolean = false,
    val ticketTypesError: String? = null,

    // Notificaciones
    val notification: List<Notification> = emptyList(),
    val isLoadingNotifications: Boolean = false,
    val notificationError: String? = null,

    // Organizadores
    val organizers: List<Organizer> = emptyList(),
    val isLoadingOrganizers: Boolean = false,
    val organizersError: String? = null,

    // Ticket del usuario
    val userTicket: Ticket? = null,
    val isLoadingUserTicket: Boolean = false,
    val userTicketError: String? = null,

    // Estados de operaciones
    val isCreatingTicketType: Boolean = false,
    val isUpdatingTicketType: Boolean = false,
    val isDeletingTicketType: Boolean = false,
    val isCreatingOrganizer: Boolean = false,
    val isUpdatingOrganizer: Boolean = false,
    val isDeletingOrganizer: Boolean = false,
    val isPurchasingTicket: Boolean = false,
    val isDeletingEvent: Boolean = false,
    val isDeletingNotification: Boolean = false,

    // Mensajes de éxito
    val successMessage: String? = null
) {
    val eventDetail: EventDetail?
        get() = eventDetailWithPermissions?.eventDetail

    val isOrganizer: Boolean
        get() = eventDetailWithPermissions?.isOrganizer == true

    val isMainAdmin: Boolean
        get() = eventDetailWithPermissions?.isMainAdmin == true

    val canManageOrganizers: Boolean
        get() = eventDetailWithPermissions?.canManageOrganizers == true

    val canManageTicketTypes: Boolean
        get() = eventDetailWithPermissions?.canManageTicketTypes == true

    val canDeleteEvent: Boolean
        get() = eventDetailWithPermissions?.canDeleteEvent == true

    val userOrganizerRole: Int?
        get() = eventDetailWithPermissions?.userOrganizerRole

    val hasUserTicket: Boolean
        get() = userTicket != null

    val isValidator: Boolean
        get() = userOrganizerRole == 2

    val isModerator: Boolean
        get() = userOrganizerRole == 3

    val isCollaborator: Boolean
        get() = userOrganizerRole == 4

    // Método para obtener el nombre del rol
    fun getRoleName(): String {
        return when (userOrganizerRole) {
            1 -> "Administrador Principal"
            2 -> "Validador"
            3 -> "Moderador"
            4 -> "Colaborador"
            else -> if (isOrganizer) "Organizador" else "Asistente"
        }
    }
}