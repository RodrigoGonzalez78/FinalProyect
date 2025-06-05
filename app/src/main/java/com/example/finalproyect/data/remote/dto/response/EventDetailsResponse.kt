package com.example.finalproyect.data.remote.dto.response

import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.LocationDto
import com.example.finalproyect.data.remote.dto.OrganizerResponseDto
import com.google.gson.annotations.SerializedName


data class EventDetailResponse(
    @SerializedName("event")
    val event: EventDto,
    @SerializedName("location")
    val location: LocationDto,
    @SerializedName("organizers")
    val organizers: List<OrganizerResponseDto>
)
