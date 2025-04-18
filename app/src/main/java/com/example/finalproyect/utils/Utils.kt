package com.example.finalproyect.utils

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {
    fun isJwtValid(jwt: String?): Boolean {
        if (jwt.isNullOrEmpty()) return false

        return try {
            val parts = jwt.split(".")
            if (parts.size != 3) return false

            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            val expiry = JSONObject(payload).getLong("exp") * 1000
            System.currentTimeMillis() < expiry
        } catch (e: Exception) {
            false
        }
    }

    fun formatDateLegacy(originalDate: String?): String {
        if (originalDate.isNullOrBlank()) {
            return "Fecha no disponible"
        }

        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date: Date? = inputFormat.parse(originalDate)
            date?.let { outputFormat.format(it) } ?: "Formato inválido"
        } catch (e: Exception) {

            "Formato inválido"
        }
    }

    fun formatISODateLegacy(originalDate: String?): String {
        if (originalDate.isNullOrBlank()) return "Fecha no disponible"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(originalDate.trim())
            if (date != null) outputFormat.format(date) else "Formato inválido"
        } catch (e: Exception) {
            "Formato inválido"
        }
    }
}