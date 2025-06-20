package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.AuthResult
import com.example.finalproyect.domain.model.User
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String,
        birthday: String,
        phone: String
    ): Result<AuthResult>
    suspend fun logout()
    fun isLoggedIn(): Flow<Boolean>
}