package com.example.finalproyect.data.repository


import android.text.BoringLayout
import android.util.Log
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
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = authApi.login(LoginRequest(email, password))

            preferenceManager.saveAuthToken(response.token)
            preferenceManager.saveCurrentUserEmail(response.email)
            preferenceManager.saveCurrentUserID(response.userID)


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
    ): Result<Boolean> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                name = name,
                lastName = lastName,
                birthday = birthday,
                phone = phone
            )
            authApi.register(request)
            Result.success(true)
        } catch (e: HttpException) {
            Result.failure(Exception("Error del servidor: ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }




    override suspend fun logout() {
        preferenceManager.clearAuthToken()
        preferenceManager.clearCurrentUserEmail()
        preferenceManager.clearCurrentUserID()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return preferenceManager.getAuthToken().map { it.isNotEmpty() }
    }
}
