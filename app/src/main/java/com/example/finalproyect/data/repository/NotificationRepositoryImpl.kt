package com.example.finalproyect.data.repository


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.mappers.toNotification
import com.example.finalproyect.data.remote.dto.request.CreateNotificationRequest
import com.example.finalproyect.data.remote.api.NotificationApi
import com.example.finalproyect.domain.model.Notification
import com.example.finalproyect.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
) : NotificationRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createNotification(
        eventId: Int,
        title: String,
        description: String,
        image: String
    ): Result<Notification> {
        return try {
            val request = CreateNotificationRequest(
                idEvent = eventId,
                title = title,
                description = description,
                image = image
            )

            val response = notificationApi.createNotification(eventId, request)

            if (response.isSuccessful) {
                response.body()?.let { notificationResponse ->
                    Result.success(notificationResponse.toNotification())
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Datos de notificación inválidos"
                    403 -> "No tienes permiso para crear notificaciones en este evento"
                    500 -> "Error interno del servidor"
                    else -> "Error al crear notificación: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNotificationsByEvent(eventId: Int): Result<List<Notification>> {
        return try {
            val response = notificationApi.getNotificationsByEvent(eventId)

            if (response.isSuccessful) {
                response.body()?.let { notificationResponses ->
                    Result.success(notificationResponses.map { it.toNotification() })
                } ?: Result.success(emptyList())
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "ID de evento inválido"
                    500 -> "Error interno del servidor"
                    else -> "Error al obtener notificaciones: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNotification(notificationId: Int): Result<Unit> {
        return try {
            val response = notificationApi.deleteNotification(notificationId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "ID de notificación inválido"
                    403 -> "No tienes permiso para eliminar esta notificación"
                    500 -> "Error interno del servidor"
                    else -> "Error al eliminar notificación: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
