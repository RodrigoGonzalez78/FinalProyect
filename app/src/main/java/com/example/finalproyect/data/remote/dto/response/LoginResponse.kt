package com.example.finalproyect.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
)
