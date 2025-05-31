package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("birthday") val birthday: String, // ISO format
    @SerializedName("phone") val phone: String
)