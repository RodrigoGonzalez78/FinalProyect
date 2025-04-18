package com.example.finalproyect.presenter.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private  val dataStore: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }

    }

    fun changeNotificationState(newState: Boolean) {
        _uiState.update { it.copy(notification = newState) }
    }

    fun validateFields() {
        val currentState = _uiState.value
        val errors = LoginUiState(
            emailError = if (currentState.email == "") "Complete el campo por favor" else "",
            passwordError = if (currentState.password == "") "Complete el campo por favor" else ""
        )
        _uiState.update {
            it.copy(
                emailError = errors.emailError,
                passwordError = errors.passwordError
            )
        }

        if (errors.hasErrors()) return

        loginClick()
    }

    private fun loginClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {


            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error de inicio de sesión"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error de inicio de sesión"
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordError: String = "",
    val emailError: String = "",
    val isLoading: Boolean = false,
    val notification: Boolean = false,
    val message: String = ""
) {
    fun hasErrors() = this.passwordError.isNotEmpty() || this.emailError.isNotEmpty()
}