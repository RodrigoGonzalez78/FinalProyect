package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("id_event") val idEvent: Long,
    @SerializedName("id_location") val idLocation: Long?,
    @SerializedName("date") val date: String, // ISO format
    @SerializedName("start_time") val startTime: String, // ISO format
    @SerializedName("end_time") val endTime: String, // ISO format
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("banner") val banner: String?,
    @SerializedName("is_public") val isPublic: Boolean,
    @SerializedName("created_at") val createdAt: String, // ISO format
    @SerializedName("updated_at") val updatedAt: String, // ISO format
)