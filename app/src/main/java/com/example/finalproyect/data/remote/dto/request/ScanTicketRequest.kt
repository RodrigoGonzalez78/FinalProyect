package com.example.finalproyect.data.remote.dto.request


import com.google.gson.annotations.SerializedName

data class ScanTicketRequest(
    @SerializedName("qr_code")
    val qrCode: String,
    @SerializedName("event_id")
    val eventId: Int
)
