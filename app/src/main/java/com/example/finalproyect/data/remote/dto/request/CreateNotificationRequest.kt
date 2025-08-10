package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateNotificationRequest(
    @SerializedName("id_event")
    val idEvent: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String
)
