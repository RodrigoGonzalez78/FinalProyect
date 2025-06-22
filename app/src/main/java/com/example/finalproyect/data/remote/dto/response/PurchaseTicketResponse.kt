package com.example.finalproyect.data.remote.dto.response

import com.example.finalproyect.data.remote.dto.TicketDto
import com.google.gson.annotations.SerializedName


data class PurchaseTicketResponse(
    @SerializedName ("message")
    val message: String,
    @SerializedName ("ticket")
    val ticket: TicketDto
)
