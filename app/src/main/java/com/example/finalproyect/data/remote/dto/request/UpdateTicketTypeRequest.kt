package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName


data class UpdateTicketTypeRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("available")
    val available: Int,
    @SerializedName("price")
    val price: Double
)
