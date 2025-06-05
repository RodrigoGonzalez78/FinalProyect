package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateEventRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("banner")
    val banner: String?,
    @SerializedName("id_location")
    val idLocation: Int,
    @SerializedName("is_public")
    val isPublic: Boolean
)