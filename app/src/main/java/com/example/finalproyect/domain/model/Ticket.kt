package com.example.finalproyect.domain.model

import java.time.LocalDateTime


data class Ticket(
    val id: Int,
    val ticketTypeId: Int,
    val userId: Int,
    val qrCode: String?,
    val price: Double,
    val entryNumber: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    val hasQrCode: Boolean
        get() = !qrCode.isNullOrBlank()

    val formattedPrice: String
        get() = "$${String.format("%.2f", price)}"

    val isValid: Boolean
        get() = hasQrCode && entryNumber.isNotBlank()
}