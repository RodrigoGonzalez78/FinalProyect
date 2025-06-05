package com.example.finalproyect.domain.model

data class PaginatedEvents(
    val page: Int,
    val size: Int,
    val events: List<Event>
)