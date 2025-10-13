package com.example.finalproyect.domain.usecase.auth

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.domain.model.AuthResult
import com.example.finalproyect.domain.repository.AuthRepository
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        name: String,
        lastName: String,
        email: String,
        password: String,
        birthday: String,
        phone: String
    ): Result<Boolean> {

        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }


        if (lastName.isBlank()) {
            return Result.failure(IllegalArgumentException("El apellido no puede estar vacío"))
        }


        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("El correo electrónico no es válido"))
        }


        val passwordError = validatePassword(password)
        if (passwordError.isNotEmpty()) {
            return Result.failure(IllegalArgumentException(passwordError))
        }

        if (phone.isBlank()) {
            return Result.failure(IllegalArgumentException("El teléfono no puede estar vacío"))
        }


        try {
            val birthdayDate = LocalDate.parse(birthday.substring(0, 10))
            val now = LocalDate.now()
            val age = Period.between(birthdayDate, now).years
            if (age < 18) {
                return Result.failure(IllegalArgumentException("Debes ser mayor de 18 años para registrarte"))
            }
        } catch (e: Exception) {
            return Result.failure(IllegalArgumentException("La fecha de nacimiento no es válida"))
        }

        return authRepository.register(name, lastName, email, password, birthday, phone)
    }

    private fun validatePassword(password: String): String {
        return when {
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            !password.any { it.isLowerCase() } -> "La contraseña debe contener al menos una minúscula"
            !password.any { it.isUpperCase() } -> "La contraseña debe contener al menos una mayúscula"
            else -> ""
        }
    }
}