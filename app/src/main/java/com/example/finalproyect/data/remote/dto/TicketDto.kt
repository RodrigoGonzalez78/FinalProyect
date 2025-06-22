package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName


data class TicketDto(
    @SerializedName("id_ticket")
    val idTicket: Int,
    @SerializedName("id_ticket_type")
    val idTicketType: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("qr_code")
    val qrCode: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("entry_number")
    val entryNumber: String,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
