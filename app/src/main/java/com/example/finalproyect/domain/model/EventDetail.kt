package com.example.finalproyect.domain.model

data class EventDetail(
    val event: Event,
    val location: Location,
    val organizers: List<OrganizerResponse>
)
