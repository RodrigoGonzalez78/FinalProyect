package com.example.finalproyect.presenter.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private fun clearFields() {
        _uiState.update {
            it.copy(
                name = "",
                lastName = "",
                email = "",
                password = "",
                confirmPassword = "",
                phone = "",
                birthday = LocalDate.now()
            )
        }
    }

    fun changeNotification(state: Boolean) {
        _uiState.update { it.copy(notification = state) }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, nameError = "") }
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.update { it.copy(lastName = newLastName, lastNameError = "") }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = "") }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = "") }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword, confirmPasswordError = "") }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone, phoneError = "") }
    }

    fun onBirthdayChange(newBirthday: LocalDate) {
        _uiState.update { it.copy(birthday = newBirthday, birthdayError = "") }
    }

    fun validateAndRegister() {
        val currentState = _uiState.value

        val errors = SignupUiState(
            nameError = if (currentState.name.isBlank()) "El nombre es obligatorio" else "",
            lastNameError = if (currentState.lastName.isBlank()) "El apellido es obligatorio" else "",
            emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) "Formato de email inválido" else "",
            passwordError = if (currentState.password.length < 6) "La contraseña debe tener al menos 6 caracteres" else "",
            confirmPasswordError = if (currentState.password != currentState.confirmPassword) "Las contraseñas no coinciden" else "",
            birthdayError = if (currentState.birthday.isAfter(LocalDate.now().minusYears(18))) "Debes ser mayor de 18 años" else "",
            phoneError = if (currentState.phone.isNotBlank() && !isPhoneValid(currentState.phone)) "Formato de teléfono inválido" else ""
        )

        _uiState.update {
            it.copy(
                nameError = errors.nameError,
                lastNameError = errors.lastNameError,
                emailError = errors.emailError,
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError,
                birthdayError = errors.birthdayError,
                phoneError = errors.phoneError
            )
        }

        if (errors.hasErrors()) return

        register()
    }

    private fun isPhoneValid(phone: String): Boolean {
        val phonePattern = "^[+]?[0-9]{8,15}$".toRegex()
        return phone.matches(phonePattern)
    }

    private fun register() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true, message = "") }

            try {
                val result = registerUseCase(
                    name = currentState.name,
                    lastName = currentState.lastName,
                    email = currentState.email,
                    password = currentState.password,
                    birthday = currentState.birthday
                        .atStartOfDay(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_INSTANT),
                    phone = currentState.phone
                )

                result.fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                notification = true,
                                message = "Registro exitoso"
                            )
                        }
                        clearFields()
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                notification = true,
                                message = exception.message ?: "Error desconocido"
                            )
                        }
                    }
                )

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notification = true,
                        message = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class SignupUiState(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthday: LocalDate = LocalDate.now().minusYears(18),
    val phone: String = "",
    val isLoading: Boolean = false,
    val notification: Boolean = false,
    val message: String = "",
    val nameError: String = "",
    val lastNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val birthdayError: String = "",
    val phoneError: String = ""
) {
    fun hasErrors() = nameError.isNotEmpty() ||
            lastNameError.isNotEmpty() ||
            emailError.isNotEmpty() ||
            passwordError.isNotEmpty() ||
            confirmPasswordError.isNotEmpty() ||
            birthdayError.isNotEmpty() ||
            phoneError.isNotEmpty()
}