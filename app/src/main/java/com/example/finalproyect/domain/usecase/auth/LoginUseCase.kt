package com.example.finalproyect.domain.usecase.auth

import com.example.finalproyect.domain.model.AuthResult
import com.example.finalproyect.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResult> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo electrónico no puede estar vacío"))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("El correo electrónico no es válido"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        return authRepository.login(email, password)
    }
}