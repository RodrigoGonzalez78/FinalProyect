package com.example.finalproyect.domain.usecase.notification

import com.example.finalproyect.domain.model.Notification
import com.example.finalproyect.domain.repository.NotificationRepository
import javax.inject.Inject

class CreateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        title: String,
        description: String,
        image: String = ""
    ): Result<Notification> {
        // Validaciones
        if (title.isBlank()) {
            return Result.failure(Exception("El título es obligatorio"))
        }

        if (description.isBlank()) {
            return Result.failure(Exception("La descripción es obligatoria"))
        }

        if (title.length > 100) {
            return Result.failure(Exception("El título no puede exceder 100 caracteres"))
        }

        if (description.length > 500) {
            return Result.failure(Exception("La descripción no puede exceder 500 caracteres"))
        }

        return notificationRepository.createNotification(
            eventId = eventId,
            title = title.trim(),
            description = description.trim(),
            image = image.trim()
        )
    }
}
