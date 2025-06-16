package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName


data class TicketTypeDto(
    @SerializedName("id_ticket_type")
    val idTicketType: Int,
    @SerializedName("id_event")
    val idEvent: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("available")
    val available: Int,
    @SerializedName("sold")
    val sold: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
