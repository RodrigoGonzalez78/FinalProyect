package com.example.finalproyect.presenter.create_notification
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.usecase.notification.CreateNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNotificationViewModel @Inject constructor(
    private val createNotificationUseCase: CreateNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateNotificationUiState())
    val uiState: StateFlow<CreateNotificationUiState> = _uiState

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = validateTitle(title)
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            descriptionError = validateDescription(description)
        )
    }

    fun updateImage(image: String) {
        _uiState.value = _uiState.value.copy(image = image)
    }

    private fun validateTitle(title: String): String? {
        return when {
            title.isBlank() -> "El título es requerido"
            title.length < 3 -> "El título debe tener al menos 3 caracteres"
            title.length > 100 -> "El título no puede exceder 100 caracteres"
            else -> null
        }
    }

    private fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "La descripción es requerida"
            description.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            description.length > 500 -> "La descripción no puede exceder 500 caracteres"
            else -> null
        }
    }

    fun createNotification(eventId: Int) {
        if (!_uiState.value.canCreate) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, error = null)

            try {
                val result = createNotificationUseCase(
                    eventId = eventId,
                    title = _uiState.value.title.trim(),
                    description = _uiState.value.description.trim(),
                    image = _uiState.value.image.trim()
                )

                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isCreating = false,
                            createSuccess = true,
                            successMessage = "Notificación creada exitosamente"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isCreating = false,
                            error = error.message ?: "Error al crear la notificación"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreating = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun resetForm() {
        _uiState.value = CreateNotificationUiState()
    }
}
