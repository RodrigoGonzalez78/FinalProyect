package com.example.finalproyect.presenter.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()


    private fun clearFields(){
        this.onEmailChange("")
        this.onPasswordChange("")
        this.onConfirmPasswordChange("")
    }

    fun changeNotification( state:Boolean){
        _uiState.update { it.copy(notification = state) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun validateAndRegister() {
        val currentState = _uiState.value
        val errors = SignupUiState(
            emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email)
                    .matches()
            ) "Formato de email inválido" else "",
            passwordError = if (currentState.password.length < 6) "La contraseña debe tener al menos 6 caracteres" else "",
            confirmPasswordError = if (currentState.password != currentState.confirmPassword) "Las contraseñas no coinciden" else ""
        )
        _uiState.update {
            it.copy(
                emailError = errors.emailError,
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError
            )
        }

        if (errors.hasErrors()) return

        register()
    }

    private fun register() {
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(message = "Las contraseñas no coinciden") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, message = "") }

            try {


                clearFields()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notification = true,
                        message = "${e.message}"
                    )
                }
            }
        }
    }
}

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val notification: Boolean = false,
    val message: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = ""
) {
    fun hasErrors() =
        emailError.isNotEmpty() || passwordError.isNotEmpty() || confirmPasswordError.isNotEmpty()
}