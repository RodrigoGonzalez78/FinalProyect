package com.example.finalproyect.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.UserEntity
import com.example.finalproyect.data.remote.dto.UserDto
import com.example.finalproyect.data.remote.dto.response.LoginResponse
import com.example.finalproyect.domain.model.AuthResult
import com.example.finalproyect.domain.model.User

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Extension para parsear fechas ISO 8601 de forma segura
@RequiresApi(Build.VERSION_CODES.O)
private fun String?.parseIsoDateTime(): LocalDateTime {
    if (this == null) return LocalDateTime.now()
    return try {
        val cleanDateTime = this.replace("Z", "").replace("+00:00", "")
        LocalDateTime.parse(cleanDateTime, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: DateTimeParseException) {
        try {
            val cleanDateTime = this.substring(0, 19)
            LocalDateTime.parse(cleanDateTime)
        } catch (e2: Exception) {
            LocalDateTime.now()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun String?.parseIsoDate(): LocalDate {
    if (this == null) return LocalDate.now()
    return try {
        val dateOnly = if (this.contains("T")) this.substring(0, 10) else this
        LocalDate.parse(dateOnly, DateTimeFormatter.ISO_DATE)
    } catch (e: DateTimeParseException) {
        LocalDate.now()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun UserDto.toUser(): User {
    return User(
        id = idUser,
        name = name ?: "",
        lastName = lastName ?: "",
        birthday = birthday.parseIsoDate(),
        email = email ?: "",
        phone = phone ?: "",
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = idUser,
        name = name ?: "",
        lastName = lastName ?: "",
        birthday = birthday.parseIsoDate(),
        email = email ?: "",
        phone = phone ?: "",
        createdAt = createdAt.parseIsoDateTime(),
        updatedAt = updatedAt.parseIsoDateTime()
    )
}

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        lastName = lastName,
        birthday = birthday,
        email = email,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun LoginResponse.toAuthResult(): AuthResult {
    return AuthResult(
        token = token,
        name = name,
        lastName = lastName,
        phone = phone,
        email = email
    )
}
