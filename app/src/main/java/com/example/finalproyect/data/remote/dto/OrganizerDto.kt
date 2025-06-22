package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrganizerDto(
    @SerializedName ("id_organizer")
    val idOrganizer: Int,
    @SerializedName("id_event")
    val idEvent: Int,
    @SerializedName("id_rol")
    val idRol: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null
)