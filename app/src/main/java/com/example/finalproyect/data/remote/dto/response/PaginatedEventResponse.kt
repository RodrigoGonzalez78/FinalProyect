package com.example.finalproyect.data.remote.dto.response

import com.example.finalproyect.data.remote.dto.EventDto
import com.google.gson.annotations.SerializedName

data class PaginatedEventsResponse(
    @SerializedName ("page")
    val page: Int,
    @SerializedName ("size")
    val size: Int,
    @SerializedName ("events")
    val events: List<EventDto>?
)