package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.Notification


interface NotificationRepository {
    suspend fun createNotification(
        eventId: Int,
        title: String,
        description: String,
        image: String
    ): Result<Notification>

    suspend fun getNotificationsByEvent(eventId: Int): Result<List<Notification>>

    suspend fun deleteNotification(notificationId: Int): Result<Unit>
}
