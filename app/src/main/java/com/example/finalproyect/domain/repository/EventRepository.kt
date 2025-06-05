package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.PaginatedEvents
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface EventRepository {
    suspend fun searchPublicEvents(
        name: String,
        page: Int = 1,
        size: Int = 10
    ): Result<PaginatedEvents>

    suspend fun getUserEvents(
        page: Int = 1,
        size: Int = 10
    ): Result<PaginatedEvents>

    suspend fun getEventById(eventId: Int): Result<EventDetail>

    suspend fun updateEvent(
        eventId: Int,
        name: String,
        description: String?,
        date: LocalDateTime,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        banner: String?,
        locationId: Int,
        isPublic: Boolean
    ): Result<Event>

    suspend fun deleteEvent(eventId: Int): Result<Unit>
}