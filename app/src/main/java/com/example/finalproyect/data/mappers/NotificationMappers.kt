package com.example.finalproyect.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.finalproyect.data.remote.dto.response.NotificationResponse
import com.example.finalproyect.domain.model.Notification

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationResponse.toNotification(): Notification {
    return Notification(
        id = idNotification,
        eventId = idEvent,
        title = title,
        description = description,
        image = image,
        createdAt = parseDateTime(createdAt),
        updatedAt = parseDateTime(updatedAt)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun parseDateTime(dateString: String): LocalDateTime {
    return try {
        // Intentar parsear con diferentes formatos
        val formatters = listOf(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        )

        for (formatter in formatters) {
            try {
                return LocalDateTime.parse(dateString.replace("Z", ""), formatter)
            } catch (e: Exception) {
                continue
            }
        }

        // Si no se puede parsear, usar fecha actual
        LocalDateTime.now()
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}
