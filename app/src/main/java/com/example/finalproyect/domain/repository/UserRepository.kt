package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UserRepository {
    suspend fun getUserById(userId: Int): Result<User>

    suspend fun updateUser(
        userId: Int,
        name: String,
        lastName: String,
        birthday: LocalDate,
        phone: String
    ): Result<User>

    suspend fun changePassword(
        userId: Int,
        oldPassword: String,
        newPassword: String
    ): Result<Unit>

    suspend fun getCurrentUser(): Result<User?>
}
