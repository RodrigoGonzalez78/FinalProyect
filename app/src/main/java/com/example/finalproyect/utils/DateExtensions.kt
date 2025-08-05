package com.example.finalproyect.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Extensiones para facilitar el trabajo con fechas
 */

/**
 * Convierte un String a LocalDate
 * Ejemplo: "2025-06-08".toLocalDate()
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String?.toLocalDate(pattern: String = "yyyy-MM-dd"): LocalDate? {
    if (this.isNullOrBlank()) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDate.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

/**
 * Convierte un String a LocalTime
 * Ejemplo: "15:30".toLocalTime()
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String?.toLocalTime(pattern: String = "HH:mm"): LocalTime? {
    if (this.isNullOrBlank()) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalTime.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

/**
 * Convierte un String a LocalDateTime
 * Ejemplo: "2025-06-08 15:30".toLocalDateTime("yyyy-MM-dd HH:mm")
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String?.toLocalDateTime(pattern: String = "yyyy-MM-dd HH:mm"): LocalDateTime? {
    if (this.isNullOrBlank()) return null

    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDateTime.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

/**
 * Convierte un String ISO a LocalDateTime
 * Ejemplo: "2025-06-08T15:30:00Z".toLocalDateTimeFromIso()
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String?.toLocalDateTimeFromIso(): LocalDateTime? {
    if (this.isNullOrBlank()) return null

    return try {
        val cleanDateTime = this.replace("Z", "").replace("+00:00", "")
        LocalDateTime.parse(cleanDateTime, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        try {
            val cleanDateTime = this.substring(0, 19)
            LocalDateTime.parse(cleanDateTime)
        } catch (e2: Exception) {
            null
        }
    }
}

/**
 * Formatea un LocalDate a String
 * Ejemplo: localDate.format("dd/MM/yyyy")
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Formatea un LocalTime a String
 * Ejemplo: localTime.format("HH:mm")
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Formatea un LocalDateTime a String
 * Ejemplo: localDateTime.format("dd/MM/yyyy HH:mm")
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Formatea un LocalDate a formato largo en español
 * Ejemplo: localDate.formatFullSpanish() -> "Domingo, 8 de junio de 2025"
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.formatFullSpanish(): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es"))
    return this.format(formatter)
}

/**
 * Formatea un LocalDateTime a ISO 8601
 * Ejemplo: localDateTime.toIsoString() -> "2025-06-08T15:30:00Z"
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toIsoString(): String {
    return this.format(DateTimeFormatter.ISO_DATE_TIME) + "Z"
}
