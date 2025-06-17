package com.example.finalproyect.presenter.profile

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.finalproyect.domain.usecase.auth.LogoutUseCase
import com.example.finalproyect.domain.usecase.user.ChangePasswordUseCase
import com.example.finalproyect.domain.usecase.user.GetUserByIdUseCase
import com.example.finalproyect.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var currentUserId: Int = 0

    init {
        // Al iniciar el ViewModel, arrancamos la carga del usuario actual
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {

            getCurrentUserUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo cargar el perfil: ${throwable.message}"
                        )
                    }
                }
                .collect { user ->
                    if (user != null) {
                        currentUserId = user.id
                        // Mapeamos los campos de User al ProfileUiState
                        _uiState.update {
                            it.copy(
                                name = user.name,
                                lastName = user.lastName,
                                email = user.email, // Solo lectura
                                phone = user.phone,
                                birthday = user.birthday,
                                imageUri = null,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Usuario no encontrado"
                            )
                        }
                    }
                }
        }
    }

    // Funciones para actualizar cada campo en el UI State
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, errorMessage = null, successMessage = null) }
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.update {
            it.copy(
                lastName = newLastName,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    // Email no se puede cambiar, así que removemos esta función
    // fun onEmailChange(newEmail: String) { ... }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone, errorMessage = null, successMessage = null) }
    }

    fun onBirthdayChange(newBirthday: LocalDate) {
        _uiState.update {
            it.copy(
                birthday = newBirthday,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onImageUriChange(newUri: Uri?) {
        _uiState.update { it.copy(imageUri = newUri, errorMessage = null, successMessage = null) }
    }

    // Método para guardar los cambios: llama a UpdateUserUseCase
    fun saveProfile() {
        val current = _uiState.value

        // Validación previa
        if (current.hasBasicInfoErrors()) {
            _uiState.update {
                it.copy(errorMessage = "Completa los campos obligatorios antes de guardar.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                val result = updateUserUseCase(
                    userId = currentUserId,
                    name = current.name,
                    lastName = current.lastName,
                    birthday = current.birthday,
                    phone = current.phone
                )

                result.fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = "Perfil actualizado correctamente"
                            )
                        }
                    },
                    onFailure = { ex ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ex.message ?: "Error al actualizar perfil"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onChangePasswordRequested() {
        _uiState.update { it.copy(isChangingPassword = true) }
    }

    fun onLogoutRequested() {
        _uiState.update { it.copy(isLoggingOut = true) }
    }

    fun onDeleteAccountRequested() {
        _uiState.update { it.copy(isDeletingAccount = true) }
    }

    fun onPasswordDialogDismissed() {
        _uiState.update { it.copy(isChangingPassword = false) }
    }

    fun onChangePasswordConfirm(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        if (newPassword != confirmPassword) {
            _uiState.update {
                it.copy(errorMessage = "Las contraseñas no coinciden")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                val result = changePasswordUseCase(
                    userId = currentUserId,
                    oldPassword = currentPassword,
                    newPassword = newPassword
                )

                result.fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isChangingPassword = false,
                                successMessage = "Contraseña cambiada correctamente"
                            )
                        }
                    },
                    onFailure = { ex ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ex.message ?: "Error al cambiar contraseña"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onDeleteAccountDialogDismissed() {
        _uiState.update { it.copy(isDeletingAccount = false) }
    }

    fun performDeleteAccount() {
        // Implementación pendiente - requiere un DeleteAccountUseCase
        _uiState.update {
            it.copy(
                isDeletingAccount = false,
                errorMessage = "Funcionalidad de eliminar cuenta no implementada aún"
            )
        }
    }

    fun onLogoutDialogDismissed() {
        _uiState.update { it.copy(isLoggingOut = false) }
    }

    fun performLogout() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                logoutUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggingOut = false,
                        successMessage = "Sesión cerrada correctamente"
                    )
                }
                // Aquí deberías navegar a la pantalla de login
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cerrar sesión"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(errorMessage = null, successMessage = null)
        }
    }
}

// UI State actualizado
@RequiresApi(Build.VERSION_CODES.O)
data class ProfileUiState constructor(
    val name: String = "",
    val lastName: String = "",
    val email: String = "", // Solo lectura
    val phone: String = "",
    val birthday: LocalDate = LocalDate.now().minusYears(18),
    val imageUri: Uri? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isChangingPassword: Boolean = false,
    val isLoggingOut: Boolean = false,
    val isDeletingAccount: Boolean = false
) {
    fun hasBasicInfoErrors(): Boolean {
        return name.isBlank() ||
                lastName.isBlank() ||
                phone.isBlank() ||
                birthday.isAfter(LocalDate.now().minusYears(18))
    }
}
