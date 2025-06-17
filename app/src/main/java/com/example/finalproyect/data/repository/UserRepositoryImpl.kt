package com.example.finalproyect.data.repository


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.mappers.toUser
import com.example.finalproyect.data.mappers.toUserEntity
import com.example.finalproyect.data.remote.api.UserApi
import com.example.finalproyect.data.remote.dto.request.ChangePasswordRequest
import com.example.finalproyect.data.remote.dto.request.UpdateUserRequest
import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import com.example.finalproyect.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val preferenceManager: PreferenceManager
) : UserRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val response = userApi.getUserById(userId)

            if (response.isSuccessful) {
                response.body()?.let { userDto ->
                    // Guardar en base de datos local
                    val userEntity = userDto.toUserEntity()
                    userDao.insertUser(userEntity)

                    // Convertir a modelo de dominio y retornar
                    val user = userDto.toUser()
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            // En caso de error de red, intentar obtener de base de datos local
            try {
                val localUser = userDao.getUserById(userId)
                localUser?.let { userEntity ->
                    val user = userEntity.toUser()
                    Result.success(user)
                } ?: Result.failure(Exception("User not found locally"))
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateUser(
        userId: Int,
        name: String,
        lastName: String,
        birthday: LocalDate,
        phone: String
    ): Result<User> {
        return try {
            val request = UpdateUserRequest(
                name = name,
                lastName = lastName,
                birthday = birthday.atStartOfDay().format(DateTimeFormatter.ISO_DATE_TIME) + "Z",
                phone = phone
            )

            val response = userApi.updateUser(userId, request)

            if (response.isSuccessful) {
                response.body()?.let { userDto ->
                    // Actualizar en base de datos local
                    val userEntity = userDto.toUserEntity()
                    userDao.updateUser(userEntity)

                    // Convertir a modelo de dominio y retornar
                    val user = userDto.toUser()
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(
        userId: Int,
        oldPassword: String,
        newPassword: String
    ): Result<Unit> {
        return try {
            val request = ChangePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword
            )

            val response = userApi.changePassword(userId, request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val currentUserId = preferenceManager.getCurrentUserID().first().toString()

            Log.e("Ayuda",currentUserId)
            if (currentUserId != null) {
                getUserById(currentUserId.toInt())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
