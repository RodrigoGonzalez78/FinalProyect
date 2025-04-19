package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun saveUser(user: User)
    val prefsFlow: Flow<Int>
}