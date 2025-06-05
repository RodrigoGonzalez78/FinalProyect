package com.example.finalproyect.presenter.event_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.usecase.auth.LoginUseCase
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModels @Inject constructor(
    private val getEventDetailUseCase: GetEventDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState

    private val _activeSection = MutableStateFlow("overview")
    val activeSection: StateFlow<String> = _activeSection

    init {
        _uiState.value = EventDetailUiState()
    }

    fun loadEventDetail(eventId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = getEventDetailUseCase(eventId.toInt())
                result.fold(
                    onSuccess = { eventDetail ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            eventDetail = eventDetail,
                            error = null
                        )
                    },
                    onFailure = { error ->
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

    fun validateTicket(qrCode: String) {
        // TODO: Implementar cuando esté disponible el use case
        viewModelScope.launch {
            // Simulación temporal
            delay(1000)
            // Aquí iría la lógica real de validación
        }
    }

    fun addOrganizer(email: String, role: String) {
        // TODO: Implementar cuando esté disponible el use case
        viewModelScope.launch {
            // Simulación temporal
            delay(1000)
            // Aquí iría la lógica real para añadir organizador
        }
    }

    fun addTicketType(name: String, price: Double, description: String, limit: Int) {
        // TODO: Implementar cuando esté disponible el use case
        viewModelScope.launch {
            // Simulación temporal
            delay(1000)
            // Aquí iría la lógica real para añadir tipo de entrada
        }
    }

    fun createNotification(title: String, message: String, sendNow: Boolean) {
        // TODO: Implementar cuando esté disponible el use case
        viewModelScope.launch {
            // Simulación temporal
            delay(1000)
            // Aquí iría la lógica real para crear notificación
        }
    }
}

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val eventDetail: EventDetail? = null,
    val error: String? = null
)