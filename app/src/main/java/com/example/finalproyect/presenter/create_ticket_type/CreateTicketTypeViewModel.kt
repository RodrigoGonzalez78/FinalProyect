package com.example.finalproyect.presenter.create_ticket_type

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.usecase.ticket_type.CreateTicketTypeUseCase
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
    private val createTicketTypeUseCase: CreateTicketTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketTypeUiState())
    val uiState: StateFlow<CreateTicketTypeUiState> = _uiState

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

    fun createTicketType(eventId: Int) {
        if (!_uiState.value.canCreate) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, error = null)

            try {
                val result = createTicketTypeUseCase(
                    eventId = eventId,
                    name = _uiState.value.name.trim(),
                    description = _uiState.value.description.trim(),
                    price = _uiState.value.price.toDouble(),
                    available =_uiState.value.quantity.toInt()
                )

                result.fold(
                    onSuccess = {
                        _uiState.value = _uiState.value.copy(
                            isCreating = false,
                            createSuccess = true,
                            successMessage = "Tipo de ticket creado exitosamente"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isCreating = false,
                            error = error.message ?: "Error al crear el tipo de ticket"
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
        _uiState.value = CreateTicketTypeUiState()
    }
}



data class CreateTicketTypeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

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
    val isCreating: Boolean = false,
    val createSuccess: Boolean = false
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

    val canCreate: Boolean
        get() = isFormValid && !isCreating
}