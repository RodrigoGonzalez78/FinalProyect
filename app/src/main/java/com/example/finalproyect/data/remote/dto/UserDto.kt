package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName ("id_user")
    val idUser: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
