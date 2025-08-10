package com.example.finalproyect.domain.usecase.notification

import com.example.finalproyect.domain.model.Notification
import com.example.finalproyect.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsByEventUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(eventId: Int): Result<List<Notification>> {
        if (eventId <= 0) {
            return Result.failure(Exception("ID de evento inválido"))
        }

        return notificationRepository.getNotificationsByEvent(eventId)
    }
}
