package com.example.finalproyect.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id_notification")
    val idNotification: Int,

    @SerializedName("id_event")
    val idEvent: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)
