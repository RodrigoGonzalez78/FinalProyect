package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateOrganizerRequest(
    @SerializedName("id_rol")
    val idRol: Int
)
