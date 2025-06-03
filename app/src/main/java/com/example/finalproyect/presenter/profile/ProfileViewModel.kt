package com.example.finalproyect.presenter.profile

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.usecase.auth.GetCurrentUserUseCase
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

// 2) Crea el ViewModel que inyecta GetCurrentUserUseCase y (opcional) UpdateUserUseCase
@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    //private val updateUserUseCase: UpdateUserUseCase // asumo que existe para guardar cambios
) : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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
                        // Mapeamos los campos de User al ProfileUiState
                        _uiState.update {
                            it.copy(
                                name = user.name,
                                lastName = user.lastName,
                                email = user.email,
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

    // 3) Funciones para actualizar cada campo en el UI State
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

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = null, successMessage = null) }
    }

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

    // 4) Método para guardar los cambios: llama a UpdateUserUseCase
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
                // Asumo que UpdateUserUseCase recibe un objeto User o similar
                val userToUpdate = User(
                    name = current.name,
                    lastName = current.lastName,
                    email = current.email,
                    phone = current.phone,
                    birthday = current.birthday,
                    id = 0,
                )

                /*val result = updateUserUseCase(userToUpdate)
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
                )*/
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
        TODO("Not yet implemented")
    }

    fun onLogoutRequested() {
        TODO("Not yet implemented")
    }

    fun onDeleteAccountRequested() {
        TODO("Not yet implemented")
    }

    fun onPasswordDialogDismissed() {
        TODO("Not yet implemented")
    }

    fun onChangePasswordConfirm(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {

    }

    fun onDeleteAccountDialogDismissed() {
        TODO("Not yet implemented")
    }

    fun performDeleteAccount() {
        TODO("Not yet implemented")
    }

    fun onLogoutDialogDismissed() {
        TODO("Not yet implemented")
    }

    fun performLogout() {
        TODO("Not yet implemented")
    }
}


// 1) Define primero el UI State
@RequiresApi(Build.VERSION_CODES.O)
data class ProfileUiState constructor(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthday: LocalDate = LocalDate.now(),
    val imageUri: Uri? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isChangingPassword: Boolean = false,
    val isLoggingOut: Boolean = false,
    val isDeletingAccount: Boolean=false
) {




    fun hasBasicInfoErrors(): Boolean {
        return name.isBlank() || lastName.isBlank() || email.isBlank()
    }
}
