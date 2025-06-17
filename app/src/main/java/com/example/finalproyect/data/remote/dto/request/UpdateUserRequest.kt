package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("phone")
    val phone: String
)