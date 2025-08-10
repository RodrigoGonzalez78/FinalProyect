package com.example.finalproyect.domain.usecase.notification

import com.example.finalproyect.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int): Result<Unit> {
        if (notificationId <= 0) {
            return Result.failure(Exception("ID de notificación inválido"))
        }

        return notificationRepository.deleteNotification(notificationId)
    }
}
