package com.example.finalproyect.presenter.create_notification
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.usecase.notification.CreateNotificationUseCase
import com.example.finalproyect.domain.usecase.upload.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNotificationViewModel @Inject constructor(
    private val createNotificationUseCase: CreateNotificationUseCase,
    private val uploadImageUseCase: UploadImageUseCase
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

    fun updateImage(imageUri: String) {
        _uiState.value = _uiState.value.copy(
            selectedImageUri = imageUri,
            image = null // Reset uploaded image name when selecting new image
        )
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

    private suspend fun uploadImage(imageUri: String): String? {
        return try {
            _uiState.value = _uiState.value.copy(isUploadingImage = true)

            val result = uploadImageUseCase(imageUri.toUri())
            result.fold(
                onSuccess = { imageName ->
                    _uiState.value = _uiState.value.copy(
                        image = imageName,
                        isUploadingImage = false
                    )
                    imageName
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isUploadingImage = false,
                        error = "Error al subir imagen: ${error.message}"
                    )
                    null
                }
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isUploadingImage = false,
                error = "Error al subir imagen: ${e.message}"
            )
            null
        }
    }

    fun createNotification(eventId: Int) {
        if (!_uiState.value.canCreate) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, error = null)

            try {
                // First, upload image if one is selected
                var imageName: String? = _uiState.value.image

                if (_uiState.value.selectedImageUri.isNotBlank() && imageName == null) {
                    imageName = uploadImage(_uiState.value.selectedImageUri)
                    if (imageName == null) {
                        // Image upload failed, stop here
                        return@launch
                    }
                }

                // Create notification with the uploaded image name
                val result = createNotificationUseCase(
                    eventId = eventId,
                    title = _uiState.value.title.trim(),
                    description = _uiState.value.description.trim(),
                    image = imageName?.trim() ?: ""
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


data class CreateNotificationUiState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val selectedImageUri: String = "", // URI de la imagen seleccionada por el usuario
    val image: String? = null, // Nombre de la imagen devuelto por el servidor
    val isUploadingImage: Boolean = false,
    val isCreating: Boolean = false,
    val createSuccess: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
) {
    val canCreate: Boolean
        get() = title.isNotBlank() &&
                description.isNotBlank() &&
                titleError == null &&
                descriptionError == null &&
                !isCreating &&
                !isUploadingImage

    val isLoading: Boolean
        get() = isCreating || isUploadingImage
}