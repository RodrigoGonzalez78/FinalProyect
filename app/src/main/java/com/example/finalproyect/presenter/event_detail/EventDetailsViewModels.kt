package com.example.finalproyect.presenter.event_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.usecase.auth.LoginUseCase
import com.example.finalproyect.domain.usecase.event.DeleteEventUseCase
import com.example.finalproyect.domain.usecase.event.EventDetailWithPermissions
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
import com.example.finalproyect.domain.usecase.event.UpdateEventUseCase
import com.example.finalproyect.domain.usecase.organizers.CheckUserOrganizerPermissionsUseCase
import com.example.finalproyect.domain.usecase.organizers.CreateOrganizerUseCase
import com.example.finalproyect.domain.usecase.organizers.DeleteOrganizerUseCase
import com.example.finalproyect.domain.usecase.organizers.GetOrganizersByEventUseCase
import com.example.finalproyect.domain.usecase.organizers.UpdateOrganizerRoleUseCase
import com.example.finalproyect.domain.usecase.ticket.GetUserTicketForEventUseCase
import com.example.finalproyect.domain.usecase.ticket.PurchaseTicketUseCase
import com.example.finalproyect.domain.usecase.ticket_type.CreateTicketTypeUseCase
import com.example.finalproyect.domain.usecase.ticket_type.CreateTicketTypeUseCase_Factory
import com.example.finalproyect.domain.usecase.ticket_type.DeleteTicketTypeUseCase
import com.example.finalproyect.domain.usecase.ticket_type.GetTicketTypesByEventUseCase
import com.example.finalproyect.domain.usecase.ticket_type.UpdateTicketTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailUseCase: GetEventDetailUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val getTicketTypesByEventUseCase: GetTicketTypesByEventUseCase,
    private val createTicketTypeUseCase: CreateTicketTypeUseCase,
    private val updateTicketTypeUseCase: UpdateTicketTypeUseCase,
    private val deleteTicketTypeUseCase: DeleteTicketTypeUseCase,
    private val createOrganizerUseCase: CreateOrganizerUseCase,
    private val getOrganizersByEventUseCase: GetOrganizersByEventUseCase,
    private val updateOrganizerRoleUseCase: UpdateOrganizerRoleUseCase,
    private val deleteOrganizerUseCase: DeleteOrganizerUseCase,
    private val getUserTicketForEventUseCase: GetUserTicketForEventUseCase,
    private val purchaseTicketUseCase: PurchaseTicketUseCase
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

                        // Cargar datos adicionales
                        loadTicketTypes()
                        loadOrganizers()
                        loadUserTicket()
                    },
                    onFailure = { error ->
                        Log.e("Fallo","Herror 1")
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

    // TICKET TYPES
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

    fun createTicketType(name: String, price: Double, description: String?, available: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingTicketType = true)

            val result = createTicketTypeUseCase(
                eventId = currentEventId,
                name = name,
                description = description,
                available = available,
                price = price
            )

            result.fold(
                onSuccess = { ticketType ->
                    _uiState.value = _uiState.value.copy(
                        isCreatingTicketType = false,
                        ticketTypes = _uiState.value.ticketTypes + ticketType,
                        successMessage = "Tipo de ticket creado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isCreatingTicketType = false,
                        error = error.message ?: "Error al crear tipo de ticket"
                    )
                }
            )
        }
    }

    fun updateTicketType(ticketTypeId: Int, name: String, price: Double, description: String?, available: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdatingTicketType = true)

            val result = updateTicketTypeUseCase(
                eventId = currentEventId,
                ticketTypeId = ticketTypeId,
                name = name,
                description = description,
                available = available,
                price = price
            )

            result.fold(
                onSuccess = { updatedTicketType ->
                    val updatedList = _uiState.value.ticketTypes.map { ticketType ->
                        if (ticketType.id == ticketTypeId) updatedTicketType else ticketType
                    }
                    _uiState.value = _uiState.value.copy(
                        isUpdatingTicketType = false,
                        ticketTypes = updatedList,
                        successMessage = "Tipo de ticket actualizado exitosamente"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isUpdatingTicketType = false,
                        error = error.message ?: "Error al actualizar tipo de ticket"
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

    fun createOrganizer(userId: Int, roleId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingOrganizer = true)

            val result = createOrganizerUseCase(
                eventId = currentEventId,
                userId = userId,
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

    fun purchaseTicket(ticketTypeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPurchasingTicket = true)

            val result = purchaseTicketUseCase(ticketTypeId)

            result.fold(
                onSuccess = { purchaseResult ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasingTicket = false,
                        userTicket = purchaseResult.ticket,
                        successMessage = purchaseResult.message
                    )
                    // Recargar tipos de ticket para actualizar disponibilidad
                    loadTicketTypes()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasingTicket = false,
                        error = error.message ?: "Error al comprar ticket"
                    )
                }
            )
        }
    }

    // EVENT MANAGEMENT
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

    // ✅ CORREGIDO: Ahora usa el campo correcto
    val userOrganizerRole: Int?
        get() = eventDetailWithPermissions?.userOrganizerRole

    val hasUserTicket: Boolean
        get() = userTicket != null

    // Métodos de conveniencia para verificar roles específicos
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