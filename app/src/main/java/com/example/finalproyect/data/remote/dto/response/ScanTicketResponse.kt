package com.example.finalproyect.data.remote.dto.response

import com.example.finalproyect.data.remote.dto.TicketDto
import com.google.gson.annotations.SerializedName

data class ScanTicketResponse(
    @SerializedName("valid")
    val valid: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("ticket")
    val ticket: TicketDto,
    @SerializedName("entry_count")
    val entryCount: Int,
    @SerializedName("is_reentry")
    val isReentry: Boolean,
    @SerializedName("entry_time")
    val entryTime: String
)