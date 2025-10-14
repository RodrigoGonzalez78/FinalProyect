package com.example.finalproyect.presenter.create_ticket_type

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.usecase.ticket_type.CreateTicketTypeUseCase
import com.example.finalproyect.domain.usecase.ticket_type.DeleteTicketTypeUseCase
import com.example.finalproyect.domain.usecase.ticket_type.GetTicketTypeByIdUseCase
import com.example.finalproyect.domain.usecase.ticket_type.UpdateTicketTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class CreateTicketTypeViewModel @Inject constructor(
    private val createTicketTypeUseCase: CreateTicketTypeUseCase,
    private val getTicketTypeByIdUseCase: GetTicketTypeByIdUseCase,
    private val deleteTicketTypeUseCase: DeleteTicketTypeUseCase,
    private val updateTicketTypeUseCase: UpdateTicketTypeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketTypeUiState())
    val uiState: StateFlow<CreateTicketTypeUiState> = _uiState

    fun loadTicketType(ticketTypeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = getTicketTypeByIdUseCase(ticketTypeId)

                result.fold(
                    onSuccess = { ticketType ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            ticketTypeId = ticketType.id,
                            name = ticketType.name,
                            description = ticketType.description ?: "",
                            price = ticketType.price.toString(),
                            quantity = ticketType.available.toString(),
                            isEditMode = true
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar el tipo de ticket"
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

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = validateName(name)
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            descriptionError = validateDescription(description)
        )
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(
            price = price,
            priceError = validatePrice(price)
        )
    }

    fun updateQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(
            quantity = quantity,
            quantityError = validateQuantity(quantity)
        )
    }

    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es requerido"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            name.length > 50 -> "El nombre no puede exceder 50 caracteres"
            else -> null
        }
    }

    private fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "La descripción es requerida"
            description.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            description.length > 200 -> "La descripción no puede exceder 200 caracteres"
            else -> null
        }
    }

    private fun validatePrice(price: String): String? {
        return when {
            price.isBlank() -> "El precio es requerido"
            else -> {
                try {
                    val priceValue = BigDecimal(price)
                    when {
                        priceValue < BigDecimal.ZERO -> "El precio no puede ser negativo"
                        priceValue > BigDecimal("999999.99") -> "El precio es demasiado alto"
                        else -> null
                    }
                } catch (e: NumberFormatException) {
                    "Formato de precio inválido"
                }
            }
        }
    }

    private fun validateQuantity(quantity: String): String? {
        return when {
            quantity.isBlank() -> "La cantidad es requerida"
            else -> {
                try {
                    val quantityValue = quantity.toInt()
                    when {
                        quantityValue <= 0 -> "La cantidad debe ser mayor a 0"
                        quantityValue > 10000 -> "La cantidad es demasiado alta"
                        else -> null
                    }
                } catch (e: NumberFormatException) {
                    "Formato de cantidad inválido"
                }
            }
        }
    }

    fun saveTicketType(eventId: Int) {
        if (!_uiState.value.canSave) return

        if (_uiState.value.isEditMode) {
            updateTicketType(eventId)
        } else {
            createTicketType(eventId)
        }
    }

    private fun createTicketType(eventId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            try {
                val result = createTicketTypeUseCase(
                    eventId = eventId,
                    name = _uiState.value.name.trim(),
                    description = _uiState.value.description.trim(),
                    price = _uiState.value.price.toDouble(),
                    available = _uiState.value.quantity.toInt()
                )

                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            saveSuccess = true,
                            successMessage = "Tipo de ticket creado exitosamente"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            error = error.message ?: "Error al crear el tipo de ticket"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    private fun updateTicketType(eventId: Int) {
        val ticketTypeId = _uiState.value.ticketTypeId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            try {
                val result = updateTicketTypeUseCase(
                    eventId = eventId,
                    ticketTypeId = ticketTypeId,
                    name = _uiState.value.name.trim(),
                    description = _uiState.value.description.trim(),
                    available = _uiState.value.quantity.toInt(),
                    price = _uiState.value.price.toDouble()
                )

                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            saveSuccess = true,
                            successMessage = "Tipo de ticket actualizado exitosamente"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            error = error.message ?: "Error al actualizar el tipo de ticket"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun deleteTicketType(eventId: Int) {
        val ticketTypeId = _uiState.value.ticketTypeId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true, error = null)

            try {
                val result = deleteTicketTypeUseCase(eventId, ticketTypeId)

                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isDeleting = false,
                            deleteSuccess = true,
                            successMessage = "Tipo de ticket eliminado exitosamente"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isDeleting = false,
                            error = error.message ?: "Error al eliminar el tipo de ticket"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun resetForm() {
        _uiState.value = CreateTicketTypeUiState()
    }
}

data class CreateTicketTypeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    // Modo de edición
    val isEditMode: Boolean = false,
    val ticketTypeId: Int? = null,

    // Campos del formulario
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val quantity: String = "",

    // Estados de validación
    val nameError: String? = null,
    val descriptionError: String? = null,
    val priceError: String? = null,
    val quantityError: String? = null,

    // Estados de operación
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val showDeleteDialog: Boolean = false
) {
    val isFormValid: Boolean
        get() = name.isNotBlank() &&
                description.isNotBlank() &&
                price.isNotBlank() &&
                quantity.isNotBlank() &&
                nameError == null &&
                descriptionError == null &&
                priceError == null &&
                quantityError == null

    val canSave: Boolean
        get() = isFormValid && !isSaving && !isDeleting

    val canDelete: Boolean
        get() = isEditMode && !isSaving && !isDeleting
}