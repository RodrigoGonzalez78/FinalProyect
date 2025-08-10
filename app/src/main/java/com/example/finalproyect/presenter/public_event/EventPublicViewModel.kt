package com.example.finalproyect.presenter.public_event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
import com.example.finalproyect.domain.usecase.organizers.GetOrganizersByEventUseCase
import com.example.finalproyect.domain.usecase.ticket.PurchaseTicketUseCase
import com.example.finalproyect.domain.usecase.ticket_type.GetTicketTypesByEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventPublicViewModel @Inject constructor(
    private val getEventDetailUseCase: GetEventDetailUseCase,
    private val getTicketTypesByEventUseCase: GetTicketTypesByEventUseCase,
    private val getOrganizersByEventUseCase: GetOrganizersByEventUseCase,
    private val purchaseTicketUseCase: PurchaseTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventPublicUiState())
    val uiState: StateFlow<EventPublicUiState> = _uiState

    fun loadEventPublicData(eventId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Cargar detalles del evento
                loadEventDetail(eventId)

                // Cargar tipos de tickets
                loadTicketTypes(eventId)

                // Cargar organizador principal
                loadMainOrganizer(eventId)

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

    private suspend fun loadTicketTypes(eventId: Int) {
        _uiState.value = _uiState.value.copy(isLoadingTicketTypes = true)

        val ticketTypesResult = getTicketTypesByEventUseCase(eventId)

        ticketTypesResult.fold(
            onSuccess = { ticketTypes ->
                // Filtrar solo tickets disponibles (con cantidad > 0)
                val availableTicketTypes = ticketTypes.filter { it.available > 0 }
                _uiState.value = _uiState.value.copy(
                    ticketTypes = availableTicketTypes,
                    isLoadingTicketTypes = false
                )
            },
            onFailure = {
                _uiState.value = _uiState.value.copy(
                    ticketTypes = emptyList(),
                    isLoadingTicketTypes = false
                )
            }
        )
    }

    private suspend fun loadMainOrganizer(eventId: Int) {
        val organizersResult = getOrganizersByEventUseCase(eventId)

        organizersResult.fold(
            onSuccess = { organizers ->
                // Buscar el organizador principal (MAIN_ADMIN)
                val mainOrganizer = organizers.find { it.roleId == 1 }
                _uiState.value = _uiState.value.copy(mainOrganizer = mainOrganizer)
            },
            onFailure = {
                // No es crítico si no se puede cargar el organizador
            }
        )
    }

    fun selectTicketType(ticketType: TicketType) {
        _uiState.value = _uiState.value.copy(selectedTicketType = ticketType)
    }

    fun clearTicketTypeSelection() {
        _uiState.value = _uiState.value.copy(selectedTicketType = null)
    }

    fun purchaseTicket() {
        val selectedTicketType = _uiState.value.selectedTicketType
        if (selectedTicketType == null || !_uiState.value.canPurchase) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isPurchasing = true,
                purchaseError = null
            )

            try {
                val result = purchaseTicketUseCase(selectedTicketType.id)

                result.fold(
                    onSuccess = { purchaseResult ->
                        _uiState.value = _uiState.value.copy(
                            isPurchasing = false,
                            purchaseSuccess = true,
                            successMessage = "¡Ticket comprado exitosamente!",
                            selectedTicketType = null
                        )

                        // Recargar tipos de tickets para actualizar disponibilidad
                        _uiState.value.event?.let { event ->
                            loadTicketTypes(event.id.toInt())
                        }
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isPurchasing = false,
                            purchaseError = error.message ?: "Error al comprar el ticket"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isPurchasing = false,
                    purchaseError = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun clearPurchaseError() {
        _uiState.value = _uiState.value.copy(purchaseError = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}



data class EventPublicUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Datos del evento
    val event: Event? = null,
    val location: Location? = null,

    // Organizador principal
    val mainOrganizer: Organizer? = null,

    // Tipos de tickets disponibles
    val ticketTypes: List<TicketType> = emptyList(),
    val isLoadingTicketTypes: Boolean = false,

    // Estados de compra
    val selectedTicketType: TicketType? = null,
    val isPurchasing: Boolean = false,
    val purchaseSuccess: Boolean = false,
    val purchaseError: String? = null,

    // Mensajes
    val successMessage: String? = null
) {
    val hasEventData: Boolean
        get() = event != null

    val hasTicketTypes: Boolean
        get() = ticketTypes.isNotEmpty()

    val canPurchase: Boolean
        get() = selectedTicketType != null && !isPurchasing
}
