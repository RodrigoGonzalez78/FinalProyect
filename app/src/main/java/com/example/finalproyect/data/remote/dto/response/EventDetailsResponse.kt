package com.example.finalproyect.data.remote.dto.response

import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.LocationDto
import com.example.finalproyect.data.remote.dto.OrganizerResponseDto
import com.google.gson.annotations.SerializedName


data class EventDetailResponse(

    @SerializedName("id_event")
    val idEvent: Int,
    @SerializedName("id_location")
    val idLocation: Int?,
    @SerializedName("date")
    val date: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("banner")
    val banner: String,
    @SerializedName("is_public")
    val isPublic: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("location")
    val location: LocationDto?,
    @SerializedName("organizers")
    val organizers: List<OrganizerResponseDto>
)
