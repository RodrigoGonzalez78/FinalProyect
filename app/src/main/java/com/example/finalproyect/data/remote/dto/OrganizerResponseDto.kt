package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrganizerResponseDto(
    @SerializedName("id_organizer") val idOrganizer: Long,
    @SerializedName("id_event") val idEvent: Long,
    @SerializedName("id_rol") val idRol: Long,
    @SerializedName("id_user") val idUser: Long,
    @SerializedName("created_at") val createdAt: String, // ISO format
    @SerializedName("updated_at") val updatedAt: String, // ISO format
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String
)
