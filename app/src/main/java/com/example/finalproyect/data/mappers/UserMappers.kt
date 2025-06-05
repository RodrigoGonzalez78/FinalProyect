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

@RequiresApi(Build.VERSION_CODES.O)
fun UserDto.toUser(): User {
    return User(
        id = idUser,
        name = name,
        lastName = lastName,
        birthday = LocalDate.parse(birthday),
        email = email,
        phone = phone,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = idUser,
        name = name,
        lastName = lastName,
        birthday = LocalDate.parse(birthday),
        email = email,
        phone = phone,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
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
