package com.example.finalproyect.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object QRCodeUtils {

    /**
     * Convierte un string base64 a Bitmap
     */
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            // Remover el prefijo "data:image/png;base64," si existe
            val cleanBase64 = if (base64String.startsWith("data:image")) {
                base64String.substring(base64String.indexOf(",") + 1)
            } else {
                base64String
            }

            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convierte un Bitmap a string base64
     */
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * Valida si un string es un QR code válido en base64
     */
    fun isValidQRCode(qrCode: String?): Boolean {
        if (qrCode.isNullOrBlank()) return false

        return try {
            val bitmap = base64ToBitmap(qrCode)
            bitmap != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtiene el tamaño de un QR code en bytes
     */
    fun getQRCodeSize(qrCode: String?): Long {
        if (qrCode.isNullOrBlank()) return 0L

        return try {
            val cleanBase64 = if (qrCode.startsWith("data:image")) {
                qrCode.substring(qrCode.indexOf(",") + 1)
            } else {
                qrCode
            }

            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            decodedBytes.size.toLong()
        } catch (e: Exception) {
            0L
        }
    }
}
