package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateOrganizerRequest(
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("id_rol")
    val idRol: Int
)