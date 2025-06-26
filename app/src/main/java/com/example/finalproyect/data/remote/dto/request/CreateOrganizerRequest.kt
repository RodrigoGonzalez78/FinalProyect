package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateOrganizerRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("id_rol")
    val idRol: Int
)