package com.example.finalproyect.presenter.ticket_detail

import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.model.TicketType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Notification
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
import com.example.finalproyect.domain.usecase.notification.GetNotificationsByEventUseCase
import com.example.finalproyect.domain.usecase.ticket.GetUserTicketForEventUseCase
import com.example.finalproyect.domain.usecase.ticket_type.GetTicketTypesByEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val getUserTicketForEventUseCase: GetUserTicketForEventUseCase,
    private val getEventDetailUseCase: GetEventDetailUseCase,
    private val getTicketTypesByEventUseCase: GetTicketTypesByEventUseCase,
    private val getNotificationsByEventUseCase: GetNotificationsByEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState

    fun loadTicketDetail(eventId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Cargar ticket del usuario
                val ticketResult = getUserTicketForEventUseCase(eventId)

                ticketResult.fold(
                    onSuccess = { ticket ->
                        _uiState.value = _uiState.value.copy(ticket = ticket)

                        // Cargar detalles del evento
                        loadEventDetail(eventId)

                        // Cargar tipo de ticket
                        if (ticket != null) {
                            loadTicketType(eventId, ticket.ticketTypeId)
                        }

                        // Cargar notificaciones del evento
                        loadNotifications(eventId)
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar el ticket"
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

    private suspend fun loadEventDetail(eventId: Int) {
        val eventResult = getEventDetailUseCase(eventId)

        eventResult.fold(
            onSuccess = { eventDetailWithPermissions ->
                _uiState.value = _uiState.value.copy(
                    event = eventDetailWithPermissions.eventDetail.event,
                    location = eventDetailWithPermissions.eventDetail.location,
                    isLoading = false
                )
            },
            onFailure = { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al cargar el evento"
                )
            }
        )
    }

    private suspend fun loadTicketType(eventId: Int, ticketTypeId: Int) {
        val ticketTypesResult = getTicketTypesByEventUseCase(eventId)

        ticketTypesResult.fold(
            onSuccess = { ticketTypes ->
                val ticketType = ticketTypes.find { it.id == ticketTypeId }
                _uiState.value = _uiState.value.copy(ticketType = ticketType)
            },
            onFailure = { /* No es crítico si no se puede cargar el tipo */ }
        )
    }

    private suspend fun loadNotifications(eventId: Int) {
        _uiState.value = _uiState.value.copy(isLoadingNotifications = true)

        val notificationsResult = getNotificationsByEventUseCase(eventId)

        notificationsResult.fold(
            onSuccess = { notifications ->
                _uiState.value = _uiState.value.copy(
                    notifications = notifications,
                    isLoadingNotifications = false
                )
            },
            onFailure = {
                // No es crítico si no se pueden cargar las notificaciones
                _uiState.value = _uiState.value.copy(
                    notifications = emptyList(),
                    isLoadingNotifications = false
                )
            }
        )
    }

    fun downloadTicket() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDownloading = true)

            try {
                // Simular descarga (aquí iría la lógica real de descarga)
                kotlinx.coroutines.delay(2000)

                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    downloadSuccess = true,
                    successMessage = "Ticket descargado exitosamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    error = "Error al descargar el ticket"
                )
            }
        }
    }

    fun shareTicket() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSharing = true)

            try {
                // Simular compartir (aquí iría la lógica real de compartir)
                kotlinx.coroutines.delay(1000)

                _uiState.value = _uiState.value.copy(
                    isSharing = false,
                    shareSuccess = true,
                    successMessage = "Ticket compartido exitosamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSharing = false,
                    error = "Error al compartir el ticket"
                )
            }
        }
    }

    fun refreshNotifications(eventId: Int) {
        viewModelScope.launch {
            loadNotifications(eventId)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Datos del ticket
    val ticket: Ticket? = null,
    val ticketType: TicketType? = null,

    // Datos del evento
    val event: Event? = null,
    val location: Location? = null,

    // Notificaciones del evento
    val notifications: List<Notification> = emptyList(),
    val isLoadingNotifications: Boolean = false,

    // Estados de operaciones
    val isDownloading: Boolean = false,
    val isSharing: Boolean = false,
    val downloadSuccess: Boolean = false,
    val shareSuccess: Boolean = false,

    // Mensajes
    val successMessage: String? = null
) {
    val hasTicketData: Boolean
        get() = ticket != null && event != null

    val isTicketValid: Boolean
        get() = ticket?.isValid == true

    val canDownload: Boolean
        get() = hasTicketData && !isDownloading

    val canShare: Boolean
        get() = hasTicketData && !isSharing
}