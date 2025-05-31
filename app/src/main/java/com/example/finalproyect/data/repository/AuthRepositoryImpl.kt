package com.example.finalproyect.data.repository


import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.mappers.toAuthResult
import com.example.finalproyect.data.mappers.toUser
import com.example.finalproyect.data.remote.api.AuthApi
import com.example.finalproyect.data.remote.dto.request.LoginRequest
import com.example.finalproyect.data.remote.dto.request.RegisterRequest
import com.example.finalproyect.domain.model.AuthResult
import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.AuthRepository
import com.example.finalproyect.utils.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            preferenceManager.saveAuthToken(response.token)
            preferenceManager.saveCurrentUserEmail(response.email)

            // Podríamos obtener más datos del usuario y guardarlos localmente
            Result.success(response.toAuthResult())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String,
        birthday: String,
        phone: String
    ): Result<AuthResult> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                name = name,
                lastName = lastName,
                birthday = birthday,
                phone = phone
            )
            val response = authApi.register(request)
            preferenceManager.saveAuthToken(response.token)
            preferenceManager.saveCurrentUserEmail(response.email)

            Result.success(response.toAuthResult())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        preferenceManager.clearAuthToken()
        preferenceManager.clearCurrentUserEmail()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return preferenceManager.getAuthToken().map { it.isNotEmpty() }
    }

    override fun getCurrentUser(): Flow<User?> {
        return preferenceManager.getCurrentUserEmail().map { email ->
            if (email.isEmpty()) {
                null
            } else {
                userDao.getUserByEmail(email)?.toUser()
            }
        }
    }
}
