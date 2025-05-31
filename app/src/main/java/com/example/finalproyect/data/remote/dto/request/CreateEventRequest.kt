package com.example.finalproyect.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateEventRequest(
    @SerializedName("event") val event: EventRequest,
    @SerializedName("location") val location: LocationRequest
)

data class EventRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String, // ISO format
    @SerializedName("banner") val banner: String,
    @SerializedName("start_time") val startTime: String, // ISO format
    @SerializedName("end_time") val endTime: String, // ISO format
    @SerializedName("is_public") val isPublic: Boolean
)

data class LocationRequest(
    @SerializedName("name") val name: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)